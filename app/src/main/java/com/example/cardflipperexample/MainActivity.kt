package com.example.cardflipperexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardflipperexample.ui.CardFlipper.FlipperCardView
import com.example.cardflipperexample.ui.FlipOrientation
import com.example.cardflipperexample.ui.UiHelper.BodyText
import com.example.cardflipperexample.ui.UiHelper.ThemeButton

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface {
                MainScreenUi()
            }
        }
    }

    @Composable
    private fun MainScreenUi() {
        val buttonText = remember { mutableStateOf("Back-Side") }
        val isCardFlip = remember { mutableStateOf(false) }
        val heightValue = remember { mutableIntStateOf(0) }
        val flipOrientation = remember { mutableStateOf(FlipOrientation.HORIZONTAL) }
        val flipOrientationSize = FlipOrientation.entries.size
        buttonText.value = if (isCardFlip.value) {
            "Front-Side"
        } else {
            "Back-Side"
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(vertical = 40.dp, horizontal = 10.dp)

        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                //CardView
                FlipperCardView(
                    modifier = Modifier
                        .fillMaxWidth(),
                    flipCard = isCardFlip.value,
                    flipOrientation = flipOrientation.value,
                    frontContent = {
                        ATMCardFrontUI(heightValue)
                    },
                    backContent = {
                        ATMCardBackUI(heightValue.intValue)
                    }
                )
                //All Flip buttons here to show all effects
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = StaggeredGridCells.Fixed(2)
                ) {
                    items(flipOrientationSize) { index ->
                        val item = FlipOrientation.entries[index]
                        ThemeButton(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(40.dp), bodyText = {
                            BodyText(
                                text = item.name,
                                fontColor = R.color.white,
                                textAlign = TextAlign.Center
                            )
                        }) {
                            flipOrientation.value = item
                            isCardFlip.value = !isCardFlip.value
                        }
                    }
                }

            }
        }
    }

    @Composable
    private fun ATMCardFrontUI(value: MutableState<Int>) {
        // Card Background
        Box(
            modifier = Modifier
                .fillMaxWidth()

                .onGloballyPositioned { cordinates ->
                    value.value = cordinates.size.height

                    Log.e("YEETS", "${value.value}")
                }
                .background(colorResource(R.color.accentColor), RoundedCornerShape(10.dp))
        ) {
            // Card Content
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                // Top Row (Bank Logo and Card Type)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.bank_logo),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        colorFilter = ColorFilter.tint(colorResource(R.color.white))
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BodyText(
                        text = "premium",
                        fontColor = R.color.white,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }

                // Chip and NFC Icon Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.chip_img),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(30.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(R.drawable.nfc_img),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(colorResource(R.color.white))
                    )
                }

                // Card Number Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.back_img),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(30.dp)
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(colorResource(R.color.white))
                    )
                    BodyText(
                        text = "1234  5678  9012  3456",
                        fontColor = R.color.white,
                        textAlign = TextAlign.Justify,

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        textSize = 18.sp,
                    )
                }

                // Card Information Row
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp)
                                .height(70.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BodyText(
                                    text = "MEMBER\nSINCE",
                                    fontColor = R.color.white,
                                    modifier = Modifier.padding(end = 5.dp),
                                    lineHeight = 4.sp,
                                    textSize = 7.sp
                                )
                                BodyText(
                                    text = "23",
                                    fontColor = R.color.white,
                                    modifier = Modifier.padding(end = 5.dp),
                                    textSize = 14.sp
                                )
                                Spacer(modifier = Modifier.weight(0.2f))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    BodyText(
                                        text = "VALID FROM",
                                        fontColor = R.color.white,
                                        lineHeight = 4.sp,
                                        textSize = 7.sp
                                    )
                                    BodyText(
                                        text = "02/22",
                                        fontColor = R.color.white,
                                        textSize = 14.sp,
                                    )
                                }
                                Spacer(modifier = Modifier.weight(0.2f))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    BodyText(
                                        text = "VALID THRU",
                                        fontColor = R.color.white,
                                        lineHeight = 4.sp,
                                        textSize = 7.sp
                                    )
                                    BodyText(
                                        text = "02/27",
                                        fontColor = R.color.white,
                                        textSize = 14.sp,
                                    )
                                }
                                Spacer(modifier = Modifier.weight(0.2f))
                            }
                            BodyText(
                                text = "HOLDER NAME HERE",
                                fontColor = R.color.white,
                                textSize = 18.sp,
                                textAlign = TextAlign.Justify,

                                modifier = Modifier.padding(top = 10.dp, start = 10.dp)
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.master_card),
                            contentDescription = null,
                            modifier = Modifier
                                .size(65.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ATMCardBackUI(value: Int) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { value.toDp() })
                .background(colorResource(R.color.accentColor), RoundedCornerShape(10.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                // Magnetic Stripe
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(colorResource(R.color.black), RoundedCornerShape(4.dp))
                        .padding(vertical = 10.dp)
                )

                // Signature Panel and CVV
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp), // Adjusted top padding
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .height(40.dp)
                            .background(colorResource(R.color.white))
                    ) {
                        BodyText(
                            text = "Signature",
                            fontColor = R.color.black,
                            textSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(horizontal = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Column(
                        modifier = Modifier.weight(0.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BodyText(
                            text = "CVV",
                            fontColor = R.color.white,
                            textSize = 10.sp
                        )
                        BodyText(
                            text = "123",
                            fontColor = R.color.white,
                            textSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp)) // Added space after signature panel

                // Top Row (Logo)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp), // Adjusted padding
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.bank_logo),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        colorFilter = ColorFilter.tint(colorResource(R.color.white))
                    )
                    Spacer(modifier = Modifier.weight(1f)) // Added space between logo and text
                    BodyText(
                        text = "This card is the property of X Bank. It is valid for use by the authorized cardholder. If found, please return to the nearest branch or call our customer service at 123-456-7890. Unauthorized use will be reported to the authorities.",
                        fontColor = R.color.white,
                        textSize = 7.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp)) // Added space between terms and help line

                // Help Line Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp), // Adjusted padding
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f)) // Space on the left
                    BodyText(
                        text = "Help Line: 123-456-7890",
                        fontColor = R.color.white,
                        textSize = 7.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }
    }
}