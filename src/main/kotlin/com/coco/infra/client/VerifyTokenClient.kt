package com.coco.infra.client

import com.coco.domain.model.User
import io.smallrye.mutiny.Uni

/**
@author Yu-Jing
@create 2024-08-20-上午 10:41
 */
interface VerifyTokenClient {
    fun verifyToken(token: String): Uni<User?>
}