package com.main

import play.api.libs.json._
import akka.actor.ActorRef

case class JsonMessage(string: String, sender: String = "") {
  val json: JsValue = Json.parse(string)

  def getMessage: String = {
    string
  }

  def get_topic: String = {
    // Topic is situated in message, tweet, user, time_zone
    (json \ "message" \ "tweet" \ "user" \ "time_zone").getOrElse(JsNull).toString()
  }

  def get_field(field: String): JsValue = {
    (json \ field).get
  }
}


case class ActorRefMessage(actorRef: ActorRef) {
  def getActorRef: ActorRef = {
    actorRef
  }
}