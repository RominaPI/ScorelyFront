package com.example.scorly.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.scorly.ui.theme.negro

@Composable
fun Login(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Regresar",
                tint = Color.White,
                modifier = Modifier
                    .padding(15.dp)
                    .size(28.dp)
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

                    ),
                modifier = Modifier
                    .height(50.dp)
                    .width(250.dp)
                    .background(contraseña.copy(alpha = 0.7f), RoundedCornerShape(28.dp))
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
                    .clickable { /* Aquí podrías navegar a otra pantalla */ }
            )

            Text(
                text = "ENTRAR",
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 148.dp, top = 104.dp)
                    .clickable {
                        navController.navigate(PrincipalRoute)
                    }
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "SIGN UP",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.White.copy(alpha=0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 158.dp, bottom = 60.dp)
                    .clickable {
                        navController.navigate(SignUpRoute)
                    }
            )

            Spacer(Modifier.weight(1f))

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
