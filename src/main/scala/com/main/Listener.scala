package com.main

import akka.actor.{Actor, ActorRef, Props}

class Listener() extends Actor {
  var workerGroupRef: ActorRef = _
  var tcpConnectionManager: ActorRef = _

  override def receive: Receive = {
    case createListener: CreateListener =>
      workerGroupRef = createListener.actorRef
      tcpConnectionManager = context.actorOf(Props(new TCPConnectionManager("localhost", createListener.port)))


    case jsonMessage: JsonMessage =>
      println(s"received json message: $jsonMessage")
      this.workerGroupRef ! jsonMessage

    case a =>
      print("unknown message:", a)
  }
}

case class CreateListener(actorRef: ActorRef, port: Int)

