package com.example.scorly.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scorly.R
import com.example.scorly.ViewModel.EstadisticasViewModel

data class JugadorTop(val rank: Int, val nombre: String, val equipo: String, val valor: String, val fotoUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasScreen(
    viewModel: EstadisticasViewModel,
    onBack: () -> Unit
) {
    val goleadores by viewModel.goleadores.collectAsState()
    val asistidores by viewModel.asistidores.collectAsState()
    val masLetales by viewModel.masLetales.collectAsState() // <--- Nueva lista real
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    val brushFondo = Brush.verticalGradient(
        colors = listOf(Color(0xFF004D40), Color(0xFF00695C), Color(0xFF000000))
    )

    Box(modifier = Modifier.fillMaxSize().background(brushFondo)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Estadísticas Globales", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    if (error != null) {
                        item {
                            Text(text = error ?: "", color = Color.Red, fontWeight = FontWeight.Bold)
                        }
                    }

                    item {
                        val topScorer = goleadores.firstOrNull()
                        JugadorDelMomentoCard(jugador = topScorer)
                    }

                    item {
                        if (goleadores.isNotEmpty()) {
                            SeccionTop("Top Goleadores", Icons.Default.Star, goleadores)
                        } else {
                            Text("No hay datos de goleadores", color = Color.LightGray)
                        }
                    }

                    item {
                        if (asistidores.isNotEmpty()) {
                            SeccionTop("Máximos Asistidores", Icons.Default.Star, asistidores)
                        }
                    }

                    item {
                        if (masLetales.isNotEmpty()) {
                            SeccionTop("Los Más Letales (Promedio)", Icons.Default.Star, masLetales)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun JugadorDelMomentoCard(jugador: JugadorTop?) {
    val nombre = jugador?.nombre ?: "Cargando..."
    val equipo = jugador?.equipo ?: "..."
    val etiqueta = if (jugador != null) "Líder: ${jugador.valor}" else "..."
    val foto = jugador?.fotoUrl ?: "https://media.api-sports.io/football/players/633.png"

    val brushCard = Brush.horizontalGradient(listOf(Color(0xFF00C853), Color(0xFF64DD17)))

    Card(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(brushCard)) {
            Image(
                painter = painterResource(id = R.drawable.fondoprincipal),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.2f
            )

            Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

                Column(modifier = Modifier.weight(1f)) {
                    Text("JUGADOR DEL MOMENTO", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    Text(
                        text = nombre,
                        color = Color.White,
                        fontSize = if (nombre.length > 15) 22.sp else 28.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 30.sp
                    )
                    Text(text = equipo, color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    ChipVerde(etiqueta)
                }

                AsyncImage(
                    model = foto,
                    contentDescription = nombre,
                    modifier = Modifier.size(120.dp).clip(CircleShape).border(2.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun SeccionTop(titulo: String, icon: ImageVector, jugadores: List<JugadorTop>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFF64DD17))
            Spacer(modifier = Modifier.width(8.dp))
            Text(titulo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(jugadores) { jugador ->
                JugadorTopCard(jugador)
            }
        }
    }
}

@Composable
fun JugadorTopCard(jugador: JugadorTop) {
    Card(
        modifier = Modifier.width(140.dp).height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                AsyncImage(
                    model = jugador.fotoUrl,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                // Posición
                Box(modifier = Modifier.align(Alignment.TopStart).background(if(jugador.rank == 1) Color(0xFFFFD700) else Color(0xFF00C853), CircleShape).size(24.dp), contentAlignment = Alignment.Center) {
                    Text("${jugador.rank}", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(jugador.nombre, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center, maxLines = 1)
            Text(jugador.equipo, color = Color.Gray, fontSize = 10.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(jugador.valor, color = Color(0xFF64DD17), fontWeight = FontWeight.Black, fontSize = 16.sp)
        }
    }
}

@Composable
fun ChipVerde(texto: String) {
    Box(
        modifier = Modifier.background(Color(0xFF00C853), RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(texto, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}