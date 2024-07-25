package com.example.apitestcompose.ui.theme

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apitestcompose.R
import com.example.apitestcompose.fontJockey

@Composable
fun HomePage(navController: NavController, modifier: Modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.dark_blue_200))){
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
        Column (modifier= Modifier.wrapContentSize(Alignment.Center)){
            Text(text = "MERIT", fontFamily = fontJockey, maxLines = 1,modifier= Modifier.align(
                Alignment.Start), fontSize = 80.sp, color = Color.White)
            Text(text = "MATCH", fontFamily = fontJockey, maxLines = 1,color = Color.White,modifier= Modifier
                .align(Alignment.End)
                .padding(70.dp, 0.dp, 0.dp, 0.dp), fontSize = 80.sp)
        }
        Column {
            Button(onClick = { /*TODO*/ },modifier= Modifier.size(115.dp,50.dp).padding(5.dp)) {
                Text(text = "Login")
            }
            Button(onClick = { /*TODO*/ },modifier= Modifier.size(115.dp,50.dp).padding(5.dp)) {
                Text(text = "Register")
            }
        }
        Image(painter = painterResource(id = R.drawable.volunteers_team_working_vector_1), contentDescription ="",modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .aspectRatio(1f)
            .fillMaxWidth(),
            contentScale = ContentScale.Crop )
    }
}
