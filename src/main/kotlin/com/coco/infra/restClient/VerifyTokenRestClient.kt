package com.coco.infra.restClient

import com.coco.domain.model.User
import com.coco.infra.client.VerifyTokenClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.eclipse.microprofile.rest.client.inject.RestClient


/**
@author Yu-Jing
@create 2024-08-15-下午 02:09
 */
@Named("rest-client")
@ApplicationScoped
class VerifyTokenRestClient @Inject constructor(
    @RestClient
    private val client: IVerifyTokenRestClient
): VerifyTokenClient {
    override fun verifyToken(token: String): Uni<User?> {
        return client.verifyToken(token)
    }
}


@Path("/jwt/verify")
@RegisterRestClient(configKey = "jwt-client-api")
@RequestScoped
interface IVerifyTokenRestClient {
    @GET
    fun verifyToken(@QueryParam("token") token: String): Uni<User?>
}
