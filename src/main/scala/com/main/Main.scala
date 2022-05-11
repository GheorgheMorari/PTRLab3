//#full-example
package com.main


import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem()
  val subscriber = system.actorOf(Props[Subscriber], "subscriber")
  val pipelineManager = system.actorOf(Props[PipelineManager], "pipelineManager")
  var port = 9000
  pipelineManager ! CreatePipeline(port)
  port += 1
  pipelineManager ! CreatePipeline(port)
}

