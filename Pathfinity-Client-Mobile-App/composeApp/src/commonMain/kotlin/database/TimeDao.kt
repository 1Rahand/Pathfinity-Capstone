package database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds
import supabase.*

@Dao
interface TimeDao {

   @Query("SELECT MAX(updated_at) FROM ${Course.TABLE_NAME}")
   suspend fun courses(): Long?

   @Query("SELECT MAX(updated_at) FROM ${CourseCategory.TABLE_NAME}")
   suspend fun courseCategories(): Long?

   @Query("SELECT MAX(updated_at) FROM ${CourseChanges.TABLE_NAME}")
   suspend fun courseChanges(): Long?

   @Query("SELECT MAX(updated_at) FROM ${CourseVideo.TABLE_NAME}")
   suspend fun courseVideos(): Long?

   @Query("SELECT MAX(updated_at) FROM ${Internship.TABLE_NAME}")
   suspend fun internships(): Long?

   @Query("SELECT MAX(updated_at) FROM ${UserAdmin.TABLE_NAME}")
   suspend fun userAdmins(): Long?

   @Query("SELECT MAX(updated_at) FROM ${UserAlumni.TABLE_NAME}")
   suspend fun userAlumni(): Long?

   @Query("SELECT MAX(updated_at) FROM ${UserCompany.TABLE_NAME}")
   suspend fun userCompanies(): Long?

   @Query("SELECT MAX(updated_at) FROM ${UserContentCreator.TABLE_NAME}")
   suspend fun userContentCreators(): Long?

   @Query("SELECT MAX(updated_at) FROM ${UserStudent.TABLE_NAME}")
   suspend fun userStudents(): Long?

   @Query("SELECT MAX(updated_at) FROM ${VideoChange.TABLE_NAME}")
   suspend fun videoChanges(): Long?

   @Query("SELECT MAX(updated_at) FROM ${ChatConversation.TABLE_NAME}")
   suspend fun chatConversations(): Long?

   @Query("SELECT MAX(updated_at) FROM ${ChatParticipant.TABLE_NAME}")
   suspend fun chatParticipants(): Long?

   @Query("SELECT MAX(updated_at) FROM ${ChatMessage.TABLE_NAME}")
   suspend fun chatMessages(): Long?

   @Query("SELECT MAX(updated_at) FROM ${LiveSession.TABLE_NAME}")
   suspend fun liveSessions(): Long?

   @Query("SELECT MAX(updated_at) FROM ${GiftCard.TABLE_NAME}")
   suspend fun giftCards(): Long?

   @Query("SELECT MAX(updated_at) FROM ${SettingsSupabaseTable.TABLE_NAME}")
   suspend fun settings(): Long?

   @Query("SELECT MAX(updated_at) FROM ${CourseVideoProgress.TABLE_NAME}")
   suspend fun courseVideoProgress(): Long?

    @Query("SELECT MAX(updated_at) FROM ${CourseReview.TABLE_NAME}")
    suspend fun courseReviews(): Long?

   /**
    * Calls the correct query method based on the provided [SupabaseTable],
    * then converts the result to an [Instant].
    * If no value exists, it returns [Instant.fromEpochMilliseconds(0L)].
    */
   suspend fun getMaxUpdatedAt(instance: SupabaseTable): Instant {
      val longValue = when (instance) {
         is Course -> courses()
         is CourseCategory -> courseCategories()
         is CourseChanges -> courseChanges()
         is CourseVideo -> courseVideos()
         is Internship -> internships()
         is UserAdmin -> userAdmins()
         is UserAlumni -> userAlumni()
         is UserCompany -> userCompanies()
         is UserContentCreator -> userContentCreators()
         is UserStudent -> userStudents()
         is VideoChange -> videoChanges()
         is ChatConversation -> chatConversations()
         is ChatParticipant -> chatParticipants()
         is ChatMessage -> chatMessages()
         is LiveSession -> liveSessions()
         is GiftCard -> giftCards()
         is SettingsSupabaseTable -> settings()
         is CourseVideoProgress -> courseVideoProgress()
         is CourseReview -> courseReviews()
      }
      return longValue?.let { Instant.fromEpochMilliseconds(it).plus(1.0.seconds) }
         ?: Instant.fromEpochMilliseconds(0L)
   }
}