package com.coco.application.service

import com.coco.domain.model.CompensationActions
import com.coco.domain.model.ErrorLog
import com.coco.infra.repo.ErrorLogRepo
import com.coco.infra.util.Log
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.*

/**
@author Yu-Jing
@create 2024-08-27-下午 09:12
 */
@ApplicationScoped
class CompensationService @Inject constructor(
    private val errorLogRepo: ErrorLogRepo
) {
    fun executeCompensation(actions: List<CompensationActions>) {
        val now = Date()
        try {
            actions.forEach { comp ->
                comp.action()
                    .onFailure().retry().atMost(5)
                    .subscribe().with (
                        { _ ->
                            Log.i(CompensationService::class, "compensation action: ${comp.functionName} success")
                        },
                        { e ->
                            val log = ErrorLog(
                                functionName = comp.functionName,
                                param = comp.params,
                                createDate = now)
                            errorLogRepo.addOneErrorLog(log)
                                .subscribe().with {
                                    Log.i(CompensationService::class, "Error logs added")
                                }
                        }
                    )
            }
        } catch (_: Exception) { }

    }
}