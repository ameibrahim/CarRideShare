package com.lubna.carrideshare

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Dashboard(navController: NavController) {

    Box {
        YellowBackground()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Dashboard",
            fontSize = 30.sp,
            fontWeight = FontWeight.W300,
            color = Color.White,
            letterSpacing = 4.sp
        )

        Button(onClick = {
            navController.navigate("settings")
        }){
            Image(
                painter = painterResource(id = R.drawable.gear),
                contentDescription = "App Logo",
                modifier = Modifier.height(50.dp)
            )
        }
    }
}