package ui.screens.course

import VideoPlayer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import database.MyDao
import presentation.StringResources
import ui.components.MyTopAppBar

@Composable
fun CourseVideoScreen(
   navController: NavController,
   dao : MyDao,
   courseVideoId : String
){
   val video by dao.getCourseVideoByIdFlow(courseVideoId).collectAsStateWithLifecycle(null)

   Scaffold(
      modifier = Modifier
         .fillMaxSize(),
      topBar = {
         MyTopAppBar(
            title = "",
            onBackClick = {navController.navigateUp()}
         )
      }
   ) { paddingValues ->
      video?.let {
         VideoPlayer(
            modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues),
            url = it.videoUrl,
            autoPlay = true,
            showControls = true
         )
      }
   }
}