package com.main

import akka.actor.{Actor, ActorRef, ActorSelection, Props}

import scala.collection.mutable.ArrayBuffer

class Subscriber extends Actor {
  val port = 9999
  val tcpConnectionManager: ActorRef = context.actorOf(Props(new TcpServerManager("localhost", port)))
  val worker: ActorSelection = context.actorSelection("akka://default/user/pipeline/worker")

  override def receive: Receive = {
    case jsonMessage: JsonMessage =>
      var operation_type = ""
      try {
        operation_type = jsonMessage.get_field("operation").toString().replace("\"", "")
      } catch {
        case e: Exception =>
          println("Error: " + e.getMessage)
      }
      if (operation_type == "subscribe") {
        val topic_arr = jsonMessage.get_field("topic").toString().replace("[", "").replace("]", "").split(',')
        val consumer_address = jsonMessage.get_field("consumer_address").toString().replace("\"", "")
        worker ! SubscribeConsumer(consumer_address, topic_arr.to(ArrayBuffer))
      }

      else if (operation_type == "unsubscribe") {
        val topic_arr = jsonMessage.get_field("topic").toString().replace("[", "").replace("]", "").split(',')
        val consumer_address = jsonMessage.get_field("consumer_address").toString().replace("\"", "")
        worker ! UnsubscribeConsumer(consumer_address, topic_arr.to(ArrayBuffer))
      }
      else {
        println("Error: Unknown operation type")
      }
  }
}


case class SubscribeConsumer(consumer_address: String, topic_array: ArrayBuffer[String])

case class UnsubscribeConsumer(consumer_address: String, topic_array: ArrayBuffer[String])