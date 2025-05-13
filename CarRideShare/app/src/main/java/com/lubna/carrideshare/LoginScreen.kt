package com.lubna.carrideshare

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Build
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.Gson

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }

    Box {
        YellowBackground()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.height(100.dp)
        )

        Text(
            text = "Login",
            fontSize = 30.sp,
            fontWeight = FontWeight.W300,
            color = Color.White,
            letterSpacing = 4.sp
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

        Button(
            onClick = {
                // Create a login request with the entered email and password
                val loginRequest = LoginRequest(email.text, password.text)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Call the API login endpoint
                        val response: Response<LoginResponse> = RetrofitInstance.api.loginUser(loginRequest)
                        if (response.isSuccessful && response.body() != null) {
                            // On success, save login state in SharedPreferences
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()

                                // Save full user data as JSON
                                val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                                val gson = Gson()
                                val userDataJson = gson.toJson(response.body())
                                sharedPref.edit().apply {
                                    putBoolean("isLoggedIn", true)
                                    putString("userData", userDataJson)
                                    apply()
                                }

                                // Navigate to the home screen (or whichever screen you prefer)
                                navController.navigate("dashboard")
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Login error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
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
            Text(text = "Login With Email", fontSize = 18.sp, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("signupscreen") },
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )
        ) {
            Row {
                Text(text = "Don't have an account?", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Signup", fontSize = 18.sp, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}