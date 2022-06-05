package com.main

import akka.actor.{Actor, ActorRef}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import akka.io.Tcp._

import java.net.InetSocketAddress

class TCPClient(host_and_port: String) extends Actor {
  val host: String = "consumer"
//  val host: String = host_and_port.split(":")(0)
  val port: Int = host_and_port.split(":")(1).toInt

  import context.system

  IO(Tcp) ! Connect(new InetSocketAddress(host, port))

  def receive: Receive = {
    case CommandFailed(_: Connect) =>
      println("Could not connect!")
      context.stop(self)

    case c@Connected(remote, local) =>
      val connection = sender()
      connection ! Register(self)
      context.become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          println("Failed to write data!")
        //        case Received(data) =>
        case _: ConnectionClosed =>
          context.stop(self)
        case "stop" =>
          context.stop(self)
        case _ =>
          println("Unknown message! when connected.")
      }

    case _ =>
      println("Unknown message!")
  }
}
