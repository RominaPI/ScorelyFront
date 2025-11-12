package com.example.scorly.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scorly.R
import com.example.scorly.ui.theme.ScorlyTheme
import com.example.scorly.ui.theme.blanco
import com.example.scorly.ui.theme.contraseña
import com.example.scorly.ui.theme.negro

@Composable
fun PantallaJugadores() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Fondo de pantalla
        Image(
            painter = painterResource(id = R.drawable.jugadoresfondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Regresar",
            tint = blanco,
            modifier = Modifier
                .padding(top=16.dp, bottom= 4.dp, start=16.dp)
                .size(28.dp)
                .align(Alignment.TopStart)

        )

        Column(){
        Text(
            text = "JUGADORES",
            fontWeight = FontWeight.Bold,
            fontSize = 60.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
modifier     = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                 )


        // Lista de jugadores
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            // Ejemplo de item (puedes repetirlo dinámicamente después)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(horizontal = 20.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    blanco.copy(alpha = 0.3f),
                                    Color.Black,
                                    contraseña.copy(alpha = 0.6f),
                                    blanco.copy(alpha = 0.2f),

                                    )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(1.dp, Color.Black, RoundedCornerShape(16.dp)),

                ) {
                   Box(
                       modifier= Modifier

                           .width(150.dp)
                            .fillMaxHeight()
                           .padding(18.dp)
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                           .align(Alignment.TopStart)
                    )
                }
            }



}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JugadoresPreview() {
    ScorlyTheme {
        PantallaJugadores()
    }
}
