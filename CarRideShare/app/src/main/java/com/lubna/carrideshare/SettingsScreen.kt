package com.lubna.carrideshare

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson

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
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Settings",
                fontSize = 30.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "User Email: $userEmail",
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    // Clear login state and user data
                    sharedPref.edit().clear().apply()
                    // Navigate back to the landing screen
                    navController.navigate("landing") {
                        popUpTo("landing") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(40.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(70.dp)

            ) {
                Text(text = "Log Out", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}
