package com.lubna.carrideshare

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.withContext

@Composable
fun SignupScreen(navController: NavController){

    val context = LocalContext.current

    var email by remember { mutableStateOf(TextFieldValue("")) }
    var fullName by remember { mutableStateOf(TextFieldValue("")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }

    Box {
        YellowBackground()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.height(100.dp)
        )

        Text(
            text = "Signup",
            fontSize = 30.sp,
            fontWeight = FontWeight.W300,
            color = Color.White,
            letterSpacing = 4.sp
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            shape = RoundedCornerShape(50.dp),  // Extra rounded corners
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Name Icon") },
            label = { Text("Full Name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            shape = RoundedCornerShape(50.dp),  // Extra rounded corners
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(50.dp),
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Outlined.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(icon, contentDescription = "Toggle Password Visibility")
                }
            },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            shape = RoundedCornerShape(50.dp),  // Extra rounded corners
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Call, contentDescription = "Phone Icon") },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )


        Button(
            onClick = {
                val user = User(email.text, fullName.text, phoneNumber.text, password.text)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitInstance.api.registerUser(user)
                        if (response.isSuccessful && response.body() != null) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                                // Optionally, navigate to login screen:
                                 navController.navigate("loginscreen")
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Signup failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Signup error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth(0.8f)
                .height(70.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = "Signup With Email", fontSize = 18.sp, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate("loginscreen") }, colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        )
        ) {
            Row {
                Text(text = "Have an account?", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Login", fontSize = 18.sp, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

    }
}