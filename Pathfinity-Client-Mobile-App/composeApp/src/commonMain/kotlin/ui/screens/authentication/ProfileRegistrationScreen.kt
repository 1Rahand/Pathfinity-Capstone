//package ui.screens.authentication
//
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import data.RepoAuth
//import domain.Gender
//import kotlinx.coroutines.launch
//import org.jetbrains.compose.resources.painterResource
//import pathfinity.composeapp.generated.resources.Res
//import pathfinity.composeapp.generated.resources.female
//import pathfinity.composeapp.generated.resources.male
//import presentation.StringResources
//import ui.components.BackButton
//import ui.components.MyButton
//import ui.components.RowC
//import ui.components.environment.MyLocalSheetManager
//import ui.components.environment.currentAppLang
//import ui.components.noIndicationClickable
//import ui.theme.green
//import ui.theme.onGreen
//
//data class Dot(val isCompleted: Boolean)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileRegistrationScreen(
//   navController: NavController,
//   repoAuth: RepoAuth,
//   isInEdit: Boolean = false
//) {
//
//   Scaffold(
//      modifier = Modifier.fillMaxSize(),
//      topBar = {
//         if(isInEdit){
//            TopAppBar(title = {
//               Text(
//                  text = StringResources.editProfile(currentAppLang()),
//                  style = MaterialTheme.typography.titleSmall,
//               )
//            } , navigationIcon = { BackButton { navController.navigateUp() } })
//         }
//      }
//   ) {
//      val scope = rememberCoroutineScope()
//      val focusManager = LocalFocusManager.current
//      Column(
//         Modifier
//            .fillMaxSize()
//            .padding(it)
//            .padding(vertical = 16.dp)
//      ) {
//         val pagerState = rememberPagerState(0) { 3 }
//         val dots = listOf(
//            Dot(isCompleted = state.name != "" && state.gender != null),
//            Dot(isCompleted = state.governorate != null),
//            Dot(isCompleted = state.school != null || state.schoolText != "")
//         )
//         OnboardingDotsIndicator(
//            totalDots = dots,
//            selectedIndex = pagerState.currentPage,
//            modifier = Modifier.padding(16.dp),
//         )
//         HorizontalPager(pagerState, modifier = Modifier.weight(1f)) {
//            if (it == 0) {
//               Column(
//                  Modifier
//                     .noIndicationClickable { focusManager.clearFocus() }
//                     .fillMaxSize()
//                     .padding(24.dp),
//                  verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
//               ) {
//
//                  Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
//                     Text(
//                        text = StringResources.tellUsYourInfo(currentAppLang()),
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold
//                     )
//
//                     Text(
//                        text = StringResources.soWeCanAddressYouProperly(currentAppLang()),
//                        style = MaterialTheme.typography.labelMedium,
//                        fontWeight = FontWeight.Light,
//                        modifier = Modifier.alpha(0.50F)
//                     )
//                  }
//
//
//
//
//                  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                     TitleText(StringResources.name(currentAppLang()))
//
//                     MyOutlineTextField(
//                        modifier = Modifier,
//                        value = state.name,
//                        onValueChange = { onAction(ProfileRegistrationScreenEvent.NameChanged(it)) },
//                        placeholder = StringResources.name(currentAppLang())
//                     )
//                  }
//
//                  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                     TitleText(StringResources.gender(currentAppLang()))
//                     RowC {
//                        Gender.values().forEach { gender ->
//                           GenderIcon(
//                              modifier = Modifier.weight(1f),
//                              gender = gender,
//                              isActive = state.gender == gender,
//                              onClick = { onAction(ProfileRegistrationScreenEvent.GenderChanged(gender)) }
//                           )
//                        }
//                     }
//                  }
//               }
//            }
//            if (it == 1) {
//               Column(
//                  Modifier
//                     .noIndicationClickable { focusManager.clearFocus() }
//                     .fillMaxSize()
//                     .padding(24.dp),
//                  verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
//               ) {
//
//                  Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
//                     Text(
//                        text = StringResources.fromWhichCity(currentAppLang()),
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold
//                     )
//
//                     Text(
//                        text = StringResources.pleaseTellUsYourGovernorate(currentAppLang()),
//                        style = MaterialTheme.typography.labelMedium,
//                        fontWeight = FontWeight.Light,
//                        modifier = Modifier.alpha(0.50F)
//                     )
//                  }
//
//
//                  KurdistanMap(selectedGovernorate = state.governorate, modifier = Modifier.weight(1f)) {
//                     onAction(ProfileRegistrationScreenEvent.GovernorateChanged(it))
//                  }
//
//               }
//            }
//
//            if (it == 2) {
//               Column(
//                  Modifier
//                     .noIndicationClickable { focusManager.clearFocus() }
//                     .fillMaxSize()
//                     .padding(24.dp),
//                  verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
//               ) {
//
//
//                  Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
//                     Text(
//                        text = StringResources.fromWhichSchool(currentAppLang()),
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold
//                     )
//
//                     Text(
//                        text = StringResources.pleaseChooseYourSchool(currentAppLang()),
//                        style = MaterialTheme.typography.labelMedium,
//                        fontWeight = FontWeight.Light,
//                        modifier = Modifier.alpha(0.50F)
//                     )
//                  }
//
//                  Column(
//                     modifier = Modifier.weight(1f),
//                  ) {
//                     val sheetManager = MyLocalSheetManager.current
//                     var isAreaFilterShowing by remember { mutableStateOf(false) }
//                     if (isAreaFilterShowing){
//                        AreaFilterModalSheetContent(
//                           generalAdministrations = state.generalAdministrations ?: emptyList(),
//                           chosenGeneralAdministration = state.filteredGeneralAdministration,
//                           chosenAdministration = state.filteredAdministration,
//                           onAction = onAction,
//                           onDismissRequest = { isAreaFilterShowing = false }
//                        )
//                     }
//                     Header(
//                        state = state,
//                        onAction = onAction,
//                        filterButtonClick = {
//                           isAreaFilterShowing = true
//                        }
//                     )
//
//                     Body(
//                        modifier = Modifier.weight(1f),
//                        filteredSchools = state.filteredSchools ?: emptyList(),
//                        selectedSchool = state.school,
//                        alternativeSchoolText = state.schoolText,
//                        onSchoolChanged = { onAction(ProfileRegistrationScreenEvent.SchoolChanged(it)) },
//                        onSchoolTextChanged = { onAction(ProfileRegistrationScreenEvent.SchoolTextChanged(it)) }
//                     )
//
//                  }
//               }
//            }
//         }
//
//
//         val isOnLastPage = pagerState.currentPage == pagerState.pageCount - 1
//         MyButton(
//            text = if (isOnLastPage) StringResources.finish(currentAppLang()) else StringResources.next(currentAppLang()),
//            onClick = {
//               if (isOnLastPage){
//                  onAction(ProfileRegistrationScreenEvent.OnDoneClicked)
//               }else {
//                  scope.launch {
//                     pagerState.animateScrollToPage(pagerState.currentPage + 1, animationSpec = tween(700))
//                  }
//               }
//            },
//            modifier = Modifier
//               .align(Alignment.CenterHorizontally)
//               .fillMaxWidth()
//               .padding(16.dp),
//            trailingIcon = if (state.isLoading) {
//               {
//                  CircularProgressIndicator(Modifier.size(24.dp) , color = if (isOnLastPage && dots.all { it.isCompleted }) MaterialTheme.colorScheme.onGreen else MaterialTheme.colorScheme.onPrimary)
//               }
//            } else null,
//            containerColor = if (isOnLastPage && dots.all { it.isCompleted }) MaterialTheme.colorScheme.green else MaterialTheme.colorScheme.primary,
//            contentColor = if (isOnLastPage && dots.all { it.isCompleted }) MaterialTheme.colorScheme.onGreen else MaterialTheme.colorScheme.onPrimary,
//            enabled = (dots.all { it.isCompleted } && isOnLastPage) || !isOnLastPage
//         )
//
//      }
//   }
//}
//
//
//@Composable
//private fun GenderIcon(modifier: Modifier = Modifier, gender: Gender, isActive: Boolean, onClick: () -> Unit) {
//   val icon = if (gender == Gender.Male) Res.drawable.male else Res.drawable.female
//   val backgroundColor by animateColorAsState(if (isActive) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background)
//   val borderColor by animateColorAsState(
//      if (isActive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground.copy(
//         alpha = 0.2f
//      )
//   )
//   val borderWidth by animateDpAsState(if (isActive) 2.dp else 1.dp)
//
//   Image(
//      painter = painterResource(icon),
//      contentDescription = null,
//      modifier = modifier
//         .heightIn(max = 100.dp)
//         .fillMaxWidth()
//         .background(backgroundColor, shape = RoundedCornerShape(8.dp))
//         .clip(RoundedCornerShape(8.dp))
//         .border(borderWidth, borderColor, shape = RoundedCornerShape(8.dp))
//         .clickable { onClick() }
//         .padding(top = 8.dp)
//   )
//}
//
//@Composable
//private fun TitleText(text: String) {
//   Text(
//      text = text,
//      style = MaterialTheme.typography.titleMedium,
//      fontWeight = FontWeight.Bold,
//      color = MaterialTheme.colorScheme.onBackground,
//      modifier = Modifier
//   )
//}