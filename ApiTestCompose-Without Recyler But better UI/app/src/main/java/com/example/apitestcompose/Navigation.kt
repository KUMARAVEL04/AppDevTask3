package com.example.apitestcompose

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apitestcompose.ui.theme.LoginPage
import com.example.apitestcompose.ui.theme.RegisterPage
import com.example.apitestcompose.ui.theme.TaskCreate
import com.example.apitestcompose.ui.theme.TaskHOF
import com.example.apitestcompose.ui.theme.TaskUpdate
import com.example.apitestcompose.ui.theme.TaskView
import com.example.apitestcompose.ui.theme.UserPage
import com.example.apitestcompose.ui.theme.YourTaskView

@Composable
    fun Navigation(modifier: Modifier = Modifier,firstTime:Boolean,shared: SharedPreferences) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = if(firstTime){Screen.registerScreen.route}else{Screen.loginScreen.route} ){
        composable(route=Screen.loginScreen.route){
            LoginPage(navController)
        }

        composable(route=Screen.registerScreen.route){
            RegisterPage(navController=navController,shared=shared)
        }

        composable(route=Screen.taskScreen.route+"/{username}",arguments = listOf(
            navArgument("username"){
                type = NavType.StringType
            }
        )){entry->
            TaskView(username=entry.arguments?.getString("username")!!, navController = navController)
        }

        composable(route=Screen.taskDashboard.route+"/{username}",arguments = listOf(
            navArgument("username"){
                type = NavType.StringType
            }
        )){entry->
            YourTaskView(username=entry.arguments?.getString("username")!!, navController = navController)
        }

        composable(route=Screen.hallOfFame.route+"/{username}",arguments = listOf(
            navArgument("username"){
                type = NavType.StringType
            }
        )){entry->
            TaskHOF(username=entry.arguments?.getString("username")!!, navController = navController)
        }

        composable(
            route=Screen.userScreen.route+"/{username}/{karma}",
            arguments = listOf(
                navArgument("username"){
                    type = NavType.StringType
                },
                navArgument("karma"){
                    type = NavType.IntType
                }
            )
            ){entry->
            var name = entry.arguments?.getString("username") ?:"Default"
            var karma = entry.arguments?.getInt("karma")?:0
            UserPage( userName = name , karmax = karma ,navController=navController)
        }

        composable(route=Screen.taskCreationScreen.route+"/{username}/{maxkarma}",arguments = listOf(
            navArgument("username"){
                type = NavType.StringType
            },
            navArgument("maxkarma"){
                type = NavType.IntType
            }
        )){entry->
            TaskCreate(maxKarma = entry.arguments?.getInt("maxkarma")?:0,username = entry.arguments?.getString("username")?:"Default", navController = navController)
        }

        composable(route=Screen.updateScreen.route+"/{username}/{maxkarma}/{karma}/{title}/{description}/{taskid}",arguments = listOf(
            navArgument("username"){
                type = NavType.StringType
            },
            navArgument("maxkarma"){
                type = NavType.IntType
            },
            navArgument("karma"){
                type = NavType.IntType
            },
            navArgument("title"){
                type = NavType.StringType
            },
            navArgument("description"){
                type = NavType.StringType
            },
            navArgument("taskid"){
                type = NavType.IntType
            }
        )){entry->
            TaskUpdate(maxKarma = entry.arguments?.getInt("maxkarma")?:0,
                username = entry.arguments?.getString("username")?:"Default",
                navController = navController,
                title = entry.arguments?.getString("title")?:"",
                description = entry.arguments?.getString("description")?:"",
                taskid = entry.arguments?.getInt("taskid")?:0,
                karma = entry.arguments?.getInt("karma")?:1)
        }
    }
}
