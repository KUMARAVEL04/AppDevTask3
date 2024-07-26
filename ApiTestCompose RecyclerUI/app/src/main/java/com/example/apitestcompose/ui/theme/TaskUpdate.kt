package com.example.apitestcompose.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.apitestcompose.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.max
import com.example.apitestcompose.RetrofitInstance
import com.example.apitestcompose.Screen
import com.example.apitestcompose.TaskResponse
import com.example.apitestcompose.TaskSchema
import com.example.apitestcompose.UpdateSchema
import com.example.apitestcompose.UserName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TaskUpdate(modifier: Modifier = Modifier.fillMaxSize(),karma:Int,maxKarma: Int,username: String,description: String,title: String,taskid:Int,navController: NavController){
    val context = LocalContext.current
    println("maxkarma:$maxKarma")
    Column {
        var  username by remember {
            mutableStateOf(username)
        }
        var  description by remember {
            mutableStateOf(description)
        }
        var  title by remember {
            mutableStateOf(title)
        }
        var  karma by remember {
            mutableStateOf(karma)
        }
        var coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.light_blue_200))
                .padding(5.dp,0.dp)
        ){
            Text(text = "Update Task", color = Color.White, modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp), fontSize = 25.sp)
            Button(onClick = { navController.navigateUp()    }, modifier = Modifier.align(Alignment.CenterEnd),colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.dark_blue_200),
                contentColor = Color.White
            )) {
                Text(text = "Back")
            }
        }
        Column(modifier= Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.dark_blue_200))
            .padding(25.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            TextField(
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor =  colorResource(id = R.color.dark_blue_200 ),
                    focusedTextColor =  colorResource(id = R.color.dark_blue_200 ),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.Gray),
                value = username,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                onValueChange = {
                    username=it
                },
                readOnly = true,
                label = { Text(text = "Username", color = colorResource(id = R.color.dark_blue_200 )) },
            )
            TextField(
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor =  colorResource(id = R.color.dark_blue_200 ),
                    focusedTextColor =  colorResource(id = R.color.dark_blue_200 ),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.Gray),
                value = title,
                onValueChange = {
                    title=it
                },
                maxLines = 1,
                label = { Text(text = "Title", color = colorResource(id = R.color.dark_blue_200 )) },
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
            )
            TextField(
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor =  colorResource(id = R.color.dark_blue_200 ),
                    focusedTextColor =  colorResource(id = R.color.dark_blue_200 ),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.Gray),
                value = description,
                singleLine = false,
                onValueChange = {
                    description=it
                },
                label = { Text(text = "Description", color = colorResource(id = R.color.dark_blue_200 )) },
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
            Column(modifier= Modifier
                .padding(5.dp)
                .fillMaxWidth()) {
                Text(text = "Karma", color = Color.White,modifier= Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidthPx = 1.dp.toPx()
                        val verticalOffset = size.height - 1.sp.toPx()
                        drawLine(
                            color = Color.White,
                            strokeWidth = strokeWidthPx,
                            start = Offset(0f, verticalOffset),
                            end = Offset(size.width, verticalOffset)
                        )
                    })
                Row(modifier= Modifier
                    .clip(RoundedCornerShape(5, 5, 0, 0))
                    .padding(2.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    val localDensity = LocalDensity.current
                    var columnHeightDp by remember {
                        mutableStateOf(0.dp)
                    }
                    Text(text=karma.toString(),modifier= Modifier
                        .weight(1f)
                        .background(Color.White)
                        .onGloballyPositioned { coordinates ->
                            columnHeightDp = with(localDensity) {
                                coordinates.size.height
                                    .toFloat()
                                    .toDp()
                            }
                        }
                        .padding(12.dp)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Column(modifier=Modifier.weight(1f)) {
                        Box(modifier= Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(20))
                            .background(Color.White)
                            .height(columnHeightDp / 2)
                            .clickable {
                                if (karma < maxKarma) {
                                    karma += 1
                                }
                            }) {
                            Image(painter = painterResource(id = R.drawable.up_drop_arrow), contentDescription = "up")
                        }
                        Box(modifier= Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(20))
                            .background(Color.White)
                            .height(columnHeightDp / 2)
                            .clickable {
                                if (karma > 1) {
                                    karma -= 1
                                }
                            }) {
                            Image(painter = painterResource(id = R.drawable.drop_arrow), contentDescription ="down" )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val response = RetrofitInstance.api.updateTask(UpdateSchema(username,description,title,karma,taskid))
                        println("TaskUpdate: ${response.isSuccessful}")
                        if(response.isSuccessful){
                            response.body()?.let {
                                withContext(Dispatchers.Main){
                                    Toast
                                        .makeText(context, response.body(), Toast.LENGTH_SHORT)
                                        .show()
                                    navController.navigateUp()
                                }
                            }
                        }
                    }},modifier=Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.light_blue_200),
                    contentColor = Color.White
                )) {
                Text(text = "Update Task")
            }
        }
    }
}