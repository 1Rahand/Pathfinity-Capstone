package ui.utility

import androidx.compose.runtime.Composable
import domain.Lang
import org.jetbrains.compose.resources.DrawableResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.flag_kurdistan
import pathfinity.composeapp.generated.resources.flag_uk

@Composable
fun Lang.getFlag(): DrawableResource {
    return when (this) {
        Lang.Eng -> Res.drawable.flag_uk
        Lang.Krd -> Res.drawable.flag_kurdistan
    }
}