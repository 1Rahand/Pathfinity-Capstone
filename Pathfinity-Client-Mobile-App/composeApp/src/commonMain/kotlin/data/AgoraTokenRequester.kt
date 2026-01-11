package data

import domain.result.ErrorSupabaseCancellation
import io.github.jan.supabase.exceptions.HttpRequestException
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TokenRequest(
   val channelName: String,
   val uid: Int = 0,
   val role: Int = 2,
   val expirationTimeInSeconds: Int = 3600
)

@Serializable
data class TokenResponse(
   val token: String
)

object AgoraTokenRequester {
   suspend fun getAgoraToken(channelName: String, httpClient: HttpClient) = wrapResultRepo {
      // Create the request payload
      val tokenRequest = TokenRequest(
         channelName = channelName,
         uid = 0,
         role = 2,
         expirationTimeInSeconds = 3600
      )


      // Make the HTTP request to the token generation service
      val response = httpClient.post("https://pathfinity-capstone-production.up.railway.app/generate-subscriber-token") {
         contentType(ContentType.Application.Json)
         setBody(tokenRequest)
      }

      // Parse the response
      if (response.status.isSuccess()) {
         val responseBody = response.bodyAsText()
         val tokenResponse = Json.decodeFromString<TokenResponse>(responseBody)
         tokenResponse.token
      } else {
         throw Exception("Failed to get Agora token: ${response.status}")
      }
   }
}
