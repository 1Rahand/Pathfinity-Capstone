package domain

import kotlinx.serialization.Serializable

@Serializable
enum class Appearance {
   Light, Dark, SystemDefault
}