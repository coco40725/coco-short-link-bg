package com.coco.application.ctrl

import com.coco.application.middleware.header.GetHeader
import com.coco.application.cqrs.DefaultActionExecutor
import com.coco.application.cqrs.command.addLinkInfo.AddLinkInfoCommand
import com.coco.application.cqrs.command.addLinkInfo.AddLinkInfoResult
import com.coco.application.cqrs.command.changeExpireDate.ChangeExpireDateCommand
import com.coco.application.cqrs.command.changeOriginLink.ChangeOriginLinkCommand
import com.coco.application.cqrs.command.disabledLinkInfo.DisabledLinkInfoCommand
import com.coco.application.cqrs.command.enabledLinkInfo.EnabledLinkInfoCommand
import com.coco.application.cqrs.command.generateWhiteShortLinkList.GenerateWhiteShortLinkListCommand
import com.coco.application.cqrs.command.redirectToOriginalLink.RedirectToOriginalLinkCommand
import com.coco.application.middleware.auth.JwtRequest
import com.coco.application.middleware.auth.Logged
import com.coco.domain.vo.RequestHeaderData
import com.coco.infra.config.WebConfig
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response

/**
@author Yu-Jing
@create 2024-08-10-下午 06:28
 */
@Path("/")
class LinkInfoCtrl @Inject constructor(
    private val executor: DefaultActionExecutor,
    private val header: RequestHeaderData,
    private val webConfig: WebConfig,
    private val jwt: JwtRequest,
) {

    @GetHeader
    @GET
    @Path("/{shortLink}")
    fun redirectToOriginalLink(@PathParam("shortLink") shortLink: String): Uni<Response> {
        val fullShortLink = "${webConfig.websiteDomain()}/${shortLink}"
        val command = RedirectToOriginalLinkCommand(fullShortLink, header)
        return executor.executeCommand(command)
    }

    @POST
    @Logged
    @Path("/add-link-info")
    fun addLinkInfo(command: AddLinkInfoCommand): Uni<AddLinkInfoResult?> {
        command.jwt = jwt
        return executor.validateCommand(command)
            .chain { validateResult -> executor.executeCommand(command, validateResult) }
    }


    @PATCH
    @Logged
    @Path("/disable-link-info")
    fun disabledLinkInfo(@QueryParam("id") id: String): Uni<Boolean> {
        val command = DisabledLinkInfoCommand(id, jwt)
        return executor.validateCommand(command)
            .chain { _  -> executor.executeCommand(command)
        }
    }

    @PATCH
    @Logged
    @Path("/enable-link-info")
    fun enabledLinkInfo(@QueryParam("id") id: String): Uni<Boolean> {
        val command = EnabledLinkInfoCommand(id, jwt)
        return executor.validateCommand(command)
            .chain { _  -> executor.executeCommand(command) }
    }

    @PATCH
    @Logged
    @Path("/change-origin-link-info")
    fun changeOriginLink(command: ChangeOriginLinkCommand): Uni<Boolean> {
        command.jwt = jwt
        return executor.validateCommand(command)
            .chain { _ ->executor.executeCommand(command) }
    }

    @PATCH
    @Logged
    @Path("/change-expire-date")
    fun changeExpireDate(command: ChangeExpireDateCommand): Uni<Boolean> {
        command.jwt = jwt
        return executor.validateCommand(command)
            .chain { _  -> executor.executeCommand(command) }
    }

    @GET
    @Path("/generate-white-short-link-list")
    fun generateWhiteShortLinkList(): Uni<Long> {
        val command = GenerateWhiteShortLinkListCommand()
        return executor.executeCommand(command)
    }


}