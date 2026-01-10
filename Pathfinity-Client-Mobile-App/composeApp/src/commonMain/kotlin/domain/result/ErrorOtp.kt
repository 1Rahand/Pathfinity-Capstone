package domain.result

import domain.Lang
import presentation.StringResources

data object ErrorOtp : Error {
   override fun getDescription(lang: Lang) = StringResources.otpCodeNotCorrect(lang)
}