package com.example.UserService

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class UserApplication: KoinComponent {

    private val userService: UserService by inject()

    fun sayHello() {
        println("Hello, ${userService.userName()}!")
    }

    fun hello() = "Hello, ${userService.userName()}!"
}