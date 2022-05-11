package com.main

import spray.json._
import akka.actor.ActorRef

case class ProducerMessage(string: String) {
  var json: JsObject = string.parseJson.asJsObject

  def getMessage: String = {
    string
  }

  def get_topic: String = {
    this.get_field("topic")
  }

  def get_field(field: String): String = {
    json.fields(field).toString()
  }
}


case class StartMessage(actorRef: ActorRef) {
  def getActorRef: ActorRef = {
    actorRef
  }
}

case class StopMessage() {
//  private val stop = "stop"
}