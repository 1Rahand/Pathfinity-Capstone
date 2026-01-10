import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.*

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    singleWindowApplication (
        alwaysOnTop = true,
        title = "Pathfinity"
    ) {
        App()
    }
}