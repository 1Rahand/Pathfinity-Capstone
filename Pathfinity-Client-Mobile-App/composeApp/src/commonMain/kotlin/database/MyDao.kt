package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
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
import supabase.UserAdmin
import supabase.UserAlumni
import supabase.UserCompany
import supabase.UserContentCreator
import supabase.UserStudent
import supabase.VideoChange

@Dao
interface MyDao {
   // ===== Course CRUD operations =====
   @Query("SELECT * FROM ${Course.TABLE_NAME} WHERE is_approved = TRUE AND is_active = TRUE")
   fun getAllCourses(): Flow<List<Course>>

   @Query("SELECT * FROM ${Course.TABLE_NAME} WHERE id = :id")
   suspend fun getCourseById(id: String): Course?

   @Query("SELECT * FROM ${Course.TABLE_NAME} WHERE id = :id")
   fun getCourseByIdFlow(id: String): Flow<Course?>

   @Query("SELECT * FROM ${Course.TABLE_NAME} WHERE creator_id = :creatorId AND is_approved = TRUE AND is_active = TRUE ORDER BY title")
   fun getCoursesByCreatorIdFlow(creatorId: String): Flow<List<Course>>

   @Upsert
   suspend fun upsertCourse(course: Course)

   @Upsert
   suspend fun upsertCourses(courses: List<Course>)

   @Delete
   suspend fun deleteCourse(course: Course)

   @Query("DELETE FROM ${Course.TABLE_NAME}")
   suspend fun deleteAllCourses()

   // ===== CourseCategory CRUD operations =====
   @Query("SELECT * FROM ${CourseCategory.TABLE_NAME}")
   fun getAllCourseCategories(): Flow<List<CourseCategory>>

   @Query("SELECT * FROM ${CourseCategory.TABLE_NAME} WHERE id = :id")
   suspend fun getCourseCategoryById(id: String): CourseCategory?

   @Query("SELECT * FROM ${CourseCategory.TABLE_NAME} WHERE id = :id")
   fun getCourseCategoryByIdFlow(id: String): Flow<CourseCategory?>

   @Upsert
   suspend fun upsertCourseCategory(courseCategory: CourseCategory)

   @Upsert
   suspend fun upsertCourseCategories(courseCategories: List<CourseCategory>)

   @Delete
   suspend fun deleteCourseCategory(courseCategory: CourseCategory)

   @Query("DELETE FROM ${CourseCategory.TABLE_NAME}")
   suspend fun deleteAllCourseCategories()

   // ===== CourseChanges CRUD operations =====
   @Query("SELECT * FROM ${CourseChanges.TABLE_NAME}")
   fun getAllCourseChanges(): Flow<List<CourseChanges>>

   @Query("SELECT * FROM ${CourseChanges.TABLE_NAME} WHERE id = :id")
   suspend fun getCourseChangesById(id: String): CourseChanges?

   @Query("SELECT * FROM ${CourseChanges.TABLE_NAME} WHERE id = :id")
   fun getCourseChangesByIdFlow(id: String): Flow<CourseChanges?>

   @Upsert
   suspend fun upsertCourseChange(courseChange: CourseChanges)

   @Upsert
   suspend fun upsertCourseChanges(courseChanges: List<CourseChanges>)

   @Delete
   suspend fun deleteCourseChange(courseChange: CourseChanges)

   @Query("DELETE FROM ${CourseChanges.TABLE_NAME}")
   suspend fun deleteAllCourseChanges()

   // ===== CourseVideo CRUD operations =====
   @Query("SELECT * FROM ${CourseVideo.TABLE_NAME} WHERE is_reviewed = TRUE AND is_approved = TRUE ORDER BY sequence_number")
   fun getAllCourseVideos(): Flow<List<CourseVideo>>

   @Query("SELECT * FROM ${CourseVideo.TABLE_NAME} WHERE id = :id AND is_reviewed = TRUE AND is_approved = TRUE")
   suspend fun getCourseVideoById(id: String): CourseVideo?

   @Query("SELECT * FROM ${CourseVideo.TABLE_NAME} WHERE id = :id AND is_reviewed = TRUE AND is_approved = TRUE")
   fun getCourseVideoByIdFlow(id: String): Flow<CourseVideo?>

