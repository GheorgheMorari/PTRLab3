package com.main


import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem()
  val subscriber = system.actorOf(Props[Subscriber], "subscriber")
  val pipeline = system.actorOf(Props[Pipeline], "pipeline")
  var port = 9000
  pipeline ! CreateListener(port)
  port += 1
  pipeline ! CreateListener(port)
}

