package domain.result

import domain.Lang
import presentation.StringResources

enum class ErrorSupabaseCancellation : RootError {
   Cancelled {
      override fun getDescription(lang: Lang) = StringResources.unknownErrorPleaseTryAgain(lang)
   },
   Rest {
      override fun getDescription(lang: Lang) = StringResources.internetErrorPleaseTryAgain(lang)
   },
   Network {
      override fun getDescription(lang: Lang) = StringResources.internetErrorPleaseTryAgain(lang)
   },
   Unknown{
      override fun getDescription(lang: Lang) = StringResources.unknownErrorPleaseTryAgain(lang)
   }
}