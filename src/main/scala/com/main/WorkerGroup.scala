package com.main

import akka.actor.Actor

class WorkerGroup() extends Actor {
  override def receive: Receive = {
    case subscribeConsumer: SubscribeConsumer =>
      println("WorkerGroup: Received SubscribeConsumer", subscribeConsumer)

    case unsubscribeConsumer: UnsubscribeConsumer =>
      println("WorkerGroup: Received UnsubscribeConsumer", unsubscribeConsumer)

    case message: JsonMessage =>
      println(s"WorkerGroup received message: $message")
      println(message.get_topic)
  }
}
