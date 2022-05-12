package com.main

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import akka.io.Tcp.Close
import akka.util.ByteString

import scala.collection.mutable.ArrayBuffer


class Worker() extends Actor {
  var topic_consumer_array_map: Map[String, ArrayBuffer[String]] = Map[String, ArrayBuffer[String]]()
  var consumer_topic_array_map: Map[String, ArrayBuffer[String]] = Map[String, ArrayBuffer[String]]()
  var consumer_actor_map: Map[String, ActorRef] = Map[String, ActorRef]()

  override def receive: Receive = {
    case subscribeConsumer: SubscribeConsumer =>
      // Check consumer topic count if it exists
      if (!consumer_topic_array_map.contains(subscribeConsumer.consumer_address)) {
        // If it doesn't exist, create a new count
        consumer_topic_array_map += (subscribeConsumer.consumer_address -> ArrayBuffer[String]())
        val consumer_actor = context.actorOf(Props(new TCPClient(subscribeConsumer.consumer_address)), subscribeConsumer.consumer_address)
        consumer_actor_map += (subscribeConsumer.consumer_address -> consumer_actor)
      }

      for (topic <- subscribeConsumer.topic_array) {
        consumer_topic_array_map(subscribeConsumer.consumer_address) += topic

        if (topic_consumer_array_map.contains(topic)) {
          topic_consumer_array_map(topic) += subscribeConsumer.consumer_address
        } else {
          topic_consumer_array_map += (topic -> ArrayBuffer(subscribeConsumer.consumer_address))
        }
      }

    case unsubscribeConsumer: UnsubscribeConsumer =>
      for (topic <- unsubscribeConsumer.topic_array) {
        topic_consumer_array_map(topic) -= unsubscribeConsumer.consumer_address
        consumer_topic_array_map(unsubscribeConsumer.consumer_address) -= topic
      }

      if (consumer_topic_array_map(unsubscribeConsumer.consumer_address).isEmpty) {
        topic_consumer_array_map -= unsubscribeConsumer.consumer_address
        consumer_actor_map(unsubscribeConsumer.consumer_address) ! "stop"
        consumer_topic_array_map -= unsubscribeConsumer.consumer_address
        consumer_actor_map -= unsubscribeConsumer.consumer_address
      }

    case message: JsonMessage =>
      val topic = message.get_topic
      // Check if topic is already present in the map
      if (topic_consumer_array_map.contains(topic)) {
        // Get the array of consumers for the topic
        val consumer_arr = topic_consumer_array_map(topic)
        // For each consumer, send the message
        for (consumer_address <- consumer_arr) {
          val bytes = message.json.toString.getBytes("utf-8")
          consumer_actor_map(consumer_address) ! ByteString.fromArray(bytes)
        }
      }

  }

}
