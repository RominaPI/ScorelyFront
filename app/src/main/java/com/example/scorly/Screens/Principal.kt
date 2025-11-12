package com.example.scorly.Screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import com.example.scorly.R
import com.example.scorly.ui.theme.ScorlyTheme
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

        // Icono de regreso
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Regresar",
            tint = blanco,
            modifier = Modifier
                .padding(15.dp)
                .size(28.dp)
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .padding(top = 22.dp)
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            Text(
                text = "SCORLY",
                fontWeight = FontWeight.Bold,
                fontSize = 90.sp,
                color = blanco.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            AnimatedButton("Jugadores") {
                navController.navigate("jugadores")
            }
            AnimatedButton("Equipos") {
                navController.navigate("equipos")
            }
            AnimatedButton("Estadísticas") {
                navController.navigate("estadisticas")
            }
            AnimatedButton("Rankings") {
                navController.navigate("rankings")
            }
            AnimatedButton("Historial") {
                navController.navigate("rankings")
            }
        }
    }
}

@Composable
fun AnimatedButton(text: String, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val rippleRadius = remember { Animatable(0f) }
    val rippleAlpha = remember { Animatable(0f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(300.dp) // 🔹 MISMO ancho para todos
            .height(90.dp) // 🔹 MISMA altura para todos
            .clip(RoundedCornerShape(28.dp))
            .border(1.dp, Color.Black.copy(alpha = 0.8f), RoundedCornerShape(28.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        rippleRadius.snapTo(0f)
                        rippleAlpha.snapTo(0.4f)

                        // Animaciones ripple
                        scope.launch {
                            rippleRadius.animateTo(
                                targetValue = 600f,
                                animationSpec = tween(durationMillis = 400)
                            )
                        }
                        scope.launch {
                            rippleAlpha.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 400)
                            )
                        }

                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
    ) {
        // Efecto visual del toque
        Canvas(modifier = Modifier.matchParentSize()) {
            drawIntoCanvas {
                drawCircle(
                    color = contraseña.copy(alpha = rippleAlpha.value),
                    radius = rippleRadius.value,
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
        }

        // Texto centrado
        Text(
            text = text,
            fontWeight = FontWeight.W500,
            fontSize = 40.sp,
            color = if (isPressed) contraseña else Color.Black, // 🔹 color cambia al presionar
            textAlign = TextAlign.Center
        )
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
