package com.example.apitestcompose.ui.theme

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.apitestcompose.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.apitestcompose.RetrofitInstance
import com.example.apitestcompose.Screen
import com.example.apitestcompose.TaskResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


@Composable
fun UserPage(modifier: Modifier= Modifier
    .fillMaxSize()
    .background(colorResource(id = R.color.light_blue_200)),userName: String,karmax:Int,navController: NavController){
    val localDensity = LocalDensity.current
    var karma by remember {
        mutableStateOf(karmax)
    }
    val coroutineScope = rememberCoroutineScope()
    var columnHeightPx by remember {
        mutableStateOf(0f)
    }
    var responses by remember {
        mutableStateOf<List<TaskResponse>?>(null)
    }
    var columnHeightSp by remember {
        mutableStateOf(0.sp)
    }
    var columnWidthPx by remember {
        mutableStateOf(0f)
    }
    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }
    var columnWidthDp by remember {
        mutableStateOf(0.dp)
    }
    var restart by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val response2 = RetrofitInstance.api.getKarma(userName)
            println("New Karma:${response2.isSuccessful}")
            if(response2.isSuccessful){
                karma=response2.body()!!
            }
            val response = RetrofitInstance.api.getTasks(userName)
            println("Success? ${response.isSuccessful}")
            if(response.isSuccessful && ! response.body().isNullOrEmpty()){
                responses = response.body()
            }
        }
    }


    Column(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                columnHeightPx = coordinates.size.height.toFloat()
                columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                columnHeightSp = with(localDensity) { coordinates.size.height.toSp() }
                columnWidthPx = coordinates.size.width.toFloat()
                columnWidthDp = with(localDensity) { coordinates.size.width.toDp() }
            }
    ) {
        Box(modifier = Modifier,){
            //background
            Column {
                //upper blue
                Row(modifier= Modifier
                    .height(columnHeightDp / 4)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween ) {
                    Column(modifier= Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(bottomEndPercent = 45))
                        .background(Color.White)
                        .padding(9.dp)
                        .clickable {
                            navController.navigate(Screen.taskDashboard.route + "/${userName}")
                        }
                        , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(id = R.drawable.task_submit), contentDescription = "", contentScale = ContentScale.FillBounds)
                        Text(text = "Active", fontSize = 15.sp, color = colorResource(id = R.color.dark_blue_200))
                    }
                    Column(modifier= Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(bottomStartPercent = 45))
                        .background(Color.White)
                        .padding(9.dp)
                        .clickable {
                            navController.navigate(Screen.taskScreen.route + "/${userName}")
                        }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                        Image(painterResource(id = R.drawable.tasks_nav), contentDescription = "", contentScale = ContentScale.FillBounds)
                        Text(text = "Tasks", fontSize = 15.sp, color = colorResource(id = R.color.dark_blue_200))
                    }
                }
                //lower white
                Column(modifier= Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))
                    .background(colorResource(id = R.color.dark_blue_200))
                    .padding(top = columnHeightDp / 8)
                    , horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {

                    //text
                    Text(text = userName, fontSize = columnHeightSp/15, color = Color.White)

                    //task_panel
                    Column(modifier= Modifier
                        .padding(25.dp, 10.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(percent = 10))
                        .background(
                            color = colorResource(
                                id = R.color.light_blue_200
                            )
                        )
                        .verticalScroll(rememberScrollState())
                        .weight(1f, fill = false)
                        .padding(25.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if(!responses.isNullOrEmpty()){
                            for(i in responses!!){
                                @Composable
                                fun TaskPanel(modifier: Modifier= Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),taskResponse: TaskResponse){
                                    val taskTitle = taskResponse.title
                                    val underinspection = taskResponse.underinspection
                                    val karma = taskResponse.karma
                                    val reservedName = taskResponse.reservename
                                    val localDensity = LocalDensity.current

                                    var columnHeightPx by remember {
                                        mutableStateOf(0f)
                                    }
                                    var columnHeightDp by remember {
                                        mutableStateOf(0.dp)
                                    }
                                    var columnHeightSp by remember {
                                        mutableStateOf(0.sp)
                                    }
                                    var columnWidthPx by remember {
                                        mutableStateOf(0f)
                                    }
                                    var columnWidthDp by remember {
                                        mutableStateOf(0.dp)
                                    }

                                    val coroutineScope = rememberCoroutineScope()
                                    Column(modifier= Modifier
                                        .clip(RoundedCornerShape(15))
                                        .background(colorResource(id = R.color.dark_blue_200))
                                        .padding(5.dp)
                                        .onGloballyPositioned { coordinates ->
                                            columnHeightPx = coordinates.size.height.toFloat()
                                            columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                                            columnHeightSp = with(localDensity) { coordinates.size.height.toSp() }
                                            columnWidthPx = coordinates.size.width.toFloat()
                                            columnWidthDp = with(localDensity) { coordinates.size.width.toDp() }
                                        }
                                        .clickable {
                                            if (underinspection) {
                                                coroutineScope.launch(Dispatchers.IO) {
                                                    val finishResponse = RetrofitInstance.api.completeTask(taskResponse)
                                                    if (finishResponse.isSuccessful && !finishResponse
                                                            .body()
                                                            .isNullOrEmpty()
                                                    ) {
                                                        @Composable
                                                        fun Refresh(navController: NavController){
                                                        }
                                                        println("Completed Message : ${finishResponse.body()}")
                                                    }
                                                }
                                            }
                                        }) {

                                        var color1 = if(underinspection){Color.Green}else{Color.Red}
                                        Text(text = taskTitle, color = color1, modifier = Modifier.padding(5.dp))
                                        Box(modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(Color.White))
                                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(text ="Karma: ${karma}", color = Color.White, modifier = Modifier.padding(5.dp))
                                            if(reservedName.isNullOrBlank()){
                                                Text(text ="Reserved by: None", color = Color.White, modifier = Modifier.padding(5.dp))
                                            }
                                            else{
                                                Text(text ="Reserved by: ${reservedName}", color = Color.White, modifier = Modifier.padding(5.dp))
                                            }
                                        }
                                    }
                                }
                                TaskPanel(modifier = Modifier,i)
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }

                    }

                    //button
                    Button(onClick =
                    {
                        println("karma:$karma")
                    navController.navigate(Screen.taskCreationScreen.route+ "/${userName}/${karma}")
                    },modifier= Modifier
                        .padding(25.dp, 0.dp)
                        .fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = colorResource(id = R.color.dark_blue_200),
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.LightGray
                    )) {
                        Image(painter = painterResource(id = R.drawable.add), contentDescription ="Add", contentScale = ContentScale.FillBounds, colorFilter = ColorFilter.tint(color = colorResource(id = R.color.dark_blue_200)))
                    }
                }
            }
            //circle
            Column(modifier = Modifier
                .padding(
                    top = (columnHeightDp / 8),
                    start = columnWidthDp / 2 - (columnHeightDp / 8)
                )
                .size((columnHeightDp / 4))
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(
                    colorResource(id = R.color.Ring)
                )
                .padding(10.dp)
                .clip(CircleShape)
                .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Text(text = karma.toString(), fontSize = columnHeightSp/12, maxLines = 1, color = colorResource(id = R.color.light_blue_200))
                Text(text = "KARMA", fontSize = columnHeightSp/35, color = colorResource(id = R.color.light_blue_200), maxLines = 1, fontWeight = FontWeight.Bold)

            }
        }
    }

}

