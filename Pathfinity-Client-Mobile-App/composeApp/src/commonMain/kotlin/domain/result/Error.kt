package domain.result

import domain.Lang

sealed interface Error {
   fun getDescription(lang : Lang): String
}