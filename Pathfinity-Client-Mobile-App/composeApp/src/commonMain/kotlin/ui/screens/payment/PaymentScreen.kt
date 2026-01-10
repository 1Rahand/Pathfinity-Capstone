package ui.screens.payment

import CodeActivationRoute
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import domain.Lang
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.fastpay
import pathfinity.composeapp.generated.resources.fib
import pathfinity.composeapp.generated.resources.voucher
import presentation.StringResources
import ui.components.MyPreview
import ui.components.buttons.BackButton
import ui.components.direction
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.RowC
import ui.screens.GradientBackgroundText
import ui.theme.green
import ui.utility.getTextDir

val gradientColors = listOf(
   Color(0xFFFF3F3F),
   Color(0xFF063CFF)
)

val gradientBrush = Brush.linearGradient(
   colors = gradientColors
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController) {
   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         TopAppBar(
            title = {
               Text(
                  text = StringResources.upgrade(currentAppLang()),
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
               )
            },
            navigationIcon = { BackButton { navController.navigateUp() } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
         )
      }
   ) {
      Column(
         modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(PaddingValues(16.dp)),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.spacedBy(24.dp)
      ) {
         Text(
            text = StringResources.chooseYourPlan(currentAppLang()),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Start)
         )

         var selectedOffer by remember { mutableIntStateOf(1) }

         // Monthly subscription option
         PriceCard(
            title = StringResources.monthly(currentAppLang()),
            price = "50,000",
            period = StringResources.month(currentAppLang()),
            isSelected = selectedOffer == 0,
            onClick = { selectedOffer = 0 },
            basePrice = "60,000"
         )

         // Annual subscription option
         PriceCard(
            title = StringResources.annual(currentAppLang()),
            price = "500,000",
            period = StringResources.year(currentAppLang()),
            isSelected = selectedOffer == 1,
            onClick = { selectedOffer = 1 },
            basePrice = "600,000",
            savings = "100,000"
         )

         PayWithContent { payment ->
            if (payment == PaymentTypes.Voucher) {
               navController.navigate(CodeActivationRoute)
            }
         }
      }
   }
}

@Composable
fun PriceCard(
   title: String,
   price: String,
   period: String,
   isSelected: Boolean,
   onClick: () -> Unit,
   basePrice: String,
   savings: String? = null
) {
   val cardColor = if (isSelected)
      MaterialTheme.colorScheme.primaryContainer
   else
      MaterialTheme.colorScheme.surface

   val contentColor = if (isSelected)
      MaterialTheme.colorScheme.onPrimaryContainer
   else
      MaterialTheme.colorScheme.onSurface

   Box(
      modifier = Modifier
         .fillMaxWidth()
         .clip(RoundedCornerShape(16.dp))
         .background(cardColor)
         .clickable(onClick = onClick)
         .padding(16.dp)
   ) {
      Column(
         modifier = Modifier.fillMaxWidth(),
         verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
         ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
               if (isSelected) {
                  Icon(
                     imageVector = Icons.Rounded.Check,
                     contentDescription = null,
                     tint = MaterialTheme.colorScheme.primary,
                     modifier = Modifier
                        .size(20.dp)
                        .background(
                           MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                           CircleShape
                        )
                        .padding(4.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
               }

               Text(
                  text = title,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = contentColor
               )
            }

            savings?.let {
               val savingGradients = listOf(
                  Color(0xFFFFEB3A),
                  Color(0xFF4DEF8E)
               )

               val savingBrush = Brush.linearGradient(colors = savingGradients)

               Text(
                  text = "${StringResources.save(currentAppLang())} $it IQD",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp,
                  modifier = Modifier
                     .background(savingBrush, shape = RoundedCornerShape(20.dp))
                     .padding(horizontal = 8.dp, vertical = 4.dp),
                  color = Color.Black
               )
            }
         }

         Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
         ) {
            Text(
               text = price,
               style = MaterialTheme.typography.headlineSmall,
               fontWeight = FontWeight.Bold,
               color = contentColor
            )

            Text(
               text = " IQD / $period",
               style = MaterialTheme.typography.bodyMedium,
               fontWeight = FontWeight.Normal,
               color = contentColor.copy(alpha = 0.7f),
               modifier = Modifier.padding(bottom = 2.dp)
            )
         }

         Text(
            text = basePrice + " IQD",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            textDecoration = TextDecoration.LineThrough,
            color = contentColor.copy(alpha = 0.5f)
         )
      }
   }
}

@Composable
fun PayWithContent(modifier: Modifier = Modifier, onPaymentClick: (PaymentTypes) -> Unit) {
   Column(
      modifier = modifier,
      verticalArrangement = Arrangement.spacedBy(16.dp),
   ) {
      Text(
         text = StringResources.selectPaymentMethod(currentAppLang()),
         style = MaterialTheme.typography.titleMedium,
         color = MaterialTheme.colorScheme.onSurface,
         fontWeight = FontWeight.Bold
      )
      PaymentTypes.values().forEach {
         PayWithButton(
            onClick = { onPaymentClick(it) },
            logo = it.logo,
            enabled = it.enabled,
            text = it.getText()
         )
      }
   }
}

@Composable
private fun PayWithButton(
   modifier: Modifier = Modifier,
   onClick: () -> Unit,
   logo: DrawableResource,
   enabled: Boolean = true,
   text: String
) {
   Button(
      modifier = modifier
         .fillMaxWidth()
         .heightIn(min = 56.dp),
      onClick = onClick,
      enabled = enabled,
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      content = {
         Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            RowC {
               val imageAlpha = if (enabled) 1F else 0.5F
               Image(
                  painter = painterResource(logo),
                  contentDescription = null,
                  modifier = Modifier
                     .size(32.dp)
                     .clip(RoundedCornerShape(4.dp))
                     .alpha(imageAlpha)
               )
               Text(
                  text = text,
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = if (enabled) FontWeight.Bold else FontWeight.Light,
               )
            }

            RowC {
               if (!enabled) {
                  RowC {
                     Text(
                        text = StringResources.comingSoon(currentAppLang()),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Light,
                     )
                  }
               } else {
                  Icon(
                     imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                     contentDescription = null
                  )
               }
            }
         }
      },
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(
         containerColor = MaterialTheme.colorScheme.surface,
         contentColor = MaterialTheme.colorScheme.onSurface,
         disabledContainerColor = MaterialTheme.colorScheme.surface,
      )
   )
}

@Composable
fun FeatureRow(text: String, modifier: Modifier = Modifier) {
   Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
      Icon(imageVector = Icons.Rounded.Check, contentDescription = "Done", tint = MaterialTheme.colorScheme.green)
      Text(
         text = text,
         style = MaterialTheme.typography.labelMedium.copy(textDirection = currentAppLang().direction().getTextDir()),
         modifier = modifier.alpha(0.75F),
         fontWeight = FontWeight.Light,
      )
   }
}

enum class PaymentTypes(val getText: @Composable () -> String, val logo: DrawableResource, val enabled: Boolean = true) {
   Voucher({ StringResources.codeRedemption(currentAppLang()) }, Res.drawable.voucher),
   FIB({ "FIB" }, Res.drawable.fib, false),
   FastPay({ "FastPay" }, Res.drawable.fastpay, false)
}

@Composable
@Preview
fun PaymentScreenPreview() {
   MyPreview(Lang.Krd) {
      PaymentScreen(navController = rememberNavController())
   }
}