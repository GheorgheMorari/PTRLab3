package com.main

import akka.actor.{Actor, ActorRef, Props}

class Pipeline extends Actor {
  val worker: ActorRef = context.actorOf(Props[Worker], "worker")
  val listener: ActorRef = context.actorOf(Props[Listener], "listener")

  override def receive: Receive = {
    case createPipeline: CreatePipeline =>
      listener ! CreateListener(worker, createPipeline.port)

    case subscribeConsumer: SubscribeConsumer =>
      worker ! subscribeConsumer

    case unsubscribeConsumer: UnsubscribeConsumer =>
      worker ! unsubscribeConsumer

    case _ =>
      println("Unknown message")
  }
}
