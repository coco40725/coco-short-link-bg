package com.coco.application.cqrs.query.getUserShortLinkInfo

import java.util.*

/**
@author Yu-Jing
@create 2024-08-14-上午 11:32
 */
data class GetUserShortLinkInfoResult(
    var userId: String,
    var enabledShortLinkInfo: List<LinkInfoData> = emptyList(),
    var disabledShortLinkInfo: List<LinkInfoData> = emptyList()
){
    data class LinkInfoData(
        var id: String,
        var shortLink: String? = null,
        var originalLink: String? = null,
        var expirationDate: Date? = null,
        var createDate: Date? = null,
    )
}
