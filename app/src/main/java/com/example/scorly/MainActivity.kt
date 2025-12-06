package com.example.scorly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.scorly.Data.ApiServiceFactory
import com.example.scorly.Navigation.*
import com.example.scorly.Screens.*
import com.example.scorly.ViewModel.EstadisticasViewModel
import com.example.scorly.ViewModels.LigasViewModel
import com.example.scorly.ui.theme.ScorlyTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScorlyTheme {
                val nav = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        navController = nav,
                        startDestination = HomeScreenRoute,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        // 1. HOME
                        composable<HomeScreenRoute> {
                            HomeScreen(navController = nav)
                        }

                        // 2. JUGADORES
                        composable<JugadoresRoute>{
                            PantallaJugadores(
                                onJugadorClick = { id ->
                                    nav.navigate(DetallesJugadorRoute(id))
                                },
                                onBackClick = {
                                    nav.navigate(PrincipalRoute) { popUpTo(PrincipalRoute) { inclusive = true } }
                                },

                                )
                        }

                        // 3. LOGIN
                        composable<LoginRoute> {
                            Login(navController = nav)
                        }

                        // 4. PRINCIPAL
                        composable<PrincipalRoute> {
                            PaginaPrincipal(nav)
                        }

                        // 5. SIGNUP
                        composable<SignUpRoute>{
                            SignUp(nav)
                        }

                        // 6. LIGAS
                        composable<LigasRoute> {
                            val ligasViewModel: LigasViewModel = viewModel()

                            PantallaSeleccionLiga(
                                viewModel = ligasViewModel,
                                onLigaClick = { idString ->
                                    val idInt = idString.toIntOrNull() ?: 1
                                    nav.navigate(EquiposRoute(id = idInt))
                                }
                            )
                        }

                        // 7. EQUIPOS
                        composable<EquiposRoute> { backStackEntry ->
                            val args = backStackEntry.toRoute<EquiposRoute>()
                            val idDeLaLiga = args.id.toString()

                            PantallaEquipos(
                                ligaId = idDeLaLiga,
                                onBackClick = {
                                    nav.popBackStack()
                                },
                                onSiguienteClick = {
                                    nav.navigate(JugadoresRoute)
                                },
                                onNuevoEquipoClick = {
                                    nav.navigate(RegistroEquiposRoute)
                                }
                            )
                        }

                        // 8. DETALLE JUGADOR
                        composable<DetallesJugadorRoute> { backStackEntry ->
                            val args = backStackEntry.toRoute<DetallesJugadorRoute>()

                            PantallaDetallesJugador (
                                jugadorId = args.id,
                                onBackClick = { nav.popBackStack() },
                            )
                        }


                        // 9. SELECCIÓN DE LIGA (PARA ESTADÍSTICAS)
                        composable<SeleccionLigaParaEstadisticasRoute> {
                            val ligasViewModel: LigasViewModel = viewModel()

                            PantallaSeleccionLiga(
                                viewModel = ligasViewModel,
                                onLigaClick = { idString ->
                                    val idInt = idString.toIntOrNull() ?: 1
                                    nav.navigate(EstadisticasDetalleRoute(id = idInt))
                                }
                            )
                        }

                        // 10. PANTALLA DE ESTADÍSTICAS
                        composable<EstadisticasDetalleRoute> { backStackEntry ->
                            val args = backStackEntry.toRoute<EstadisticasDetalleRoute>()

                            val apiService = remember { ApiServiceFactory.create() }
                            val statsViewModel = remember { EstadisticasViewModel(apiService) }

                            LaunchedEffect(args.id) {
                                statsViewModel.cargarDatos(ligaId = args.id, temporada = "2025-2026")
                            }

                            EstadisticasScreen(
                                viewModel = statsViewModel,
                                onBack = { nav.popBackStack() }
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}