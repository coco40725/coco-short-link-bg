package com.coco.application.cqrs.command.generateWhiteShortLinkList

import com.coco.application.cqrs.command.base.Command
import io.smallrye.mutiny.Uni

/**
@author Yu-Jing
@create 2024-08-12-上午 11:27
 */
class GenerateWhiteShortLinkListCommand(): Command<Uni<Long>>