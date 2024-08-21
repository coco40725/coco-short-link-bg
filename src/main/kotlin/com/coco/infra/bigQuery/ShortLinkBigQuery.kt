package com.coco.infra.bigQuery

import ShortLinkBigQueryException
import com.coco.domain.model.LinkLog
import com.coco.infra.config.BigQueryConfig
import com.google.cloud.bigquery.*
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.*
import java.util.stream.Collectors
import java.util.stream.StreamSupport




/**
@author Yu-Jing
@create 2024-08-16-下午 04:51
 */

@ApplicationScoped
class ShortLinkBigQuery @Inject constructor(
    private val bigQuery: BigQuery,
    private val bigQueryConfig: BigQueryConfig
) {

    fun getDataByShortLink(url: String): List<LinkLog> {
        val query = "SELECT * FROM ${bigQueryConfig.bigQueryDb()}.${bigQueryConfig.bigQueryLogTable()} WHERE shortLink=\"${url}\""
        val queryConfig = QueryJobConfiguration.newBuilder(query).setUseLegacySql(false).build()


        // Create a job ID so that we can safely retry.
        val jobId = JobId.of(UUID.randomUUID().toString())
        var queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build())


        // Wait for the query to complete.
        queryJob = queryJob.waitFor()


        // Check for errors
        if (queryJob == null) {
            throw ShortLinkBigQueryException("Job no longer exists")
        } else if (queryJob.status.error != null) {
            throw ShortLinkBigQueryException(queryJob.status.error.toString())
        }

        // Get the results and return them
        val result = queryJob.getQueryResults()
        val linkLogList = StreamSupport.stream(result.iterateAll().spliterator(), false)
            .map { row: FieldValueList ->
                val dateLong = row["createDate"].doubleValue * 1000
                val shortLink = if (row["refererIP"].isNull) null else row["shortLink"].stringValue
                val refererIP = if (row["refererIP"].isNull) null else row["refererIP"].stringValue
                val userAgent = if (row["userAgent"].isNull) null else row["userAgent"].stringValue
                val referer = if (row["referer"].isNull) null else row["referer"].stringValue
                val log = LinkLog(
                    id = row["id"].stringValue,
                    shortLink = shortLink,
                    refererIP = refererIP,
                    userAgent = userAgent,
                    referer = referer,
                    createDate = Date(dateLong.toLong())
                )
                log
            }.collect(Collectors.toList())

        return linkLogList
    }
}