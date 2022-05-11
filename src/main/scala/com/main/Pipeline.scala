package com.main

import akka.actor.{Actor, Props}

class Pipeline extends Actor {
  val workerGroup = context.actorOf(Props[WorkerGroup], "workerGroup")
  val listener = context.actorOf(Props[Listener], "listener")

  override def receive: Receive = {
    case createPipeline: CreatePipeline =>
      listener ! CreateListener(workerGroup, createPipeline.port)

    case subscribeConsumer: SubscribeConsumer =>
      workerGroup ! subscribeConsumer

    case unsubscribeConsumer: UnsubscribeConsumer =>
      workerGroup ! unsubscribeConsumer
  }
}
