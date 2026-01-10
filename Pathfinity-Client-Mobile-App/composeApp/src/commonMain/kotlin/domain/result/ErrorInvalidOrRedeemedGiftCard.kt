package domain.result

import domain.Lang
import presentation.StringResources

data object ErrorInvalidOrRedeemedGiftCard : Error {
   override fun getDescription(lang: Lang): String {
      return StringResources.invalidOrAlreadyRedeemedGiftCard(lang)
   }
}