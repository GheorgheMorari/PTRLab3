package com.main

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import java.net.ServerSocket
import scala.util.{Try, Using}

// The listener class is used to listen for tcp packages.
// It is used to receive the packages and to send the packages to the Worker Group.
class Listener() extends Actor {
  var workerGroupRef: ActorRef = _
  var tcpConnectionManager: ActorRef = _

  private def findFreePort(): Try[Int] = Using(new ServerSocket(0))(_.getLocalPort)


  override def receive: Receive = {
    case message: StartMessage =>
      workerGroupRef = message.getActorRef
      var port = findFreePort().get
      port = 5012 //TODO remove this line
      tcpConnectionManager = context.actorOf(Props(new TCPConnectionManager("localhost", port)))

    case StopMessage =>
      context.stop(self)

    case producerMessage: ProducerMessage =>
      println(s"received json message: $producerMessage")
      this.workerGroupRef ! producerMessage

    case a =>
      print("unknown message:", a)
  }
}



