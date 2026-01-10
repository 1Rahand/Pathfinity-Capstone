package database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
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

@Database(
   entities = [
      Course::class,
      CourseCategory::class,
      Internship::class,
      UserAdmin::class,
      UserCompany::class,
      UserAlumni::class,
      UserContentCreator::class,
      UserStudent::class,
      CourseChanges::class,
      VideoChange::class,
      CourseVideo::class,
      ChatConversation::class,
      ChatParticipant::class,
      ChatMessage::class,
      LiveSession::class,
      GiftCard::class,
      SettingsSupabaseTable::class,
      CourseVideoProgress::class,
      CourseReview::class
   ],
   version = 20
)
@TypeConverters(InstantConverter::class, StringListConvertor::class, LocalDateConvertor::class)
@ConstructedBy(MyDatabaseConstructor::class)
abstract class MyDatabase : RoomDatabase() {
   abstract fun myDao(): MyDao
   abstract fun timeDao(): TimeDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MyDatabaseConstructor : RoomDatabaseConstructor<MyDatabase>