   @Query("SELECT * FROM ${CourseVideo.TABLE_NAME} WHERE course_id = :courseId AND is_reviewed = TRUE AND is_approved = TRUE ORDER BY sequence_number")
   fun getCourseVideosByIdFlow(courseId: String): Flow<List<CourseVideo>>

   @Upsert
   suspend fun upsertCourseVideo(courseVideo: CourseVideo)

   @Upsert
   suspend fun upsertCourseVideos(courseVideos: List<CourseVideo>)

   @Delete
   suspend fun deleteCourseVideo(courseVideo: CourseVideo)

   @Query("DELETE FROM ${CourseVideo.TABLE_NAME}")
   suspend fun deleteAllCourseVideos()

   // ===== CourseVideoProgress CRUD operations =====
   @Query("SELECT * FROM ${CourseVideoProgress.TABLE_NAME}")
   fun getAllCourseVideoProgress(): Flow<List<CourseVideoProgress>>

   @Query("SELECT * FROM ${CourseVideoProgress.TABLE_NAME} WHERE video_id = :videoId AND student_id = :studentId")
   suspend fun getCourseVideoProgress(videoId: String, studentId: String): CourseVideoProgress?

   @Query("SELECT * FROM ${CourseVideoProgress.TABLE_NAME} WHERE video_id = :videoId AND student_id = :studentId")
   fun getCourseVideoProgressFlow(videoId: String, studentId: String): Flow<CourseVideoProgress?>

   @Query("""
       SELECT cvp.* FROM ${CourseVideoProgress.TABLE_NAME} cvp 
       JOIN ${CourseVideo.TABLE_NAME} cv ON cvp.video_id = cv.id 
       WHERE cv.course_id = :courseId AND cvp.student_id = :studentId
   """)
   fun getCourseProgressFlow(courseId: String, studentId: String): Flow<List<CourseVideoProgress>>

   @Upsert
   suspend fun upsertCourseVideoProgress(progress: CourseVideoProgress)

   @Upsert
   suspend fun upsertCourseVideoProgressList(progressList: List<CourseVideoProgress>)

   @Query("DELETE FROM ${CourseVideoProgress.TABLE_NAME} WHERE video_id = :videoId AND student_id = :studentId")
   suspend fun deleteCourseVideoProgress(videoId: String, studentId: String)

   @Query("DELETE FROM ${CourseVideoProgress.TABLE_NAME}")
   suspend fun deleteAllCourseVideoProgress()

   // ===== Internship CRUD operations =====
   @Query("SELECT * FROM ${Internship.TABLE_NAME} WHERE is_approved = TRUE AND is_active = TRUE")
   fun getAllInternships(): Flow<List<Internship>>

   @Query("SELECT * FROM ${Internship.TABLE_NAME} WHERE is_approved = TRUE AND is_active = TRUE AND company_id = :companyId ORDER by title")
   fun getAllInternshipsByCompanyId(companyId : String): Flow<List<Internship>>

   @Query("SELECT * FROM ${Internship.TABLE_NAME} WHERE id = :id")
   suspend fun getInternshipById(id: String): Internship?

   @Query("SELECT * FROM ${Internship.TABLE_NAME} WHERE id = :id")
   fun getInternshipByIdFlow(id: String): Flow<Internship?>

   @Upsert
   suspend fun upsertInternship(internship: Internship)

   @Upsert
   suspend fun upsertInternships(internships: List<Internship>)

   @Delete
   suspend fun deleteInternship(internship: Internship)

   @Query("DELETE FROM ${Internship.TABLE_NAME}")
   suspend fun deleteAllInternships()

   // ===== UserAdmin CRUD operations =====
   @Query("SELECT * FROM ${UserAdmin.TABLE_NAME}")
   fun getAllUserAdmins(): Flow<List<UserAdmin>>

   @Query("SELECT * FROM ${UserAdmin.TABLE_NAME} WHERE id = :id")
   suspend fun getUserAdminById(id: String): UserAdmin?

   @Query("SELECT * FROM ${UserAdmin.TABLE_NAME} WHERE id = :id")
   fun getUserAdminByIdFlow(id: String): Flow<UserAdmin?>

   @Upsert
   suspend fun upsertUserAdmin(userAdmin: UserAdmin)

   @Upsert
   suspend fun upsertUserAdmins(userAdmins: List<UserAdmin>)

   @Delete
   suspend fun deleteUserAdmin(userAdmin: UserAdmin)

