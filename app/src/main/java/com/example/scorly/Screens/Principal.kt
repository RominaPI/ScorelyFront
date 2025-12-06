package com.example.scorly.Screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scorly.Navigation.SeleccionLigaParaEstadisticasRoute
import com.example.scorly.Navigation.JugadoresRoute
import com.example.scorly.Navigation.LigasRoute
import com.example.scorly.R
import com.example.scorly.ui.theme.amarillo
import com.example.scorly.ui.theme.blanco
import com.example.scorly.ui.theme.contraseña
import kotlinx.coroutines.launch

@Composable
fun PaginaPrincipal(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondoprincipal),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Regresar",
            tint = blanco,
            modifier = Modifier
                .padding(15.dp)
                .size(28.dp)
                .clickable { navController.popBackStack() }
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 22.dp)
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {

            item {
                Text(
                    text = "SCORELY",
                    fontWeight = FontWeight.Bold,
                    fontSize = 70.sp,
                    color = blanco.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 1. JUGADORES (ROSTER GLOBAL)
            item {
                AnimatedButton("Jugadores", R.drawable.jugadores, imagen = R.drawable.jugadoresbanner) {
                    navController.navigate(JugadoresRoute)
                }
            }

            // 2. EQUIPOS (SELECCIÓN DE LIGA)
            item {
                AnimatedButton("Equipos", R.drawable.equipos, imagen = R.drawable.equiposbanner) {
                    navController.navigate(LigasRoute)
                }
            }

            // 3. ESTADÍSTICAS (SELECCIÓN DE LIGA PARA RANKINGS)
            item {
                AnimatedButton("Estadísticas", R.drawable.estadisticas, imagen = R.drawable.estadisticasbanner) {
                    navController.navigate(SeleccionLigaParaEstadisticasRoute)
                }
            }
        }
    }
}

@Composable
fun AnimatedButton(
    text: String,
    icon: Int,
    imagen: Int,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .width(300.dp)
            .height(270.dp)
            .scale(scale.value)
            .clip(RoundedCornerShape(28.dp))
            .border(1.dp, amarillo.copy(alpha = 0.8f), RoundedCornerShape(28.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        scale.animateTo(0.95f, tween(100))
                        tryAwaitRelease()
                        isPressed = false
                        scale.animateTo(1f, tween(100))
                        onClick()
                    }
                )
            }
    ) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(bottom = 70.dp),
            contentScale = ContentScale.Crop
        )

        if(isPressed) {
            Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.1f)))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(65.dp).padding(end = 12.dp, start = 8.dp)
            )
            Text(
                text = text,
                fontWeight = FontWeight.W600,
                fontSize = 28.sp,
                color = if (isPressed) contraseña else amarillo,
                textAlign = TextAlign.Left
            )
        }
    }
}