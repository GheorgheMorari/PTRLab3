//package com.main
//
//import akka.actor.{Actor, Props}
//import akka.util.ByteString
//
//class Sender extends Actor {
//  override def receive: Receive = {
//    case work: Work =>
//      // Recursively get one string address from the array and make a tcp connection to that address
//      // Get one string address from the array
//      var full_address: String = ""
//      try {
//        full_address = work.iterator.next()
//
//        val host = full_address.split(":")(0)
//        val port = full_address.split(":")(1).toInt
//        // Create one actor of type Sender
//        val next_sender = context.actorOf(Props[Sender])
//        next_sender ! work
//
//        // Make a tcp connection to that address
//        val tcp_client = context.actorOf(Props(new TCPClient(host, port)))
//        val bytes = work.message.toString.getBytes("utf-8")
//        tcp_client ! ByteString.fromArray(bytes)
//      } catch {
//        case e: Exception => return receive
//      }
//  }
//}