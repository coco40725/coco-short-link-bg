package com.coco.infra.grpc

import com.coco.domain.model.User
import com.coco.infra.client.VerifyTokenClient
import com.coco.infra.exception.GrpcConnectionException
import com.cocodev.grpc.verifyToken.VerifyTokenRequest
import com.cocodev.grpc.verifyToken.VerifyTokenSvc
import io.quarkus.grpc.GrpcClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.inject.Named

/**
@author Yu-Jing
@create 2024-08-20-上午 10:04
 */

@ApplicationScoped
@Named("grpc")
class VerifyTokenGrpc @Inject constructor(
    @GrpcClient("verify-token")
    val client: VerifyTokenSvc

): VerifyTokenClient {

    private val className = this::class.simpleName
    override fun verifyToken(token: String): Uni<User?> {
        val request = VerifyTokenRequest.newBuilder().setToken(token).build()
        return client.verifyToken(request)
            .map { User(id = it.id, name = it.name, email = it.email, password = null) }
            .onFailure().recoverWithUni  { throwable ->
                Uni.createFrom().failure(GrpcConnectionException(className, this::verifyToken.name, throwable.message ?: "Unknown"))
            }
    }
}