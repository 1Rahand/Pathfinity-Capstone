package domain.result

import domain.Lang
import presentation.StringResources

data object ErrorIncorrectEmailPass : RootError {
   override fun getDescription(lang: Lang): String {
      return StringResources.incorrectEmailOrPassword(lang)
   }
}