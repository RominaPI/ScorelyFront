package com.example.scorly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.scorly.Navigation.*
import com.example.scorly.Screens.Login
import com.example.scorly.Screens.PaginaPrincipal
import com.example.scorly.Screens.PantallaJugadores
import com.example.scorly.Screens.SignUp
import com.example.scorly.Screens.HomeScreen
import com.example.scorly.Screens.PantallaEquipos
import com.example.scorly.Screens.PantallaSeleccionLiga
import com.example.scorly.ui.theme.ScorlyTheme

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
                        startDestination = HomeScreenRoute
                    ) {

                        // 1. HOME
                        composable<HomeScreenRoute> {
                            HomeScreen(navController = nav)
                        }

                        // 2. JUGADORES
                        composable<JugadoresRoute>{
                            PantallaJugadores()
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


                        // 6. LIGAS Y EQUIPOS
                        composable<LigasRoute> {
                            PantallaSeleccionLiga(
                                onLigaClick = { idLiga ->
                                    nav.navigate(EquiposRoute(id = idLiga))
                                }
                            )
                        }

                        composable<EquiposRoute> { backStackEntry ->
                            val args = backStackEntry.toRoute<EquiposRoute>()
                            val idDeLaLiga = args.id


                            PantallaEquipos(
                                onEquipoClick = { equipo ->
                                    nav.navigate(JugadoresRoute)
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}