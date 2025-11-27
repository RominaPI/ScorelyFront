package com.example.scorly.Screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.scorly.Data.ApiServiceFactory
import com.example.scorly.Models.Equipo
import com.example.scorly.ViewModel.EquiposViewModel
import com.example.scorly.ViewModel.EquiposViewModelFactory
import com.example.scorly.ui.theme.ScorlyTheme
import com.example.scorly.R
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import kotlin.math.absoluteValue


fun getCustomImageLoader(context: Context): ImageLoader {
    val userAgentInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
            .build()
        chain.proceed(request)
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(userAgentInterceptor)
        .build()

    return ImageLoader.Builder(context)
        .okHttpClient(okHttpClient)
        .build()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaEquipos(
    onEquipoClick: (Equipo) -> Unit
) {
    val api = ApiServiceFactory.create()
    val viewModel: EquiposViewModel = viewModel(
        factory = EquiposViewModelFactory(api)
    )

    val equipos by viewModel.equipos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val customImageLoader = remember { getCustomImageLoader(context) }


    val pagerState = rememberPagerState(pageCount = { equipos.size })

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

        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

        Column(modifier = Modifier.fillMaxSize()) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "Equipos",
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            )


            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(color = Color.White)
                    }
                    equipos.isEmpty() -> {
                        Text(
                            text = "No se encontraron equipos.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    else -> {

                        HorizontalPager(
                            state = pagerState,
                            contentPadding = PaddingValues(horizontal = 40.dp),
                            pageSpacing = 10.dp,
                            modifier = Modifier.fillMaxWidth().height(550.dp)
                        ) { page ->

                            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            val scale = lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                            )
                            val alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                            )

                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        this.alpha = alpha
                                    }
                            ) {
                                EquipoCardMinimalista(
                                    equipo = equipos[page],
                                    customImageLoader = customImageLoader,
                                    onClick = onEquipoClick
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun EquipoCardMinimalista(
    equipo: Equipo,
    customImageLoader: ImageLoader,
    onClick: (Equipo) -> Unit
) {
    Log.d("IMAGEN_DEBUG", "Cargando URL para Card: ${equipo.escudo_logo_url}")

    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable { onClick(equipo) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFF0F4F8)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(equipo.escudo_logo_url)
                        .crossfade(true)
                        .build(),

                    contentDescription = "Escudo de ${equipo.nombre}",
                    imageLoader = customImageLoader,

                    modifier = Modifier
                        .size(200.dp)
                        .padding(10.dp),
                    contentScale = ContentScale.Fit,


                    //ESTO LLO PODEMOS BORRAR ES SOLO PARA DEBUGEAR : )

                    onState = { state ->
                        when (state) {
                            is AsyncImagePainter.State.Success -> {
                                Log.d("COIL_STATE", "exito cargando imagen de ${equipo.nombre}")
                            }
                            is AsyncImagePainter.State.Error -> {
                                Log.e("COIL_STATE", "FALLO DE COIL para ${equipo.nombre}: ${state.result.throwable.message}")
                            }
                            is AsyncImagePainter.State.Loading -> {

                            }
                            else -> {}
                        }
                    }
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = equipo.nombre,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = Color.Black,
                        letterSpacing = (-1).sp
                    )

                    Text(
                        text = equipo.nombre_liga.uppercase(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // CAJITA DE DATOS EXTRA
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE8E8E8), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "ESTADIO", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Text(text = equipo.nombre_estadio, fontSize = 14.sp, color = Color.DarkGray)
                        }


                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "CIUDAD", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Text(text = equipo.ciudad, fontSize = 14.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EquiposPreview() {
    ScorlyTheme {
        PantallaEquipos {}
    }
}