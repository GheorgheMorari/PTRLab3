package com.main

import akka.actor.{Actor, ActorRef, ActorSelection, Props}

class Subscriber extends Actor {
  val port = 9999
  val tcpConnectionManager: ActorRef = context.actorOf(Props(new TCPConnectionManager("localhost", port)))
  val pipelineManager: ActorSelection = context.actorSelection("../pipelineManager")
  override def receive: Receive = {
    case jsonMessage: JsonMessage =>
      var operation_type = ""
      try {
        operation_type = jsonMessage.get_field("operation").toString()
      } catch {
        case e: Exception =>
          println("Error: " + e.getMessage)
      }

      if (operation_type == "") {
        println("Invalid message")
      } else {

        if (operation_type == "subscribe") {
          tcpConnectionManager ! SubscribeConsumer(jsonMessage.sender)
        } else {
          tcpConnectionManager ! UnsubscribeConsumer(jsonMessage.sender)
        }
      }
  }
}


case class SubscribeConsumer(consumer_address: String)

case class UnsubscribeConsumer(consumer_address: String)