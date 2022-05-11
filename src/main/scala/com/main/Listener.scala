package com.main

import akka.actor.{Actor, ActorRef, Props}

class Listener() extends Actor {
  var workerRef: ActorRef = _
  var tcpConnectionManager: ActorRef = _

  override def receive: Receive = {
    case createListener: CreateListener =>
      workerRef = createListener.actorRef
      tcpConnectionManager = context.actorOf(Props(new TCPConnectionManager("localhost", createListener.port)))


    case jsonMessage: JsonMessage =>
      this.workerRef ! jsonMessage

    case a =>
      print("unknown message:", a)
  }
}

case class CreateListener(actorRef: ActorRef, port: Int)

