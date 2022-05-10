package com.main

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import java.net.ServerSocket
import scala.util.{Try, Using}

// The listener class is used to listen for tcp packages.
// It is used to receive the packages and to send the packages to the Worker Group.
class Listener() extends Actor {
  var workerGroupRef: ActorRef = _
  var tcpConnectionManager: ActorRef = _

  def findFreePort(): Try[Int] = Using(new ServerSocket(0))(_.getLocalPort)


  override def receive: Receive = {
    case ("start", actorRef: ActorRef) =>
      workerGroupRef = actorRef
      var port = findFreePort().get
      port = 5012 //TODO remove this line
      tcpConnectionManager = context.actorOf(Props(new TCPConnectionManager("localhost", port)))

      tcpConnectionManager ! "start"
      println(s"worker group started, address: $this.workerGroupRef")

    case "stop" =>
      context.stop(self)

    case message: String =>
      this.workerGroupRef ! message
  }
}