   @Query("DELETE FROM ${UserAdmin.TABLE_NAME}")
   suspend fun deleteAllUserAdmins()

   // ===== UserCompany CRUD operations =====
   @Query("SELECT * FROM ${UserCompany.TABLE_NAME}")
   fun getAllUserCompanies(): Flow<List<UserCompany>>

   @Query("SELECT * FROM ${UserCompany.TABLE_NAME} WHERE id = :id")
   suspend fun getUserCompanyById(id: String): UserCompany?

   @Query("SELECT * FROM ${UserCompany.TABLE_NAME} WHERE id = :id")
   fun getUserCompanyByIdFlow(id: String): Flow<UserCompany?>

   @Upsert
   suspend fun upsertUserCompany(userCompany: UserCompany): Long

   @Upsert
   suspend fun upsertUserCompanies(userCompanies: List<UserCompany>): List<Long>

   @Delete
   suspend fun deleteUserCompany(userCompany: UserCompany)

   @Query("DELETE FROM ${UserCompany.TABLE_NAME}")
   suspend fun deleteAllUserCompanies()

   // ===== UserAlumni CRUD operations =====
   @Query("SELECT * FROM ${UserAlumni.TABLE_NAME}")
   fun getAllUserAlumni(): Flow<List<UserAlumni>>

   @Query("SELECT * FROM ${UserAlumni.TABLE_NAME} WHERE id = :id")
   suspend fun getUserAlumniById(id: String): UserAlumni?

   @Query("SELECT * FROM ${UserAlumni.TABLE_NAME} WHERE id = :id")
   fun getUserAlumniByIdFlow(id: String): Flow<UserAlumni?>

   @Upsert
   suspend fun upsertUserAlumni(userAlumni: UserAlumni): Long

   @Upsert
   suspend fun upsertUserAlumnis(userAlumnis: List<UserAlumni>): List<Long>

   @Delete
   suspend fun deleteUserAlumni(userAlumni: UserAlumni)

   @Query("DELETE FROM ${UserAlumni.TABLE_NAME}")
   suspend fun deleteAllUserAlumni()

   // ===== UserContentCreator CRUD operations =====
   @Query("SELECT * FROM ${UserContentCreator.TABLE_NAME} WHERE is_approved = TRUE")
   fun getAllUserContentCreators(): Flow<List<UserContentCreator>>

   @Query("SELECT * FROM ${UserContentCreator.TABLE_NAME} WHERE id = :id")
   suspend fun getUserContentCreatorById(id: String): UserContentCreator?

   @Query("SELECT * FROM ${UserContentCreator.TABLE_NAME} WHERE id = :id")
   fun getUserContentCreatorByIdFlow(id: String?): Flow<UserContentCreator?>

   @Upsert
   suspend fun upsertUserContentCreator(userContentCreator: UserContentCreator): Long

   @Upsert
   suspend fun upsertUserContentCreators(userContentCreators: List<UserContentCreator>): List<Long>

   @Delete
   suspend fun deleteUserContentCreator(userContentCreator: UserContentCreator)

   @Query("DELETE FROM ${UserContentCreator.TABLE_NAME}")
   suspend fun deleteAllUserContentCreators()

   // ===== UserStudent CRUD operations =====
   @Query("SELECT * FROM ${UserStudent.TABLE_NAME}")
   fun getAllUserStudents(): Flow<List<UserStudent>>

   @Query("SELECT * FROM ${UserStudent.TABLE_NAME} WHERE id = :id")
   suspend fun getUserStudentById(id: String): UserStudent?

   @Query("SELECT * FROM ${UserStudent.TABLE_NAME} WHERE id = :id")
   fun getUserStudentByIdFlow(id: String): Flow<UserStudent?>

   @Upsert
   suspend fun upsertUserStudent(userStudent: UserStudent)

   @Upsert
   suspend fun upsertUserStudents(userStudents: List<UserStudent>): List<Long>

   @Delete
   suspend fun deleteUserStudent(userStudent: UserStudent)

   @Query("DELETE FROM ${UserStudent.TABLE_NAME}")
   suspend fun deleteAllUserStudents()

   // ===== VideoChange CRUD operations =====
   @Query("SELECT * FROM ${VideoChange.TABLE_NAME}")
   fun getAllVideoChanges(): Flow<List<VideoChange>>

