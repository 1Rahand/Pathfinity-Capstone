package platform

import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.engine.http
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

actual fun getHttpClient(): HttpClient {
   return runBlocking {
      withContext(Dispatchers.IO) {
         HttpClient {
            install(ContentNegotiation) {
               json(Json {
                  prettyPrint = true
                  isLenient = true
                  ignoreUnknownKeys = true
               })
            }
         }
      }
   }
}