package com.lubna.carrideshare

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigator() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") { LandingScreen(navController) }
        composable("loginscreen") { LoginScreen(navController) }
        composable("signupscreen") { SignupScreen(navController) }
        composable("dashboard") { Dashboard(navController) }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigator()
        }
    }
}

@Composable
fun LandingScreen(navController: NavController) {

    val context = LocalContext.current

    // Check login state when the LandingScreen is first composed
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        if (sharedPref.getBoolean("isLoggedIn", false)) {
            // Navigate directly to the dashboard if already logged in.
            navController.navigate("dashboard") {
                // Clear the backstack so the user can't navigate back to landing.
                popUpTo("landing") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        YellowBackground()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.height(100.dp)
            )

            Spacer(modifier = Modifier.weight(1f))


            Text(
                text = "Share a",
                fontSize = 30.sp,
                fontWeight = FontWeight.W200,
                color = Color.White,
                letterSpacing = 4.sp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Taxi Ride",
                fontSize = 50.sp,
                fontWeight = FontWeight.W800,
                color = Color.White,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "It's cheaper, more fun and will reduce CO2 emissions",
                fontSize = 26.sp,
                fontWeight = FontWeight.W400,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 60.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("loginscreen") },
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(40.dp),
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth(0.8f)
                    .height(70.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(5.dp))
                    IconButton(onClick = {}) {
                        Image(painter = painterResource(id = R.drawable.email),
                            contentDescription = "email icon",
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Login With Email", fontSize = 18.sp, color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(30.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { navController.navigate("signupscreen") }, colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )) {
                Row {
                    Text(text = "Don't have an account?", fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Signup", fontSize = 18.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

        }
    }
}