   @Query("SELECT * FROM ${VideoChange.TABLE_NAME} WHERE id = :id")
   suspend fun getVideoChangeById(id: String): VideoChange?

   @Query("SELECT * FROM ${VideoChange.TABLE_NAME} WHERE id = :id")
   fun getVideoChangeByIdFlow(id: String): Flow<VideoChange?>

   @Upsert
   suspend fun upsertVideoChange(videoChange: VideoChange): Long

   @Upsert
   suspend fun upsertVideoChanges(videoChanges: List<VideoChange>): List<Long>

   @Delete
   suspend fun deleteVideoChange(videoChange: VideoChange)

   @Query("DELETE FROM ${VideoChange.TABLE_NAME}")
   suspend fun deleteAllVideoChanges()

   // ===== ChatConversation CRUD operations =====
   @Query("SELECT * FROM ${ChatConversation.TABLE_NAME}")
   fun getAllChatConversations(): Flow<List<ChatConversation>>

   @Query("SELECT * FROM ${ChatConversation.TABLE_NAME} WHERE id = :id")
   suspend fun getChatConversationById(id: String): ChatConversation?

   @Query("SELECT * FROM ${ChatConversation.TABLE_NAME} WHERE id = :id")
   fun getChatConversationByIdFlow(id: String): Flow<ChatConversation?>

   @Upsert
   suspend fun upsertChatConversation(chatConversation: ChatConversation)

   @Upsert
   suspend fun upsertChatConversations(chatConversations: List<ChatConversation>)

   @Delete
   suspend fun deleteChatConversation(chatConversation: ChatConversation)

   @Query("DELETE FROM ${ChatConversation.TABLE_NAME}")
   suspend fun deleteAllChatConversations()

   // ===== ChatParticipant CRUD operations =====
   @Query("SELECT * FROM ${ChatParticipant.TABLE_NAME}")
   fun getAllChatParticipants(): Flow<List<ChatParticipant>>

   @Query("SELECT * FROM ${ChatParticipant.TABLE_NAME} WHERE id = :id")
   suspend fun getChatParticipantById(id: String): ChatParticipant?

   @Query("SELECT * FROM ${ChatParticipant.TABLE_NAME} WHERE id = :id")
   fun getChatParticipantByIdFlow(id: String): Flow<ChatParticipant?>

   @Query("SELECT * FROM ${ChatParticipant.TABLE_NAME} WHERE conversation_id = :conversationId")
   fun getChatParticipantsByConversationIdFlow(conversationId: String): Flow<List<ChatParticipant>>

   @Upsert
   suspend fun upsertChatParticipant(chatParticipant: ChatParticipant)

   @Upsert
   suspend fun upsertChatParticipants(chatParticipants: List<ChatParticipant>)

   @Delete
   suspend fun deleteChatParticipant(chatParticipant: ChatParticipant)

   @Query("DELETE FROM ${ChatParticipant.TABLE_NAME}")
   suspend fun deleteAllChatParticipants()

   // ===== ChatMessage CRUD operations =====
   @Query("SELECT * FROM ${ChatMessage.TABLE_NAME}")
   fun getAllChatMessages(): Flow<List<ChatMessage>>

   @Query("SELECT * FROM ${ChatMessage.TABLE_NAME} WHERE id = :id")
   suspend fun getChatMessageById(id: String): ChatMessage?

   @Query("SELECT * FROM ${ChatMessage.TABLE_NAME} WHERE id = :id")
   fun getChatMessageByIdFlow(id: String): Flow<ChatMessage?>

   @Query("SELECT * FROM ${ChatMessage.TABLE_NAME} WHERE conversation_id = :conversationId ORDER BY created_at")
   fun getChatMessagesByConversationIdFlow(conversationId: String): Flow<List<ChatMessage>>

   @Query("SELECT * FROM ${ChatMessage.TABLE_NAME} WHERE is_sync_with_server = FALSE")
   fun getUnsyncedChatMessages() : Flow<List<ChatMessage>>

   @Upsert
   suspend fun upsertChatMessage(chatMessage: ChatMessage)

   @Upsert
   suspend fun upsertChatMessages(chatMessages: List<ChatMessage>)

   @Delete
   suspend fun deleteChatMessage(chatMessage: ChatMessage)

   @Query("DELETE FROM ${ChatMessage.TABLE_NAME}")
   suspend fun deleteAllChatMessages()

