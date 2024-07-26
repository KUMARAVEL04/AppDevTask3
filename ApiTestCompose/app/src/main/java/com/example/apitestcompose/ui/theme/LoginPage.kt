package com.example.apitestcompose.ui.theme

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.apitestcompose.R
import com.example.apitestcompose.RetrofitInstance
import com.example.apitestcompose.Screen
import com.example.apitestcompose.UserName
import com.example.apitestcompose.UserResponse
import com.example.apitestcompose.fontJockey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOError


@Composable
fun LoginPage(navController: NavController,modifier: Modifier = Modifier
    .fillMaxSize()
    .background(colorResource(id = R.color.dark_blue_200))
){

    val lineHeightSp: TextUnit = 25.sp
    val lineHeightDp: Dp = with(LocalDensity.current) {
        lineHeightSp.toDp()
    }
    val coroutinescope = rememberCoroutineScope()
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var dialogShow by remember {
        mutableStateOf(false)
    }
    var url by remember {
        mutableStateOf("")
    }
    if(dialogShow){
        AlertDialog(
            onDismissRequest = {
                               dialogShow=false
            },
            confirmButton = {
                TextButton(onClick = {
                    RetrofitInstance.urlText=url
                    println("Url:${RetrofitInstance.urlText}")
                    dialogShow=false
                }) {
                    Text("OK")
                }
            },
            title = {
                Text(text = "Alert")
            },
            text = {
                Column {
                    Text("Server might have not been deployed, please use ADB to access the app")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
    Box(modifier = modifier){
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Column (modifier= Modifier.wrapContentSize(Alignment.Center)){
                Text(text = "MERIT", fontFamily = fontJockey, maxLines = 1,modifier= Modifier.align(
                    Alignment.Start), fontSize = 80.sp, color = Color.White)
                Text(text = "MATCH", fontFamily = fontJockey, maxLines = 1,color = Color.White,modifier= Modifier
                    .align(Alignment.End)
                    .padding(70.dp, 0.dp, 0.dp, 0.dp), fontSize = 80.sp)
            }
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
                    if (username.isBlank() || password.isBlank()) {
                        Toast
                            .makeText(context, "No Username/Password", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        var userBody = UserName(username.toString(), password.toString())
                        coroutinescope.launch() {
                            try {
                                val response = withContext(Dispatchers.IO) {
                                    RetrofitInstance.api.loginCheck(userBody)
                                }
                                if (!response.isSuccessful) {
                                    println("Successful")
                                    val errorMessage = response
                                        .errorBody()
                                        ?.string()
                                    if (errorMessage?.contains("No Account") == true) {
                                        Toast
                                            .makeText(context, "No Account", Toast.LENGTH_SHORT)
                                            .show()
                                        println("Already exists")
                                    } else {
                                        println("Error")
                                        Toast
                                            .makeText(
                                                context,
                                                "Error: ${response.code()}",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                } else {
                                    println("Not Successful")
                                    Toast
                                        .makeText(
                                            context,
                                            "Login Success",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    navController.navigate(Screen.userScreen.route + "/${response.body()!!.username}/${response.body()!!.karma}")

                                }
                            } catch (e: Exception) {
                                Toast
                                    .makeText(
                                        context,
                                        "Network Error, Try Again",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                dialogShow = true
                            } catch (e: Error) {
                                Toast
                                    .makeText(
                                        context,
                                        "Network Error, Try Again",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                dialogShow = true
                            } catch (e:HttpException){
                                Toast
                                    .makeText(
                                        context,
                                        "Network Error, Try Again",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                dialogShow = true
                            }
                        }

                    }
                },
            contentAlignment = Alignment.Center
        ){
            Text(text = "Login", color = colorResource(id = R.color.dark_blue_200), letterSpacing = 5.sp, fontSize = lineHeightSp, maxLines = 1,modifier= Modifier
                .requiredHeight(lineHeightDp * 2)
                .padding(5.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ApiTestComposeTheme {
//        LoginPage(modifier = Modifier
//            .fillMaxSize()
//            .background(colorResource(id = R.color.dark_blue_200)))
//    }
//}
