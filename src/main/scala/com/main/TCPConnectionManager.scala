package com.main

import java.net.InetSocketAddress

import akka.actor.{Props, Actor}
import akka.io.{Tcp, IO}
import akka.io.Tcp._
import akka.util.ByteString


class TCPConnectionManager(address: String, port: Int) extends Actor {

  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(address, port))

  override def receive: Receive = {
    case Bound(local) =>
      println(s"Server started on $local")
    case Connected(remote, local) =>
      val handler = context.actorOf(Props[TCPConnectionHandler])
      println(s"New connnection: $local -> $remote")
      sender() ! Register(handler)
    case message: String =>
      context.parent ! message
  }
}

class TCPConnectionHandler extends Actor {
  override def receive: Actor.Receive = {
    case Received(data) =>
      val decoded = data.utf8String
      context.parent ! decoded
      sender() ! Write(ByteString(s"OK"))

    case _: ConnectionClosed =>
      println("Connection has been closed")
      context stop self
  }
}
