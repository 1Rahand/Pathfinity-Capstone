package data

import androidx.lifecycle.ViewModel
import database.MyDao
import database.TimeDao
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import supabase.ChatConversation
import supabase.ChatMessage
import supabase.ChatParticipant
import supabase.Course
import supabase.CourseCategory
import supabase.CourseChanges
import supabase.CourseReview
import supabase.CourseVideo
import supabase.CourseVideoProgress
import supabase.GiftCard
import supabase.Internship
import supabase.LiveSession
import supabase.SettingsSupabaseTable
import supabase.SupabaseTable
import supabase.UserAdmin
import supabase.UserAlumni
import supabase.UserCompany
import supabase.UserContentCreator
import supabase.UserStudent
import supabase.VideoChange
import supabase.getUpdatedList

class Syncer(
   private val supabase: SupabaseClient,
   private val repoSetting: RepoSetting,
   private val dao: MyDao,
   private val timeDao: TimeDao
) : ViewModel() {
   private val _isSyncing = MutableStateFlow(false)
   val isSyncing = _isSyncing.asStateFlow()

   suspend fun sync() = wrapResultRepo {
      if (_isSyncing.value) return@wrapResultRepo

      withContext(Dispatchers.IO) {
         _isSyncing.value = true
         println("Syncer: Starting sync")

         try {
            syncAllData()
            println("Syncer: Sync completed successfully")
         } finally {
            _isSyncing.value = false
         }
      }
   }

   private suspend fun syncAllData() = coroutineScope {
      println("Syncer: Syncing all data")
      dao.clearAllData()
      // Fetch all data concurrently
      val coursesDeferred = async { getUpdatedList(Course("", creatorId = "")) }
      val courseChangesDeferred = async { getUpdatedList(CourseChanges("")) }
      val courseVideosDeferred = async { getUpdatedList(CourseVideo("", "", "", "", "", sequenceNumber = 0)) }
      val courseCategoriesDeferred = async { getUpdatedList(CourseCategory("", "")) }
      val internshipsDeferred = async { getUpdatedList(Internship.empty) }
      val userAdminsDeferred = async { getUpdatedList(UserAdmin("", "", "", "")) }
      val userAlumniDeferred = async { getUpdatedList(UserAlumni("", "", "", "")) }
      val userCompaniesDeferred = async { getUpdatedList(UserCompany("", "", "")) }
      val userContentCreatorsDeferred = async { getUpdatedList(UserContentCreator("", "", "", "")) }
      val userStudentsDeferred = async { getUpdatedList(UserStudent("")) }
      val videoChangesDeferred = async { getUpdatedList(VideoChange("")) }
      val settingsDeferred = async { getUpdatedList(SettingsSupabaseTable("", "")) }
      val chatConversationsDeferred = async { getUpdatedList(ChatConversation("")) }
      val chatParticipantsDeferred = async { getUpdatedList(ChatParticipant("", "", "", "")) }
      val chatMessagesDeferred = async { getUpdatedList(ChatMessage("", "", "", "")) }
      val liveSessionsDeferred = async { getUpdatedList(LiveSession("", "", "", "", "", "")) }
      val giftCardsDeferred = async { getUpdatedList(GiftCard.empty) }
      val courseVideoProgressDeferred = async { getUpdatedList(CourseVideoProgress("", "", false)) }
      val courseReviewsDeferred = async { getUpdatedList(CourseReview.empty) }

      // Await all results
      val courses = coursesDeferred.await()
      val courseChanges = courseChangesDeferred.await()
      val courseVideos = courseVideosDeferred.await()
      val courseCategories = courseCategoriesDeferred.await()
      val internships = internshipsDeferred.await()
      val userAdmins = userAdminsDeferred.await()
      val userAlumni = userAlumniDeferred.await()
      val userCompanies = userCompaniesDeferred.await()
      val userContentCreators = userContentCreatorsDeferred.await()
      val userStudents = userStudentsDeferred.await()
      val videoChanges = videoChangesDeferred.await()
      val settingsList = settingsDeferred.await()
      val chatConversations = chatConversationsDeferred.await()
      val chatParticipants = chatParticipantsDeferred.await()
      val chatMessages = chatMessagesDeferred.await()
      val liveSessions = liveSessionsDeferred.await()
      val giftCards = giftCardsDeferred.await()
      val courseVideoProgress = courseVideoProgressDeferred.await()
        val courseReviews = courseReviewsDeferred.await()

      repoSetting.saveSettings(settingsList)
      // Upsert all data in a single call
      dao.upsertAll(
         courses = courses,
         courseChanges = courseChanges,
         courseVideos = courseVideos,
         courseCategories = courseCategories,
         internships = internships,
         userAdmins = userAdmins,
         userAlumni = userAlumni,
         userCompanies = userCompanies,
         userContentCreators = userContentCreators,
         userStudents = userStudents,
         videoChanges = videoChanges,
         chatConversations = chatConversations,
         chatParticipants = chatParticipants,
         chatMessages = chatMessages,
         liveSessions = liveSessions,
         giftCards = giftCards,
         courseVideoProgress = courseVideoProgress,
         courseReviews = courseReviews
      )

      println("Syncer: All data synced successfully")
   }

   suspend fun reloadProfile() {
      coroutineScope {
         val profileDeferred = async { getUpdatedList(UserStudent("")) }
         val giftCardSupabaseTableDeferred = async { getUpdatedList(GiftCard.empty) }

         val profile = profileDeferred.await()
         val giftCardSupabaseTable = giftCardSupabaseTableDeferred.await()
         dao.upsertUserStudents(profile)
         dao.upsertGiftCards(giftCardSupabaseTable)
      }
   }

   private suspend inline fun <reified T : SupabaseTable> getUpdatedList(tableInstance: T): List<T> {
      val tableName = tableInstance.tableName
      println("Syncer: Fetching $tableName")

      val instant = timeDao.getMaxUpdatedAt(tableInstance)
      return supabase.getUpdatedList(tableInstance, instant).also {
         println("Syncer: $tableName fetched, size is ${it.size}")
      }
   }

}