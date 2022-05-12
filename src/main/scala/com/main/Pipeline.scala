package com.main

import akka.actor.{Actor, ActorRef, Props}

import scala.collection.mutable.ListBuffer

class Pipeline extends Actor {
  val worker: ActorRef = context.actorOf(Props[Worker], "worker")
  var listener_list = new ListBuffer[ActorRef]()

  override def receive: Receive = {
    case createListener: CreateListener =>
      val new_listener = context.actorOf(Props[Listener])
      new_listener ! createListener
      listener_list += new_listener

    case subscribeConsumer: SubscribeConsumer =>
      worker ! subscribeConsumer

    case unsubscribeConsumer: UnsubscribeConsumer =>
      worker ! unsubscribeConsumer

    case _ =>
      println("Unknown message")
  }
}
