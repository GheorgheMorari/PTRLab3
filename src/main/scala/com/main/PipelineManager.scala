package com.main

import akka.actor.{Actor, ActorRef, Props}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class PipelineManager extends Actor {
  val pipeline_hash_map = mutable.HashMap.empty[Int, ActorRef]
  val pipeline_array: ArrayBuffer[ActorRef] = mutable.ArrayBuffer.empty[ActorRef]

  override def receive: Receive = {
    case createPipeline: CreatePipeline =>
      val pipeline = context.actorOf(Props[Pipeline], createPipeline.port.toString)
      pipeline_hash_map += (createPipeline.port -> pipeline)
      pipeline_array += pipeline
      pipeline ! CreatePipeline

    case subscribeConsumer: SubscribeConsumer =>
      pipeline_array.foreach(pipeline => pipeline ! subscribeConsumer)

    case unsubscribeConsumer: UnsubscribeConsumer =>
      pipeline_array.foreach(pipeline => pipeline ! unsubscribeConsumer)
  }

}

case class CreatePipeline(port: Int)