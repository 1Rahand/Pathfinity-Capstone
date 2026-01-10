package ui.utility

import domain.Gender
import org.jetbrains.compose.resources.DrawableResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.female
import pathfinity.composeapp.generated.resources.male
import pathfinity.composeapp.generated.resources.unknown_gender_icon

fun Gender.getIcon() : DrawableResource {
   return when (this) {
      Gender.Male -> Res.drawable.male
      Gender.Female -> Res.drawable.female
      Gender.Unknown -> Res.drawable.unknown_gender_icon
   }
}