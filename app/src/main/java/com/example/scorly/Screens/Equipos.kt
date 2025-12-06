package com.example.scorly.Screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scorly.Data.ApiServiceFactory
import com.example.scorly.Models.Equipo
import com.example.scorly.R
import com.example.scorly.ViewModel.EquiposViewModel
import com.example.scorly.ViewModel.EquiposViewModelFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient

fun getCustomImageLoader(context: Context): ImageLoader {
    val userAgentInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .header("User-Agent", "Mozilla/5.0")
            .build()
        chain.proceed(request)
    }
    val okHttpClient = OkHttpClient.Builder().addInterceptor(userAgentInterceptor).build()
    return ImageLoader.Builder(context).okHttpClient(okHttpClient).build()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEquipos(
    ligaId: String,
    onBackClick: () -> Unit,
    onSiguienteClick: () -> Unit = {},
    // Nota: Aunque borramos el botón, mantengo el parámetro por si lo usas en la navegación
    // para no romper la llamada en tu MainActivity/NavGraph.
    onNuevoEquipoClick: () -> Unit = {}
) {
    val api = ApiServiceFactory.create()
    val viewModel: EquiposViewModel = viewModel(
        factory = EquiposViewModelFactory(api)
    )

    LaunchedEffect(ligaId) {
        val idInt = ligaId.toIntOrNull() ?: 0
        if (idInt != 0) {
            viewModel.cargarEquiposPorLiga(idInt)
        }
    }

    val equipos by viewModel.equipos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val selectedIds = remember { mutableStateListOf<Int>() }
    var searchText by remember { mutableStateOf("") }

    val equiposFiltrados = equipos.filter {
        it.nombre.contains(searchText, ignoreCase = true)
    }

    val context = LocalContext.current
    val customImageLoader = remember { getCustomImageLoader(context) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.jugadoresfondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)))

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Elegir Equipos", fontWeight = FontWeight.Bold, color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Atrás", tint = Color.White)
                        }
                    },
                    actions = {
                        TextButton(onClick = onSiguienteClick) {
                            Text("SIGUIENTE", color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
            // El floatingActionButton ha sido eliminado de aquí
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(24.dp),
                    placeholder = { Text("Buscar equipo...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF00C853),
                        focusedBorderColor = Color(0xFF00C853),
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF00C853))
                    }
                } else if (equipos.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay equipos disponibles", color = Color.White)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(equiposFiltrados) { equipo ->
                            val isSelected = selectedIds.contains(equipo.equipo_id)

                            EquipoItem(
                                equipo = equipo,
                                isSelected = isSelected,
                                imageLoader = customImageLoader,
                                onToggle = {
                                    if (isSelected) selectedIds.remove(equipo.equipo_id)
                                    else selectedIds.add(equipo.equipo_id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EquipoItem(
    equipo: Equipo,
    isSelected: Boolean,
    imageLoader: ImageLoader,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier.size(100.dp).clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) Color(0xFF00C853) else Color.White.copy(alpha = 0.3f),
                    shape = CircleShape
                )
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(equipo.escudo_logo_url)
                    .crossfade(true)
                    .build(),
                contentDescription = equipo.nombre,
                imageLoader = imageLoader,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier.align(Alignment.TopEnd).offset(x = (-10).dp, y = 5.dp).size(24.dp).background(Color(0xFF00C853), CircleShape).border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
    }
}