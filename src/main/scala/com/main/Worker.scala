package com.main

import akka.actor.Actor

class Worker() extends Actor {
  override def receive: Receive = {
    case subscribeConsumer: SubscribeConsumer =>
      println("Worker: Received SubscribeConsumer", subscribeConsumer)

    case unsubscribeConsumer: UnsubscribeConsumer =>
      println("Worker: Received UnsubscribeConsumer", unsubscribeConsumer)

    case message: JsonMessage =>
      println(message.get_topic)
//      println(s"Worker received message: $message")
  }
}
