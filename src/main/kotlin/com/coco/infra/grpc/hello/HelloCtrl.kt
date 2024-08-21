package com.coco.infra.grpc.hello

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

/**
@author Yu-Jing
@create 2024-08-19-下午 02:50
 */
@Path("/test")
class HelloCtrl @Inject constructor(
    private val service: HelloService
){

    @GET
    @Path("/{name}")
    fun hello(name: String?): Uni<String> {
        println("HelloCtrl hello!!")
        return service.sayHelloToServer(name!!)
    }

}