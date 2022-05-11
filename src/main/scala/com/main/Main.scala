//#full-example
package com.main


import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

//#main-class
object Main extends App {
  //#actor-system
//  val main_supervisor: ActorSystem[MainSupervisor] = ActorSystem(MainSupervisor(), "main-supervisor")
  //#actor-system


  val system = ActorSystem()
  var dummy_worker_group = system.actorOf(Props[WorkerGroup], "worker-group")
  var dummy_listener = system.actorOf(Props[Listener], "listener")

  dummy_listener ! new StartMessage(dummy_worker_group)

  //#main-send-messages
}


//
////# main-supervisor
//object MainSupervisor {
//  //  The main supervisor must start the subscriber actor, the listener_group actor and the balancer actor.
//
//  def apply(): Behavior[String] = {
//    Behaviors.setup { context =>
//
//    }
//  }
//}
//
//case class MainSupervisor()
//# main-supervisor
//#full-example
