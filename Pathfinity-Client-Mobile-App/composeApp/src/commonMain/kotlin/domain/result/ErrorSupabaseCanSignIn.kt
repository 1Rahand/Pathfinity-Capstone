package domain.result

import domain.Lang
import presentation.StringResources

sealed class ErrorSupabaseCanSignIn : Error {
   data class NotAllowed(val deviceOs: String, val deviceName: String) : ErrorSupabaseCanSignIn() {
      override fun getDescription(lang: Lang) = StringResources.signedInOnAnotherDevice(lang)
   }

   data object Rest : ErrorSupabaseCanSignIn() {
      override fun getDescription(lang: Lang) = StringResources.internetErrorPleaseTryAgain(lang)
   }

   data object Network : ErrorSupabaseCanSignIn() {
      override fun getDescription(lang: Lang) = StringResources.internetErrorPleaseTryAgain(lang)
   }
}