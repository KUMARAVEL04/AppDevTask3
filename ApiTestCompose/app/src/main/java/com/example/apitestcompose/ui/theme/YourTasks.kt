package com.example.apitestcompose.ui.theme

import android.app.ActivityManager.TaskDescription
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apitestcompose.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.apitestcompose.Inter
import com.example.apitestcompose.ReserveBody
import com.example.apitestcompose.RetrofitInstance
import com.example.apitestcompose.TaskResponse
import com.example.apitestcompose.jost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//
//@Preview(showBackground = true)
//@Composable
//fun Tasks(){
//        TaskView(username="string")
//}

@Composable
fun YourTaskView(modifier: Modifier= Modifier
    .fillMaxSize()
    .background(colorResource(id = R.color.dark_blue_200)),username:String,navController: NavController
){
    Column{
        var list_number by remember {
            mutableStateOf(0)
        }
        var maxTask by remember {
            mutableStateOf(0)
        }
        var titleText by remember {
            mutableStateOf("Hi")
        }
        var descriptionText by remember {
            mutableStateOf("There might be a connection error or no new tasks are available yet. Please try again later")
        }
        var karma by remember {
            mutableStateOf(0)
        }
        var list_of_tasks = remember {
            mutableStateOf(listOf<TaskResponse>())
        }
        val coroutineScope = rememberCoroutineScope()
        var restart by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(restart) {
            withContext(Dispatchers.IO){
                val response =RetrofitInstance.api.getYourTasks(username)
                println("Tasks Response : ${response.isSuccessful} Response: ${response.body()}")
                if(response.isSuccessful){
                    maxTask=response.body()!!.size
                    if(maxTask>0){
                        maxTask-=1
                    }
                    list_of_tasks.value=response.body()!!
                    if(!list_of_tasks.value.isNullOrEmpty()){
                        titleText=list_of_tasks.value[0].title
                        descriptionText=list_of_tasks.value[0].description
                        karma=list_of_tasks.value[0].karma
                    }
                    else{
                        titleText="Hi"
                        descriptionText="There might be a connection error or no new tasks are available yet. Please try again later"
                        karma=0
                    }
                }
            }
        }
        Row(modifier=modifier.weight(0.9f)) {
            Column(modifier= Modifier
                .weight(0.22f)
                .fillMaxHeight()
                .background(colorResource(id = R.color.light_blue_200))
                .padding(10.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                Column(modifier= Modifier
                    .clip(RoundedCornerShape(25))
                    .background(colorResource(id = R.color.dark_blue_200))
                    .padding(10.dp)
                    .aspectRatio(1f)
                    .clickable {
                        if (list_number > 0) {
                            list_number -= 1
                            titleText = list_of_tasks.value[list_number].title
                            descriptionText = list_of_tasks.value[list_number].description
                            karma = list_of_tasks.value[list_number].karma
                        }
                    }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(painter = painterResource(id = R.drawable.up_drop_arrow), contentDescription = "up", tint = Color.White)
                    Text("PREV", color = Color.White, fontFamily = jost)
                }
                Column(modifier= Modifier
                    .clip(RoundedCornerShape(25))
                    .background(colorResource(id = R.color.dark_blue_200))
                    .padding(10.dp)
                    .aspectRatio(1f)
                    .clickable {
                        if(!list_of_tasks.value.isNullOrEmpty()){
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    val response2 = RetrofitInstance.api.submitTask(
                                        ReserveBody(list_of_tasks.value[list_number].taskid,list_of_tasks.value[list_number].reservename)
                                    )
                                    println("Reserve ${response2.isSuccessful} ${response2.body()}")
                                    if (response2.isSuccessful) {
                                        if (response2.body() != null) {
                                            restart=!restart
                                        }
                                    }
                                }
                            }
                        }
                    }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(painter = painterResource(id = R.drawable.add), contentDescription = "up", tint = Color.White)
                    Text("SUBMIT", color = Color.White, fontFamily = jost)
                }
                Column(modifier= Modifier
                    .clip(RoundedCornerShape(25))
                    .background(colorResource(id = R.color.dark_blue_200))
                    .padding(10.dp)
                    .aspectRatio(1f)
                    .clickable {
                        if (list_number < maxTask) {
                            list_number += 1
                            titleText = list_of_tasks.value[list_number].title
                            descriptionText = list_of_tasks.value[list_number].description
                            karma = list_of_tasks.value[list_number].karma
                            println("karma = ${list_of_tasks.value[list_number].karma}, Title = ${list_of_tasks.value[list_number].title}, task")
                        }
                    }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("NEXT", color = Color.White, fontFamily = jost)
                    Icon(painter = painterResource(id = R.drawable.drop_arrow), contentDescription = "up", tint = Color.White)
                }

            }
            Column(modifier= Modifier
                .weight(0.7f)
                .fillMaxSize()){
                TaskDesc(modifier=Modifier.padding(15.dp), titleText = titleText, karma = karma, descriptionText = descriptionText)
            }
        }
        Column(modifier = Modifier
            .weight(0.06f)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                navController.navigateUp()
            }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Image(painterResource(id = R.drawable.baseline_account_circle_24), contentDescription = "", contentScale = ContentScale.FillBounds)
            Text(text = "BACK", fontSize = 15.sp, color = colorResource(id = R.color.dark_blue_200))
        }
    }


}

