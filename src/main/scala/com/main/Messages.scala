package com.main

import spray.json._
import akka.actor.ActorRef

case class JsonMessage(string: String, sender: String = "") {
  var json: JsObject = string.parseJson.asJsObject

  def getMessage: String = {
    string
  }

  def get_topic: String = {
    this.get_field("topic").toString()
  }

  def get_field(field: String): JsObject = {
    json.fields(field).asJsObject
  }
}


case class ActorRefMessage(actorRef: ActorRef) {
  def getActorRef: ActorRef = {
    actorRef
  }
}