package com.example.scorly.Screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scorly.Navigation.PrincipalRoute
import com.example.scorly.Navigation.SignUpRoute
import com.example.scorly.R
import com.example.scorly.ui.theme.ScorlyTheme
import com.example.scorly.ui.theme.contraseña
import com.example.scorly.Data.RetrofitClient
import com.example.scorly.Models.LoginRequest
import kotlinx.coroutines.launch

@Composable
fun Login(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    // ANIMACIÓN del logo cuando está cargando
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val colorChange by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color(0xFFFFD700), // Dorado
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo
        Image(
            painter = painterResource(id = R.drawable.loginfondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            // Flecha regresar
            Image(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Regresar",
                modifier = Modifier
                    .padding(15.dp)
                    .size(28.dp)
                    .clickable { navController.popBackStack() },
                alignment = Alignment.Center,
            )

            Spacer(Modifier.weight(6f))

            // Campo EMAIL
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color.White.copy(alpha = 0.7f)) },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = contraseña.copy(alpha = 0.7f),
                    unfocusedContainerColor = contraseña.copy(alpha = 0.7f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .height(50.dp)
                    .width(250.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(28.dp))
                    .align(Alignment.CenterHorizontally)
            )

            // Campo CONTRASEÑA
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña", color = Color.White.copy(alpha = 0.7f)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = contraseña.copy(alpha = 0.7f),
                    unfocusedContainerColor = contraseña.copy(alpha = 0.7f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(50.dp)
                    .width(250.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(28.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Reestablecer contraseña",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 100.dp, top = 20.dp)
                    .clickable {  }
            )

            // BOTÓN / ANIMACIÓN DE CARGA
            Box(
                modifier = Modifier
                    .padding(start = 148.dp, top = 104.dp)
                    .clickable(enabled = !isLoading) {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                try {
                                    val response = RetrofitClient.instance.login(
                                        LoginRequest(email, password)
                                    )
                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        if (body != null && body.mensaje == "Login exitoso") {

                                            navController.navigate(PrincipalRoute)
                                        } else {
                                            errorMessage = body?.mensaje ?: "Credenciales incorrectas"
                                        }
                                    } else {
                                        errorMessage = "Error: ${response.code()}"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Error de conexión: ${e.localizedMessage}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        } else {
                            errorMessage = "Por favor completa los campos"
                        }
                    }
            ) {
                if (isLoading) {
                    Image(
                        painter = painterResource(id = R.drawable.scorelylogo2),
                        contentDescription = "Cargando",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 158.dp)
                            .rotate(rotation)
                            .align(Alignment.Center),
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()

                    )
                } else {
                    Text(
                        text = "ENTRAR",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // MENSAJE DE ERROR
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            // Sign up
            Text(
                text = "SIGN UP",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 158.dp, bottom = 60.dp)
                    .clickable {
                        navController.navigate(SignUpRoute)
                    }
            )

            Spacer(Modifier.weight(1f))

            // Marca
            Text(
                text = "SCORELY",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = contraseña.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 9.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    ScorlyTheme {
        val fakeNav = rememberNavController()
        Login(fakeNav)
    }
}
