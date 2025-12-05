package com.example.scorly.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.scorly.Data.ApiServiceFactory
import com.example.scorly.Models.Estadistica
import com.example.scorly.R
import com.example.scorly.ViewModel.DetallesJugadorViewModel
import com.example.scorly.ViewModel.DetallesJugadorViewModelFactory

@Composable
fun PantallaDetallesJugador(
    jugadorId: Int,
    onBackClick: () -> Unit
) {
    val api = ApiServiceFactory.create()
    val viewModel: DetallesJugadorViewModel = viewModel(
        factory = DetallesJugadorViewModelFactory(api)
    )

    var estadisticas by remember { mutableStateOf<List<Estadistica>>(emptyList()) }
    var loadingStats by remember { mutableStateOf(true) }

    LaunchedEffect(jugadorId) {
        viewModel.obtenerDetallesJugador(jugadorId)

        try {
            val response = api.getEstadisticasJugador(jugadorId)
            if (response.isSuccessful && response.body() != null) {
                estadisticas = response.body()!!.data
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            loadingStats = false
        }
    }

    val jugador by viewModel.jugador.collectAsState()
    val nombreEquipo by viewModel.nombreEquipo.collectAsState()
    val nombreLiga by viewModel.nombreLiga.collectAsState()
    val isLoadingJugador by viewModel.isLoading.collectAsState()

    if (isLoadingJugador || jugador == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF006b4a))
        }
    } else {
        jugador?.let { p ->
            val brushVerde = Brush.verticalGradient(
                colors = listOf(Color(0xFF44a538), Color(0xFF006b4a))
            )

            Scaffold { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                                .background(brushVerde)
                        )

                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                        }

                        AsyncImage(
                            model = p.foto_url,
                            contentDescription = null,
                            modifier = Modifier
                                .height(300.dp)
                                .align(Alignment.BottomCenter)
                                .offset(y = 20.dp),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(R.drawable.ic_launcher_foreground),
                            error = painterResource(R.drawable.ic_launcher_foreground)
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${p.nombre} ${p.apellido}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                        Text(
                            text = p.posicion?.uppercase() ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = nombreEquipo,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF006b4a)
                                )
                                Text(
                                    text = nombreLiga,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DatoChip(titulo = "NÚMERO", valor = "#${p.numero_camiseta ?: "-"}")
                            DatoChip(titulo = "NACIONALIDAD", valor = p.nacionalidad ?: "N/A")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    if (estadisticas.isNotEmpty()) {
                        val stat = estadisticas[0]

                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            Text(
                                text = "ESTADÍSTICAS ${stat.temporada}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF006b4a)
                            )
                            Text(
                                text = "${stat.nombre_equipo} • ${stat.nombre_liga}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StatBoxBig(Modifier.weight(1f), "GOLES", stat.goles.toString(), Color(0xFF2E7D32))
                            StatBoxBig(Modifier.weight(1f), "ASISTENCIAS", stat.asistencias.toString(), Color(0xFF1565C0))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StatBoxBig(Modifier.weight(1f), "T. AMARILLAS", stat.tarjetas_amarillas.toString(), Color(0xFFFBC02D))
                            StatBoxBig(Modifier.weight(1f), "MINUTOS", stat.minutos_jugados.toString(), Color(0xFF455A64))
                        }

                    } else if (loadingStats) {
                        Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.Gray)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Este jugador no tiene estadísticas registradas.", color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}

@Composable
fun DatoChip(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(titulo, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(valor, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatBoxBig(modifier: Modifier = Modifier, titulo: String, valor: String, color: Color) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, fontSize = 32.sp, fontWeight = FontWeight.Black, color = color)
            Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}