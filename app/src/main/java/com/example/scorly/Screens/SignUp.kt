package com.example.scorly.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scorly.R
import com.example.scorly.ui.theme.ScorlyTheme
import com.example.scorly.ui.theme.blanco
import com.example.scorly.ui.theme.contraseña
import com.example.scorly.ui.theme.negro

@Composable
fun SignUp(navController: NavController) {
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

            var usuario by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            // Campo de Usuario
            TextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .height(55.dp)
                    .width(270.dp)
                    .padding(top=10.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
            )
            TextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Email") },
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .height(55.dp)
                    .width(270.dp)
                    .padding(top=10.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
            )

            // Campo de Contraseña
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .height(55.dp)
                    .width(270.dp)
                    .padding(top=10.dp)

                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
            )

            // Botón Entrar
            Text(
                text = "ENTRAR",
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = blanco.copy(alpha = 0.9f),
                modifier = Modifier
                    .padding( 35.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, blanco.copy(alpha = 0.9f), RoundedCornerShape(28.dp))
                    .background(contraseña.copy(alpha = 0.4f), RoundedCornerShape(28.dp))
                    .padding(horizontal = 40.dp, vertical = 10.dp)
                    .clickable {
                        navController.navigate("pagina_principal")
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
