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
    fun executeCompensation(actions: List<CompensationActions>): Uni<Unit> {
        val now = Date()
        val failUni = mutableListOf<Uni<Boolean>>()
        try {
            actions.forEach { comp ->
                comp.action()
                    .onFailure().retry().atMost(5)
                    .subscribe().with (
                        { _ ->
                            Log.i(CompensationService::class, "compensation action: ${comp.functionName} success")
                        },
                        { e ->
                            Log.i(CompensationService::class, "compensation action: ${comp.functionName} failed, cause: ${e.message}")
                            val log = ErrorLog(
                                functionName = comp.functionName,
                                param = comp.params,
                                createDate = now)
                            failUni.add(errorLogRepo.addOneErrorLog(log))
                        }
                    )
            }

            if (failUni.isEmpty()) {
                Log.i(CompensationService::class, "All compensation actions executed")
                return Uni.createFrom().item(Unit)
            } else {
                return Uni.join().all(failUni).andCollectFailures()
                    .map { _ ->
                        Log.i(CompensationService::class, "Error logs added")
                    }
            }

        } catch (_: Exception) {
            return Uni.createFrom().item(Unit)
        }

    }
}