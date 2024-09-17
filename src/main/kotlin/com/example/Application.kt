package com.example


import com.example.UserService.UserApplication
import com.example.modules.ModuloExemplo
import com.example.plugins.configureMonitoring
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.server.application.Application
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module


fun main(args: Array<String>) {

    startKoin {
        printLogger()
        modules(
            ModuloExemplo().module,
        )
    }

    UserApplication().sayHello()

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
