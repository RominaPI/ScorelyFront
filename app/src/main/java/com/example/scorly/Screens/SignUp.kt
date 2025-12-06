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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scorly.Data.ApiServiceFactory
import com.example.scorly.Navigation.PrincipalRoute
import com.example.scorly.R
import com.example.scorly.ViewModel.AuthStatus
import com.example.scorly.ViewModel.AuthViewModel
import com.example.scorly.ui.theme.ScorlyTheme
import com.example.scorly.ui.theme.blanco
import com.example.scorly.ui.theme.contraseña
import com.example.scorly.ui.theme.negro

@Composable
fun SignUp(navController: NavController) {

    val apiService = remember { ApiServiceFactory.create() }
    val viewModel: AuthViewModel = remember { AuthViewModel(apiService) }

    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    // --- LÓGICA DE REGISTRO ---
    val performRegistration: () -> Unit = {
        val usuarioClean = usuario.trim()
        val emailClean = email.trim()
        val passwordClean = password.trim()

        if (usuarioClean.isBlank() || emailClean.isBlank() || passwordClean.isBlank()) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
        } else if (authState != AuthStatus.Loading) {
            viewModel.registerUser(usuarioClean, emailClean, passwordClean)
        }
    }


    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthStatus.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            is AuthStatus.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            AuthStatus.Finished -> {
                navController.navigate(PrincipalRoute) {
                    popUpTo(PrincipalRoute) { inclusive = true }
                }
            }
            else -> {}
        }
    }


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
                    .clip(RoundedCornerShape(28.dp)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
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
                keyboardActions = KeyboardActions(onSend = { performRegistration() })
            )

            // Botón ENTRAR
            Text(
                text = if (authState is AuthStatus.Loading) "CARGANDO..." else "ENTRAR",
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = blanco.copy(alpha = 0.9f),
                modifier = Modifier
                    .padding(35.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco.copy(alpha = 0.9f), RoundedCornerShape(28.dp))
                    .background(contraseña.copy(alpha = 0.4f), RoundedCornerShape(28.dp))
                    .padding(horizontal = 40.dp, vertical = 10.dp)
                    .clickable(enabled = authState != AuthStatus.Loading) {
                        performRegistration() // Llama a la lógica del ViewModel
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