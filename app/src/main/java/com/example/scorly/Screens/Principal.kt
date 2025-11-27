package com.example.scorly.Screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scorly.Navigation.LigasRoute // <--- Importamos la ruta de Ligas
import com.example.scorly.Navigation.JugadoresRoute
import com.example.scorly.R
import com.example.scorly.ui.theme.ScorlyTheme
import com.example.scorly.ui.theme.amarillo
import com.example.scorly.ui.theme.blanco
import com.example.scorly.ui.theme.contraseña
import kotlinx.coroutines.launch

@Composable
fun PaginaPrincipal(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondoprincipal),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Oscurecer fondo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        // Icono de regreso
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Regresar",
            tint = blanco,
            modifier = Modifier
                .padding(15.dp)
                .size(28.dp)
        )

        // Contenido con scroll
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

            item {
                AnimatedButton("Jugadores", R.drawable.jugadores, imagen = R.drawable.jugadoresbanner) {
                    navController.navigate(JugadoresRoute)
                }
            }

            item {
                AnimatedButton("Equipos", R.drawable.equipos, imagen = R.drawable.equiposbanner) {
                    // CÓDIGO CORREGIDO: Navegamos al selector de ligas (LigasRoute)
                    // La navegación a Equipos se hace desde la pantalla de Ligas.
                    navController.navigate(LigasRoute)
                }
            }

            item {
                AnimatedButton("Estadísticas", R.drawable.estadisticas, imagen = R.drawable.estadisticasbanner) {
                    navController.navigate("estadisticas")
                }
            }

            item {
                AnimatedButton("Rankings", R.drawable.rankings, imagen = R.drawable.rankingsbanner) {
                    navController.navigate("rankings")
                }
            }

            item {
                AnimatedButton("Historial", R.drawable.historial, imagen = R.drawable.historialbanner) {
                    navController.navigate("rankings")
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
    val rippleRadius = remember { Animatable(0f) }
    val rippleAlpha = remember { Animatable(0f) }

    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .width(300.dp)
            .height(270.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(1.dp, amarillo.copy(alpha = 0.8f), RoundedCornerShape(28.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        rippleRadius.snapTo(0f)
                        rippleAlpha.snapTo(0.4f)

                        // Animación del ripple
                        scope.launch {
                            rippleRadius.animateTo(
                                targetValue = 600f,
                                animationSpec = tween(400)
                            )
                        }
                        scope.launch {
                            rippleAlpha.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(400)
                            )
                        }

                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
    ) {

        Image(
            painter = painterResource(id = imagen),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            contentScale = ContentScale.Crop
        )

        // Ripple
        Canvas(modifier = Modifier.matchParentSize()) {
            drawIntoCanvas {
                drawCircle(
                    color = contraseña.copy(alpha = rippleAlpha.value),
                    radius = rippleRadius.value,
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
        }

        // Icono + texto
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp)
        ) {

            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(65.dp)
                    .padding(end = 12.dp, start = 8.dp)
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

@Preview(showBackground = true)
@Composable
fun PrincipalPreview() {
    ScorlyTheme {
        val fakeNavController = rememberNavController()
        PaginaPrincipal(fakeNavController)
    }
}