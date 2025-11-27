package com.example.scorly.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scorly.Data.ApiService
import com.example.scorly.Models.RegistroRequest
import com.example.scorly.Models.RegistroResponse
import com.example.scorly.Navigation.PrincipalRoute
import com.example.scorly.R
import com.example.scorly.ui.theme.ScorlyTheme
import com.example.scorly.ui.theme.blanco
import com.example.scorly.ui.theme.contraseña
import com.example.scorly.ui.theme.negro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun SignUp(navController: NavController) {
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Crear Retrofit local
    val api: ApiService = Retrofit.Builder()
        .baseUrl("http://165.227.57.191:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        blanco.copy(alpha = 0.9f),
                        negro.copy(alpha = 0.6f),
                        contraseña.copy(alpha = 0.9f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Regresar",
                tint = contraseña,
                modifier = Modifier
                    .padding(15.dp)
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )

            // Imágenes decorativas
            listOf(
                R.drawable.secuencia1,
                R.drawable.secuencia2,
                R.drawable.secuencia31
            ).forEach { img ->
                Box(
                    modifier = Modifier
                        .width(400.dp)
                        .height(170.dp)
                        .padding(10.dp, bottom = 30.dp, end = 10.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = img),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(40.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Campos de Usuario, Email y Contraseña
            TextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .height(55.dp)
                    .width(270.dp)
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp)), keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .height(55.dp)
                    .width(270.dp)
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
                , keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .height(55.dp)
                    .width(270.dp)
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
                , keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send,
                    keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(
                    onSend={
                        val usuarioClean = usuario.trim()
                        val emailClean = email.trim()
                        val passwordClean = password.trim()

                        if (usuarioClean.isBlank() || emailClean.isBlank() || passwordClean.isBlank()) {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response: Response<RegistroResponse> = api.register(
                                        RegistroRequest(
                                            nombre = usuarioClean,
                                            email = emailClean,
                                            password = passwordClean
                                        )
                                    )

                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        CoroutineScope(Dispatchers.Main).launch {
                                            Toast.makeText(
                                                context,
                                                body?.mensaje ?: "Registro exitoso",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate(PrincipalRoute)
                                        }
                                    } else {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            Toast.makeText(
                                                context,
                                                "Error ${response.code()}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(
                                            context,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }

                    }
                )
            )

            // Botón ENTRAR
            Text(
                text = "ENTRAR",
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = blanco.copy(alpha = 0.9f),
                modifier = Modifier
                    .padding(35.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco.copy(alpha = 0.9f), RoundedCornerShape(28.dp))
                    .background(contraseña.copy(alpha = 0.4f), RoundedCornerShape(28.dp))
                    .padding(horizontal = 40.dp, vertical = 10.dp)
                    .clickable {
                        val usuarioClean = usuario.trim()
                        val emailClean = email.trim()
                        val passwordClean = password.trim()

                        if (usuarioClean.isBlank() || emailClean.isBlank() || passwordClean.isBlank()) {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response: Response<RegistroResponse> = api.register(
                                        RegistroRequest(
                                            nombre = usuarioClean,
                                            email = emailClean,
                                            password = passwordClean
                                        )
                                    )

                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        CoroutineScope(Dispatchers.Main).launch {
                                            Toast.makeText(
                                                context,
                                                body?.mensaje ?: "Registro exitoso",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate(PrincipalRoute)
                                        }
                                    } else {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            Toast.makeText(
                                                context,
                                                "Error ${response.code()}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(
                                            context,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    },
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "SCORELY",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = blanco.copy(alpha = 0.6f),
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
fun SignUpPreview() {
    ScorlyTheme {
        val fakeNavController = rememberNavController()
        SignUp(fakeNavController)
    }
}
