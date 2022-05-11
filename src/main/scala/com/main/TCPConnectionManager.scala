package com.main

import java.net.InetSocketAddress
import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp._
import akka.util.ByteString


class TCPConnectionManager(address: String, port: Int) extends Actor {

  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(address, port))
  val handler: ActorRef = context.actorOf(Props[TCPConnectionHandler])

  override def receive: Receive = {
    case Bound(local) =>
      println(s"Server started on $local")

    case Connected(remote, local) =>
      println(s"New connnection: $local -> $remote")
      sender() ! Register(handler)
      handler ! remote.toString

    case jsonMessage: JsonMessage =>
      context.parent ! jsonMessage
  }
}

class TCPConnectionHandler extends Actor {
  var remote: String = ""

  override def receive: Actor.Receive = {
    case Received(data) =>
      val decoded = data.utf8String
      context.parent ! JsonMessage(decoded, remote)
      sender() ! Write(ByteString(s"OK"))

    case remote: String =>
      this.remote = remote

    case _: ConnectionClosed =>
      println("Connection has been closed")
      context stop self
  }
}
