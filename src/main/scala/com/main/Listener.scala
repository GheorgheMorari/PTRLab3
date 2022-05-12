package com.main

import akka.actor.{Actor, ActorRef, ActorSelection, Props}

class Listener() extends Actor {
  var workerRef: ActorSelection = context.actorSelection("../worker")
  var tcpConnectionManager: ActorRef = _

  override def receive: Receive = {
    case createListener: CreateListener =>
      tcpConnectionManager = context.actorOf(Props(new TCPConnectionManager("localhost", createListener.port)))

    case jsonMessage: JsonMessage =>
      this.workerRef ! jsonMessage

    case a =>
      print("unknown message:", a)
  }
}

case class CreateListener(port: Int)

