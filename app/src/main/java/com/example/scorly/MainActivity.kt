package com.example.scorly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scorly.Navigation.HomeScreenRoute
import com.example.scorly.Navigation.JugadoresRoute
import com.example.scorly.Navigation.LoginRoute
import com.example.scorly.Navigation.PrincipalRoute
import com.example.scorly.Navigation.SignUpRoute
import com.example.scorly.Screens.Login
import com.example.scorly.Screens.PaginaPrincipal
import com.example.scorly.Screens.PantallaJugadores
import com.example.scorly.Screens.SignUp
import com.example.scorly.ui.theme.ScorlyTheme
import androidx.navigation.toRoute


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

