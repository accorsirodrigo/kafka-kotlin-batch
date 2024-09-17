package com.example


import com.example.modules.ModuloExemplo
import com.example.plugins.configureMonitoring
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.server.application.Application
import java.time.Duration
import java.util.Properties
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module


suspend fun main(args: Array<String>) {

    startKoin {
        printLogger()
        modules(
            ModuloExemplo().module,
        )
    }

    CoroutineScope(Dispatchers.IO).launch {
        val props = Properties()
        props["bootstrap.servers"] = "localhost:9092"
        props["group.id"] = "my-consumer-group"
        props["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        props["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"

        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"

        val consumer = KafkaConsumer<String, String>(props)
        consumer.subscribe(listOf("my-topic"))

        val producer = KafkaProducer<String, String>(props)

        launch {
            while (true) {
                producer.send(ProducerRecord("my-topic", "${UUID.randomUUID()}", "teste"))
                producer.flush()
                delay(5000)
            }
        }

        launch {
            while (true) {
                val records = consumer.poll(Duration.ofMillis(100))
                if (!records.isEmpty) {
                    records.forEach { record ->
                        println("Received message: ${record.value()}")
                        consumer.committed(setOf(TopicPartition(record.topic(), record.partition())), Duration.ofMillis(10))
                    }
                }
                delay(1000)
            }
        }

    }

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
