package com.main

import akka.actor.Actor

class WorkerGroup() extends Actor {
  override def receive: Receive = {
    case message: ProducerMessage =>
      println(s"WorkerGroup received message: $message")
      println(message.get_topic)
  }
}
