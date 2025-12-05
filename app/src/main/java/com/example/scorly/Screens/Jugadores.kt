package com.example.scorly.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scorly.Models.Jugador
import com.example.scorly.R

@Composable
fun PantallaJugadores(
    jugadores: List<Jugador> = emptyList(),
    onJugadorClick: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNuevoJugadorClick: () -> Unit = {}
) {
    val posiciones = listOf("PORTERO", "DEFENSA", "MEDIO", "DELANTERO")
    var filtroActual by remember { mutableStateOf("DELANTERO") }

    // Lógica de filtrado segura (Anti-Crash)
    val jugadoresFiltrados = jugadores.filter { jugador ->
        val posApi = jugador.posicion?.uppercase() ?: ""
        val categoria = when {
            posApi.contains("PORTERO") || posApi.contains("ARQUERO") -> "PORTERO"
            posApi.contains("DEFENSA") || posApi.contains("LATERAL") || posApi.contains("CENTRAL") -> "DEFENSA"
            posApi.contains("MEDIO") || posApi.contains("VOLANTE") || posApi.contains("CENTROCAMPISTA") -> "MEDIO"
            posApi.contains("DELANTERO") || posApi.contains("EXTREMO") || posApi.contains("PUNTA") -> "DELANTERO"
            else -> "OTROS"
        }
        categoria == filtroActual
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.jugadoresfondo1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back",
                tint = Color.White,
                modifier = Modifier.padding(16.dp).size(28.dp).clickable { onBackClick() }
            )

            Text(
                text = "JUGADORES",
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = Color.White,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                posiciones.forEach { pos ->
                    FilterChip(
                        selected = filtroActual == pos,
                        onClick = { filtroActual = pos },
                        label = { Text(text = pos, color = if (filtroActual == pos) Color.Black else Color.White, fontWeight = FontWeight.SemiBold, fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.White, containerColor = Color.White.copy(alpha = 0.2f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp, top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(jugadoresFiltrados) { jugador ->
                    JugadorCardWithUrl(
                        jugador = jugador,
                        onClick = { onJugadorClick(jugador.jugador_id) }
                    )
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onNuevoJugadorClick,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp),
            containerColor = Color.White,
            contentColor = Color.Black,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "REGISTRAR JUGADOR", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun JugadorCardWithUrl(jugador: Jugador, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(1f).fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 0.dp)) {
                Column(modifier = Modifier.weight(0.45f).padding(top = 10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = jugador.nombre.uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black, maxLines = 1, textAlign = TextAlign.Center)
                    Text(text = jugador.apellido.uppercase(), fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, maxLines = 1)
                    Text(text = "${jugador.numero_camiseta ?: "-"}", fontSize = 40.sp, fontWeight = FontWeight.Black, color = Color.Black, lineHeight = 36.sp, modifier = Modifier.padding(top = 10.dp))
                }
                Box(modifier = Modifier.weight(0.50f).fillMaxHeight(), contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = jugador.foto_url,
                        contentDescription = "Foto Jugador",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxWidth().height(50.dp).background(brush = Brush.horizontalGradient(colors = listOf(Color(0xFF44a538), Color(0xFF006b4a))))) {
                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = jugador.nombre_equipo?.uppercase() ?: "SIN EQUIPO",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                    AsyncImage(
                        model = jugador.escudo_equipo,
                        contentDescription = "Escudo",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}
