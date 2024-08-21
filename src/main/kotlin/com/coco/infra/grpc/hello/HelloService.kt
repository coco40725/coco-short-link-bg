package com.coco.infra.grpc.hello

import io.quarkus.example.Greeter
import io.quarkus.example.HelloReply
import io.quarkus.example.HelloRequest
import io.quarkus.grpc.GrpcClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
@author Yu-Jing
@create 2024-08-19-下午 02:05
 */

@ApplicationScoped
class HelloService @Inject constructor(
    @GrpcClient("hello")
    val client: Greeter
) {

    fun sayHelloToServer(name: String): Uni<String> {
        return client.sayHello(HelloRequest.newBuilder().setName(name).build())
            .onItem().transform { helloReply: HelloReply -> helloReply.message }
    }
}