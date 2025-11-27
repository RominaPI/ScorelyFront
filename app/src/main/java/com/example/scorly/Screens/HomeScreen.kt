package com.example.scorly.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scorly.Navigation.LoginRoute
import com.example.scorly.R
import com.example.scorly.ui.theme.ScorlyTheme

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Text(
            text = "EMPEZAR",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .border(1.dp, Color.White, RoundedCornerShape(22.dp))
                .padding(horizontal = 32.dp, vertical = 12.dp)
                .clickable {
                    navController.navigate(LoginRoute)

                }
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 320.dp)
        ) {
            Text(
                text = "we didn´t",
                fontWeight = FontWeight.W300,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = "COME THIS FAR",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 300.dp)
        ) {
            Text(
                text = "to just",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "COME THIS FAR",
                fontWeight = FontWeight.W300,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Text(
            text = "COME THIS FAR",
            fontWeight = FontWeight.Light,
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 10.dp, top = 275.dp)
        )

        Text(
            text = "SCORLY",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 10.dp, top = 575.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ScorlyTheme {
        val fakeNavController = rememberNavController()
        HomeScreen(fakeNavController)
    }
}
