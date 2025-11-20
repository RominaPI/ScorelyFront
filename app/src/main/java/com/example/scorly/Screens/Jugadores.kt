package com.example.Scorly.Screens

import com.example.Scorly.Models.Jugador

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
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
import com.example.Scorly.ui.theme.ScorlyTheme
import java.time.LocalDate
import java.time.Period

@Composable
fun PantallaJugadores(
    jugadores: List<Jugador> = emptyList()
) {
    val posiciones = listOf("PORTERO", "DEFENSA", "MEDIO", "DELANTERO")
    var filtroActual by remember { mutableStateOf("PORTERO") }
    val jugadoresFiltrados = jugadores.filter { it.posicion.uppercase() == filtroActual }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.jugadoresfondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .size(28.dp)
            )

            Text(
                text = "JUGADORES",
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                posiciones.forEach { pos ->
                    FilterChip(
                        selected = filtroActual == pos,
                        onClick = { filtroActual = pos },
                        label = {
                            Text(
                                text = pos,
                                color = if (filtroActual == pos) Color.Black else Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.White,
                            containerColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 50.dp)
            ) {
                items(jugadoresFiltrados) { jugador ->
                    JugadorCard(jugador)
                }
            }
        }
    }
}

fun calcularEdad(fecha: String?): Int {
    if (fecha.isNullOrBlank()) return 0

    return try {
        val limpia = fecha.take(10)
        val separador = if (limpia.contains("-")) "-" else "/"
        val p = limpia.split(separador)

        val (año, mes, dia) = when {
            p[0].length == 4 -> Triple(p[0].toInt(), p[1].toInt(), p[2].toInt())
            else -> Triple(p[2].toInt(), p[1].toInt(), p[0].toInt())
        }

        val nacimiento = LocalDate.of(año, mes, dia)
        val hoy = LocalDate.now()
        Period.between(nacimiento, hoy).years
    } catch (e: Exception) {
        0
    }
}

@Composable
fun JugadorCard(jugador: Jugador) {
    val edad = calcularEdad(jugador.fecha_nacimiento)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 20.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.Black,
                        Color(0xFF8888FF).copy(alpha = 0.6f),
                        Color.White.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .padding(18.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 13.dp)
            ) {
                Text(
                    "${jugador.nombre.uppercase()} ${jugador.apellido.uppercase()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text("${edad} AÑOS", color = Color.White)
                Text(jugador.posicion.uppercase(), color = Color.White)
                Text("EQUIPO ID: ${jugador.equipo_id}", color = Color.White)
                Text("Nº ${jugador.numero_camiseta}", color = Color.White)
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 8.dp, top = 32.dp)
            ) {
                BotonMini("GUARDAR")
                BotonMini("MODIFICAR")
                BotonMini("BAJA")
            }
        }
    }
}

@Composable
fun BotonMini(text: String) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .width(100.dp)
            .height(28.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text.uppercase(),
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JugadoresPreview() {
    ScorlyTheme {
        PantallaJugadores()
    }
}
