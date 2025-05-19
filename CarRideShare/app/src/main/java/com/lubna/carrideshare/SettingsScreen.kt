package com.lubna.carrideshare

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    // Retrieve the user data JSON from SharedPreferences and parse it
    val userDataJson = sharedPref.getString("userData", null)
    val userEmail = if (userDataJson != null) {
        val gson = Gson()
        val user = gson.fromJson(userDataJson, LoginResponse::class.java)
        user.email
    } else {
        "No Email"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        YellowBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Settings",
                fontSize = 40.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = Color.White
            )

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = userEmail,
                    fontSize = 20.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        // Clear login state and user data
                        sharedPref.edit().clear().apply()
                        // Navigate back to the landing screen
                        navController.navigate("landing") {
                            popUpTo("landing") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp)

                ) {
                    Text(text = "Log Out", fontSize = 18.sp, color = Color.White)
                }
            }

            val faqList = remember {
                mutableStateListOf(
                    FAQItem("What is RideShare?", "RideShare connects users that are going to the same location together so that they can split the cost of the ride."),
                    FAQItem("How many people can I share a ride with?", "Because the are only for 4 empty spots in a car and 2 are for the driver and you. You can have a maximum of 3 free slots to share a ride with."),
                    FAQItem("How do I begin creating a ride share?", "To begin creating a ride share, on your dashboard, click the new ride share button and complete the missing fields. Once it has been created, wait if anyone wants to join and start your trip."),
                    FAQItem("I cannot see a place to add my card for payment", "At the moment we do not accept in payment methods, you will have to pay the driver with cash. Future updates will connect you to the driver so that they get their payment automatically."),
                    FAQItem("Can I cancel a ride share?", "Yes, you can cancel a ride share by simply deleting it if you are the owner or opting out if you said yes to one. This will remove you from the current booking.")
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            FAQSection(
                faqs = faqList
            )
        }
    }
}