   // ===== LiveSession CRUD operations =====
   @Query("SELECT * FROM ${LiveSession.TABLE_NAME}")
   fun getAllLiveSessions(): Flow<List<LiveSession>>

   @Query("SELECT * FROM ${LiveSession.TABLE_NAME} WHERE id = :id")
   suspend fun getLiveSessionById(id: String): LiveSession?

   @Query("SELECT * FROM ${LiveSession.TABLE_NAME} WHERE id = :id")
   fun getLiveSessionByIdFlow(id: String): Flow<LiveSession?>

   @Query("SELECT * FROM ${LiveSession.TABLE_NAME} WHERE course_id = :courseId")
   fun getLiveSessionsByCourseIdFlow(courseId: String): Flow<List<LiveSession>>

   @Upsert
   suspend fun upsertLiveSession(liveSession: LiveSession)

   @Upsert
   suspend fun upsertLiveSessions(liveSessions: List<LiveSession>)

   @Delete
   suspend fun deleteLiveSession(liveSession: LiveSession)

   @Query("DELETE FROM ${LiveSession.TABLE_NAME}")
   suspend fun deleteAllLiveSessions()

   // ===== GiftCard CRUD operations =====
   @Query("SELECT * FROM ${GiftCard.TABLE_NAME}")
   fun getAllGiftCards(): Flow<List<GiftCard>>

   @Query("SELECT * FROM ${GiftCard.TABLE_NAME} WHERE code = :code")
   suspend fun getGiftCardByCode(code: String): GiftCard?

   @Upsert
   suspend fun upsertGiftCard(giftCard: GiftCard)

   @Upsert
   suspend fun upsertGiftCards(giftCards: List<GiftCard>)

   @Delete
   suspend fun deleteGiftCard(giftCard: GiftCard)

   @Query("DELETE FROM ${GiftCard.TABLE_NAME}")
   suspend fun deleteAllGiftCards()

   // ===== SettingsSupabaseTable CRUD operations =====
   @Query("SELECT * FROM ${SettingsSupabaseTable.TABLE_NAME}")
   fun getAllSettings(): Flow<List<SettingsSupabaseTable>>

   @Query("SELECT * FROM ${SettingsSupabaseTable.TABLE_NAME} WHERE `key` = :key")
   suspend fun getSettingByKey(key: String): SettingsSupabaseTable?

   @Query("SELECT * FROM ${SettingsSupabaseTable.TABLE_NAME} WHERE `key` = :key")
   fun getSettingByKeyFlow(key: String): Flow<SettingsSupabaseTable?>

   @Upsert
   suspend fun upsertSetting(setting: SettingsSupabaseTable)

   @Upsert
   suspend fun upsertSettings(settings: List<SettingsSupabaseTable>)

   @Delete
   suspend fun deleteSetting(setting: SettingsSupabaseTable)

   @Query("DELETE FROM ${SettingsSupabaseTable.TABLE_NAME}")
   suspend fun deleteAllSettings()

   // ===== CourseReview CRUD operations =====
   @Query("SELECT * FROM ${CourseReview.TABLE_NAME}")
   fun getAllCourseReviews(): Flow<List<CourseReview>>

   @Query("SELECT * FROM ${CourseReview.TABLE_NAME} WHERE id = :id")
   suspend fun getCourseReviewById(id: String): CourseReview?

   @Query("SELECT * FROM ${CourseReview.TABLE_NAME} WHERE id = :id")
   fun getCourseReviewByIdFlow(id: String): Flow<CourseReview?>

   @Query("SELECT * FROM ${CourseReview.TABLE_NAME} WHERE course_id = :courseId")
   fun getCourseReviewsByCourseIdFlow(courseId: String): Flow<List<CourseReview>>

   @Query("SELECT * FROM ${CourseReview.TABLE_NAME} WHERE student_id = :studentId")
   fun getCourseReviewsByStudentIdFlow(studentId: String): Flow<List<CourseReview>>

   @Upsert
   suspend fun upsertCourseReview(courseReview: CourseReview)

   @Upsert
   suspend fun upsertCourseReviews(courseReviews: List<CourseReview>)

   @Delete
   suspend fun deleteCourseReview(courseReview: CourseReview)

   @Query("DELETE FROM ${CourseReview.TABLE_NAME}")
   suspend fun deleteAllCourseReviews()

