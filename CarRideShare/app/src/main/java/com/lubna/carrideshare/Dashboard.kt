package com.lubna.carrideshare

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Row {
            Text(
                text = "Dashboard",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    navController.navigate("searchRides")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search Logo",
                    modifier = Modifier.height(50.dp)
                )
            }

            IconButton(
                onClick = {
                    navController.navigate("settings")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gear),
                    contentDescription = "Settings Logo",
                    modifier = Modifier.height(50.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(text = "Available Ride Shares", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(10.dp))


        Column(verticalArrangement = Arrangement.spacedBy((-10).dp)) {
            RideShareItem(
                rideName = "Carpool to Campus With Lubna",
                departureTime = "08:30 AM",
                onClick = { /* handle click navigation or action */ }
            )

            RideShareItem(
                rideName = "Ride Share To Town With Ahmed",
                departureTime = "10:30 AM",
                onClick = { /* handle click navigation or action */ }
            )

            RideShareItem(
                rideName = "Go to Beach With Hussein",
                departureTime = "4:30 PM",
                onClick = { /* handle click navigation or action */ }
            )
        }

        Spacer(Modifier.height(20.dp))


        Button(
            onClick = {
                navController.navigate("newrideshare")
            },
            colors = ButtonDefaults.buttonColors(Color.Black),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
                .align(Alignment.CenterHorizontally)

        ) {
            Text(text = "New Ride Share", fontSize = 18.sp, color = Color.White)
        }

        Spacer(Modifier.height(20.dp))

        Text(text = "My Ride Shares", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy((-10).dp)) {
            RideShareItem(
                rideName = "Carpool to Campus With Lubna",
                departureTime = "08:30 AM",
                onClick = { /* handle click navigation or action */ }
            )

            RideShareItem(
                rideName = "Carpool To Bakery With Lubna",
                departureTime = "9:00 AM",
                onClick = { /* handle click navigation or action */ }
            )

            RideShareItem(
                rideName = "Carpool To Market With Lubna",
                departureTime = "5:00 PM",
                onClick = { /* handle click navigation or action */ }
            )
        }

    }
}