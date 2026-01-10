package di

import RootViewModel
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import data.RepoAuth
import data.RepoAuthImpl
import data.RepoSetting
import data.RepoSettingImpl
import data.Syncer
import data.MessageSyncer
import database.MyDao
import database.MyDatabase
import database.TimeDao
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.FileManager
import platform.getDatastore
import platform.getHttpClient
import platform.getMyDatabaseBuilder
import platform.getSqliteDriver
import kotlin.time.Duration.Companion.minutes


// It crashed on Android if I didn't use this object
object DatastoreHolder {
   val datastore = getDatastore()
}

fun commonModule() = module {
   single<MyDatabase> {
      getMyDatabaseBuilder()
         .fallbackToDestructiveMigration(true)
         .setDriver(getSqliteDriver())
         .setQueryCoroutineContext(Dispatchers.IO)
         .build()
   }

   single<MyDao> { get<MyDatabase>().myDao() }
   single<TimeDao> { get<MyDatabase>().timeDao() }

   single<DataStore<Preferences>> { DatastoreHolder.datastore }

   singleOf(::Syncer)
   singleOf(::MessageSyncer)
   singleOf(::RepoSettingImpl) bind RepoSetting::class
   singleOf(::RepoAuthImpl) bind RepoAuth::class

   single<FileManager> {
      FileManager
   }



   single<SupabaseClient> {
      createSupabase()
   }

   single<HttpClient> {
      getHttpClient()
   }


   viewModelOf(::RootViewModel)
}

fun createSupabase(): SupabaseClient {
   return createSupabaseClient(
      supabaseUrl = "https://njqaahqfgymkwxkyqifq.supabase.co",
      supabaseKey = "sb_publishable_6ApSIHMTMqgOs8JSMID4zA_9a8MFuEt"
   ) {
      install(Auth)
      install(Postgrest)
      install(Realtime)
      install(Storage)
      defaultLogLevel = LogLevel.DEBUG
      defaultSerializer = KotlinXSerializer(Json {
         ignoreUnknownKeys = true
         encodeDefaults = true
      })

//      Uncomment this if you want to use the proxy
      /*      this.httpConfig {
               this.defaultRequest {
                  val credentials = Base64.encode("squid_user:Enos2003".toByteArray())
                  header(HttpHeaders.ProxyAuthorization, "Basic $credentials")
               }
            }
            this.httpEngine = getHttpClient().engine*/


      requestTimeout = 1.minutes
      //install other modules
   }
}

