package com.example.apitestcompose.ui.theme

import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apitestcompose.R
import com.example.apitestcompose.RetrofitInstance
import com.example.apitestcompose.Screen
import com.example.apitestcompose.UserName
import com.example.apitestcompose.fontJockey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun RegisterPage(navController: NavController,modifier: Modifier = Modifier.fillMaxSize()
    .background(colorResource(id = R.color.dark_blue_200)),shared:SharedPreferences){
    val lineHeightSp: TextUnit = 25.sp
    val lineHeightDp: Dp = with(LocalDensity.current) {
        lineHeightSp.toDp()
    }
    val coroutinescope = rememberCoroutineScope()
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    Box(modifier = modifier){
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Column (modifier= Modifier.wrapContentSize(Alignment.Center)){
                Text(text = "MERIT", fontFamily = fontJockey, maxLines = 1,modifier= Modifier.align(
                    Alignment.Start), fontSize = 80.sp, color = Color.White)
                Text(text = "MATCH", fontFamily = fontJockey, maxLines = 1,color = Color.White,modifier= Modifier
                    .align(Alignment.End)
                    .padding(70.dp, 0.dp, 0.dp, 0.dp), fontSize = 80.sp)
            }
            Text(text = "Welcome!", fontFamily = fontJockey, maxLines = 1,color = Color.White,modifier= Modifier.padding(0.dp,20.dp,0.dp,0.dp).drawBehind {
                val strokeWidthPx = 1.dp.toPx()
                val verticalOffset = size.height - 1.sp.toPx()
                drawLine(
                    color = Color.White,
                    strokeWidth = strokeWidthPx,
                    start = Offset(0f, verticalOffset),
                    end = Offset(size.width, verticalOffset)
                )} ,fontSize = 20.sp)
            Column(modifier = Modifier.padding(0.dp,10.dp,0.dp,0.dp), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = Color.LightGray,
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(40),
                    value = username,
                    modifier = Modifier.padding(5.dp),
                    onValueChange = {
                        username=it
                    },
                    label = { Text(text = "Username", color = Color.White) },
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_account_circle_24), contentDescription = "User", tint = Color.White) },
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = Color.LightGray,
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent),
                    shape = RoundedCornerShape(40),
                    visualTransformation = if(passwordVisible){
                        VisualTransformation.None} else{
                        PasswordVisualTransformation()
                    },
                    value = password,
                    modifier = Modifier.padding(5.dp),
                    onValueChange = {
                        password=it
                    },
                    label = { Text(text = "Password", color = Color.White) },
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_adjust_24), contentDescription = "User", tint = Color.White) },
                    trailingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_blur_circular_24), contentDescription = "User", tint = Color.White, modifier = Modifier.clickable { passwordVisible=passwordVisible.not()}) },
                )
                Image(
                    painter = painterResource(id = R.drawable.group),
                    contentDescription = stringResource(id = R.string.group_desc),
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 0.dp, 0.dp)
                        .aspectRatio(1f)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .background(Color.White)
                .clickable {
                    if (username.length < 4 || password.length < 4) {
                        Toast
                            .makeText(context, "No Username/Password", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        var userBody = UserName(username.toString(), password.toString())
                        coroutinescope.launch {
                            val response = withContext(Dispatchers.IO){RetrofitInstance.api.getUser(userBody)}
                            if (!response.isSuccessful) {
                                println("Error")
                                Toast
                                    .makeText(
                                        context,
                                        "Error: ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } else {
                                println("Account")
                                with (shared.edit()) {
                                    putBoolean("First", false)
                                    apply()
                                }

                                navController.navigate(Screen.userScreen.route + "/${response.body()!!.username}/${response.body()!!.karma}")
//                                Toast
//                                    .makeText(
//                                        context,
//                                        "Account Created",
//                                        Toast.LENGTH_SHORT
//                                    )
//                                    .show()
                            }
                        }

                    }
                },
            contentAlignment = Alignment.Center
        ){
            Text(text = "Register", color = colorResource(id = R.color.dark_blue_200), letterSpacing = 5.sp, fontSize = lineHeightSp, maxLines = 1,modifier= Modifier
                .requiredHeight(lineHeightDp*2)
                .padding(5.dp)
            )
        }
    }
}
