package com.main

import akka.actor.Actor

class WorkerGroup() extends Actor {
  override def receive: Receive = {
    case message: String => println(s"WorkerGroup received message: $message")
  }
}
