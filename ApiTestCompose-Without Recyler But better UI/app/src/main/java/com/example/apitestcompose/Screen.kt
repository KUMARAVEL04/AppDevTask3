package com.example.apitestcompose

sealed class Screen(val route:String){
    object loginScreen : Screen("login")
    object registerScreen : Screen("register")
    object userScreen : Screen("user")
    object taskCreationScreen : Screen("taskCreate")
    object taskScreen : Screen("taskView")
    object hallOfFame : Screen("hallOfFame")

    object updateScreen : Screen("updateScreen")

    object taskDashboard : Screen("taskDashboard")


    fun withArgs(vararg args:String):String{
        return buildString {
            append(route)
            args.forEach {
                arg->
                append("/$arg")
            }
        }
    }
}