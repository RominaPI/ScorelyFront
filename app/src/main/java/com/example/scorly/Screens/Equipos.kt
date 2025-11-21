package com.example.Scorly.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scorly.R
import com.example.Scorly.Models.Equipo
import com.example.Scorly.ViewModel.EquiposViewModel
import com.example.Scorly.ViewModel.EquiposViewModelFactory
import com.example.Scorly.Data.ApiServiceFactory

@Composable
fun PantallaEquipos(
    onEquipoClick: (Equipo) -> Unit
) {
    // Crear API desde Factory
    val api = ApiServiceFactory.create()

    // Crear ViewModel con Factory
    val viewModel: EquiposViewModel = viewModel(
        factory = EquiposViewModelFactory(api)
    )

    val equipos by viewModel.equipos.collectAsState()

    LaunchedEffect(true) {
        viewModel.getEquipos()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        Image(
            painter = painterResource(id = R.drawable.jugadoresfondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(28.dp)
                )
            }

            Text(
                text = "Equipos",
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                items(equipos) { equipo ->
                    EquipoMegaBoton(equipo = equipo) {
                        onEquipoClick(equipo)
                    }
                }
            }
        }
    }
}

@Composable
fun EquipoMegaBoton(
    equipo: Equipo,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(800.dp)
            .clickable { onClick() }
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.85f),
                        Color.White.copy(alpha = 0.55f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = equipo.escudo_logo_url,
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Text(
                text = equipo.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black
            )

            Text(
                text = "Ciudad: ${equipo.ciudad}",
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Text(
                text = "Estadio: ${equipo.nombre_estadio}",
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Text(
                text = "Liga: ${equipo.nombre_liga}",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}
