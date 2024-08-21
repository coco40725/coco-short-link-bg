package com.coco.infra.pubsub


import com.coco.domain.model.LinkLog
import com.coco.domain.vo.pubsub.LinkLogData
import com.coco.infra.config.PubSubConfig
import com.coco.infra.repo.LinkLogRepo
import com.coco.infra.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.pubsub.v1.AckReplyConsumer
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.cloud.pubsub.v1.Publisher
import com.google.cloud.pubsub.v1.Subscriber
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import io.quarkiverse.googlecloudservices.pubsub.QuarkusPubSub
import io.smallrye.mutiny.Multi
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject


/**
@author Yu-Jing
@create 2024-08-10-下午 09:07
 */
@ApplicationScoped
class LinkLogPubSubSvc @Inject constructor(
    private val pubSubConfig: PubSubConfig,
    private val pubSub: QuarkusPubSub,
    private val linkLogRepo: LinkLogRepo
) {

    private lateinit var subscriber: Subscriber
    private lateinit var publisher: Publisher

    @PostConstruct
    fun init(){
        // init topic and subscription
        val topic = pubSubConfig.linkInfoLogTopic()
        val subscriptionId = pubSubConfig.subLinkInfoLog()

        // Subscribe to PubSub (we will use this to receive messages and save data to mongodb)
        val receiver = MessageReceiver { message, consumer ->
            handleMessage(message, consumer).subscribe().with {  consumer.ack() }
        }
        subscriber = pubSub.subscriber(subscriptionId, receiver)
        subscriber.startAsync().awaitRunning()

        // Publish to PubSub
        publisher = pubSub.publisher(topic)
    }



    fun publish(data: LinkLog){
        try {
            val messageData = ByteString.copyFromUtf8(convertToJsonString(data))
            val pubsubMessage = PubsubMessage.newBuilder().setData(messageData).build()
            println("publish pubsubMessage: $pubsubMessage")
            publisher.publish(pubsubMessage)

        } catch (e:Exception){
            Log.i(this::class, "fail: ${e.message}")
            Log.i(this::class, "fail data : $data")
        }
    }


    private fun handleMessage(message: PubsubMessage, consumer: AckReplyConsumer): Multi<LinkLog?> {
        val dataJson = message.data.toStringUtf8()
        val data = convertToData(dataJson)

        return linkLogRepo.insetOne(data).toMulti()
    }

    private fun convertToJsonString(data: LinkLog?): String {
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(data)
    }

    private fun convertToData(json: String): LinkLog {
        val mapper = ObjectMapper()
        val logData =  mapper.readValue(json, LinkLog::class.java)
        return logData
    }
}