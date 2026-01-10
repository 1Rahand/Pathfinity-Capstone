package ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.RepoSetting
import domain.Lang
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.onboarding_1
import pathfinity.composeapp.generated.resources.onboarding_2
import pathfinity.composeapp.generated.resources.onboarding_3
import presentation.StringResources
import ui.components.buttons.LangButton
import ui.components.buttons.MyButton
import ui.components.environment.MyLocalSettings
import ui.components.environment.currentAppLang
import ui.components.layout.LtrLayout
import ui.components.layout.RowC
import ui.components.layout.RtlLayout
import ui.components.textDirection
import ui.utility.getFlag

@Composable
fun OnBoardingScreen(
   modifier: Modifier = Modifier,
   onComplete: () -> Unit
) {
   Scaffold(Modifier.fillMaxSize()) {

      val pages = 3
      val pagerState = rememberPagerState(
         initialPage = 0,
         pageCount = { pages }
      )
      val scope = rememberCoroutineScope()

      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(it),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
         OnBoardingScreenContainer(
            modifier = Modifier
               .fillMaxWidth()
               .weight(1f),
            pagerState = pagerState,
         )
         PagerIndicator(pageCount = pages, activePage = pagerState.currentPage)

         MyButton(
            text = StringResources.next(currentAppLang()),
            onClick = {
               if (pagerState.currentPage == pages - 1) {
                  onComplete()
               } else {
                  scope.launch {
                     pagerState.animateScrollToPage(
                        page = pagerState.currentPage + 1,
                        animationSpec = tween(700)
                     )
                  }
               }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth(0.75F).padding(bottom = 16.dp)
         )

      }


   }
}

@Composable
fun PagerIndicator(pageCount: Int, activePage: Int) {
   val modded = activePage % pageCount
   RowC(
      spacing = 8.dp
   ) {
      repeat(pageCount) {
         val isActive = it == modded
         val alphaAnimated by animateFloatAsState(
            targetValue = if (isActive) 1.0F else 0.20F,
            animationSpec = tween(300)
         )
         val widthAnimated by animateFloatAsState(
            targetValue = if (isActive) 24f else 8f,
            animationSpec = tween(300)
         )
         val colorAnimated by animateColorAsState(
            targetValue = if (isActive)
               MaterialTheme.colorScheme.primary
            else
               MaterialTheme.colorScheme.onSurface,
            animationSpec = tween(300)
         )

         Box(
            modifier = Modifier
               .alpha(alphaAnimated)
               .clip(CircleShape)
               .height(8.dp)
               .width(widthAnimated.dp)
               .background(colorAnimated)
         )
      }
   }
}

@Composable
fun OnBoardingScreenContainer(
   modifier: Modifier = Modifier,
   pagerState: PagerState
) {
   HorizontalPager(modifier = modifier, state = pagerState) { page ->
      when (page) {
         0 -> OnBoardingScreen1(Modifier.padding(16.dp))
         1 -> OnBoardingScreen2(Modifier.padding(16.dp))
         2 -> OnBoardingScreen3(Modifier.padding(16.dp))
      }
   }
}

@Composable
fun OnBoardingScreen1(
   modifier: Modifier = Modifier
) {
   val repoSetting: RepoSetting = koinInject()
   val setting = MyLocalSettings.current
   val scope = rememberCoroutineScope()
   Column(
      verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)
   ) {
      Image(
         painter = painterResource(resource = Res.drawable.onboarding_1),
         contentDescription = null,
         modifier = Modifier
            .widthIn(max = 200.dp),
         contentScale = ContentScale.FillWidth
      )


      RtlLayout {
         Text(
            text = buildAnnotatedString {
               withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                  append(StringResources.welcomeTo(currentAppLang()))
                  append(" ")
               }
               withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                  append(StringResources.pathfinity(currentAppLang()))
               }
            },
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
         )
      }

      SubtitleText(
         text = StringResources.pathfinityWelcomeMessageSubtitle(currentAppLang()),
         modifier = Modifier.widthIn(max = 600.dp)
      )

      RowC {
         Lang.values().forEach { lang ->
            LangButton(
               isActive = lang == currentAppLang(),
               onClick = {
                  scope.launch {
                     repoSetting.setLang(lang)
                  }
               },
               flag = lang.getFlag(),
               text = lang.name,
            )

         }

      }
   }
}

@Composable
fun OnBoardingScreen2(
   modifier: Modifier = Modifier,
) {

   Column(
      verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.fillMaxSize()
   ) {
      Image(
         painter = painterResource(resource = Res.drawable.onboarding_2),
         contentDescription = null,
         modifier = Modifier
            .widthIn(max = 200.dp),
         contentScale = ContentScale.FillWidth
      )

      Text(
         text = StringResources.mentorshipGuidance(currentAppLang()),
         fontWeight = FontWeight.Bold,
         style = MaterialTheme.typography.headlineSmall
      )

      SubtitleText(
         text = StringResources.mentorshipGuidanceDescription(currentAppLang()),
         modifier = Modifier.fillMaxWidth(0.85F)
      )
   }

}

@Composable
fun OnBoardingScreen3(
   modifier: Modifier = Modifier
) {
   Column(
      verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.fillMaxSize()
   ) {
      Image(
         painter = painterResource(resource = Res.drawable.onboarding_3),
         contentDescription = null,
         modifier = Modifier
            .widthIn(max = 200.dp),
         contentScale = ContentScale.FillWidth
      )

      Text(
         text = StringResources.internshipPrograms(currentAppLang()),
         fontWeight = FontWeight.Bold,
         style = MaterialTheme.typography.headlineSmall
      )

      SubtitleText(
         text = StringResources.internshipProgramsDescription(currentAppLang()),
         modifier = Modifier.fillMaxWidth(0.85F)
      )
   }

}

@Composable
fun SubtitleText(text: String, modifier: Modifier = Modifier) {
   Text(
      text = text,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = modifier.alpha(0.75F),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodyMedium.copy(textDirection = currentAppLang().textDirection()),
      lineHeight = 32.sp,
      fontWeight = FontWeight.Light
   )
}