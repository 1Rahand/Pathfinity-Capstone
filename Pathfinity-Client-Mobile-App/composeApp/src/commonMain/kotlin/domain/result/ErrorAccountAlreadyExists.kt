package domain.result

import domain.Lang
import presentation.StringResources

data object ErrorAccountAlreadyExists : Error {
   override fun getDescription(lang: Lang) = StringResources.thisEmailHasBeenUsedOnAnotherAccount(lang)
}