package com.main

import java.net.InetSocketAddress
import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp._
import akka.util.ByteString


class TcpServerManager(address: String, port: Int) extends Actor {

  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(address, port))
  var handler: ActorRef = _

  override def receive: Receive = {
    case Bound(local) =>
      println(s"Server started on $local")

    case Connected(remote, local) =>
      println(s"New connnection: $local -> $remote")
      handler = context.actorOf(Props[TcpServerHandler])
      sender() ! Register(handler)
      handler ! remote.toString

    case jsonMessage: JsonMessage =>
      context.parent ! jsonMessage
  }
}

class TcpServerHandler extends Actor {
  var remote: String = ""

  override def receive: Actor.Receive = {
    case Received(data) =>
      val decoded = data.decodeString("utf-8")
      context.parent ! JsonMessage(decoded, remote)
      sender() ! Write(ByteString(s"OK"))

    case remote: String =>
      this.remote = remote

    case _: ConnectionClosed =>
      println("Connection has been closed")
      context stop self
  }
}