   /**
    * Combined method to upsert multiple entity types at once.
    * Data is inserted in order of foreign key dependencies.
    * Only performs database operations for non-empty lists.
    */
   suspend fun upsertAll(
      courses: List<Course> = emptyList(),
      courseCategories: List<CourseCategory> = emptyList(),
      courseChanges: List<CourseChanges> = emptyList(),
      courseVideos: List<CourseVideo> = emptyList(),
      courseVideoProgress: List<CourseVideoProgress> = emptyList(),
      internships: List<Internship> = emptyList(),
      userAdmins: List<UserAdmin> = emptyList(),
      userAlumni: List<UserAlumni> = emptyList(),
      userCompanies: List<UserCompany> = emptyList(),
      userContentCreators: List<UserContentCreator> = emptyList(),
      userStudents: List<UserStudent> = emptyList(),
      videoChanges: List<VideoChange> = emptyList(),
      chatConversations: List<ChatConversation> = emptyList(),
      chatParticipants: List<ChatParticipant> = emptyList(),
      chatMessages: List<ChatMessage> = emptyList(),
      liveSessions: List<LiveSession> = emptyList(),
      giftCards: List<GiftCard> = emptyList(),
      settings: List<SettingsSupabaseTable> = emptyList(),
        courseReviews: List<CourseReview> = emptyList()
   ) {
      // First: User tables that don't depend on other entities
      if (userAdmins.isNotEmpty()) upsertUserAdmins(userAdmins)
      if (userAlumni.isNotEmpty()) upsertUserAlumnis(userAlumni)
      if (userStudents.isNotEmpty()) upsertUserStudents(userStudents)
      if (userCompanies.isNotEmpty()) upsertUserCompanies(userCompanies)
      if (userContentCreators.isNotEmpty()) upsertUserContentCreators(userContentCreators)

      // Second: Independent entities
      if (courseCategories.isNotEmpty()) upsertCourseCategories(courseCategories)
      if (chatConversations.isNotEmpty()) upsertChatConversations(chatConversations)
      if (settings.isNotEmpty()) upsertSettings(settings)

      // Third: Entities depending on basic users
      if (giftCards.isNotEmpty()) upsertGiftCards(giftCards)
      if (courses.isNotEmpty()) upsertCourses(courses)
      if (internships.isNotEmpty()) upsertInternships(internships)
      if (chatParticipants.isNotEmpty()) upsertChatParticipants(chatParticipants)

      // Fourth: Entities depending on previous entities
      if (courseVideos.isNotEmpty()) upsertCourseVideos(courseVideos)
      if (chatMessages.isNotEmpty()) upsertChatMessages(chatMessages)
      if (liveSessions.isNotEmpty()) upsertLiveSessions(liveSessions)

      // Fifth: Changes which depend on courses or videos
      if (courseChanges.isNotEmpty()) upsertCourseChanges(courseChanges)
      if (videoChanges.isNotEmpty()) upsertVideoChanges(videoChanges)

      // Finally: Progress data which depends on videos and students
      if (courseVideoProgress.isNotEmpty()) upsertCourseVideoProgressList(courseVideoProgress)
        if (courseReviews.isNotEmpty()) upsertCourseReviews(courseReviews)
   }

   @Query("DELETE FROM ${UserStudent.TABLE_NAME} WHERE id = :userId")
   suspend fun deleteProfile(userId: String)

   /**
    * Clears all data from all tables in the database.
    * Tables are cleared in reverse dependency order to respect foreign key constraints.
    */
   suspend fun clearAllData() {
      // First: Clear tables that depend on other tables
      deleteAllCourseVideoProgress()
      deleteAllVideoChanges()
      deleteAllCourseChanges()
      deleteAllCourseReviews() // Add this line


      // Second: Clear tables with dependencies on users or courses
      deleteAllLiveSessions()
      deleteAllChatMessages()
      deleteAllCourseVideos()
      deleteAllChatParticipants()
      deleteAllInternships()
      deleteAllCourses()
      deleteAllGiftCards()

      // Third: Clear independent entity tables
      deleteAllSettings()
      deleteAllChatConversations()
      deleteAllCourseCategories()

      // Finally: Clear user tables
      deleteAllUserContentCreators()
      deleteAllUserCompanies()
      deleteAllUserStudents()
      deleteAllUserAlumni()
      deleteAllUserAdmins()
   }
}