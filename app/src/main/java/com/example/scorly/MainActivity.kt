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
import com.example.Scorly.Models.Equipo
import com.example.Scorly.Navigation.EquiposRoute
import com.example.Scorly.Navigation.HomeScreenRoute
import com.example.Scorly.Navigation.JugadoresRoute
import com.example.Scorly.Navigation.LoginRoute
import com.example.Scorly.Navigation.PrincipalRoute
import com.example.Scorly.Navigation.SignUpRoute
import com.example.Scorly.Screens.Login
import com.example.Scorly.Screens.PaginaPrincipal
import com.example.Scorly.Screens.PantallaJugadores
import com.example.Scorly.Screens.SignUp
import com.example.Scorly.Screens.HomeScreen
import com.example.Scorly.Screens.PantallaEquipos
import com.example.Scorly.ui.theme.ScorlyTheme


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
                        composable<HomeScreenRoute> {
                            HomeScreen(navController = nav)
                        }
                        composable<JugadoresRoute>{
                            PantallaJugadores()
                        }
                        composable<LoginRoute> {
                            Login(navController = nav)
                        }
                        composable<PrincipalRoute> {
                            PaginaPrincipal(nav)
                        }
                        composable<SignUpRoute>{
                            SignUp(nav)
                        }
                        composable<EquiposRoute> {
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

