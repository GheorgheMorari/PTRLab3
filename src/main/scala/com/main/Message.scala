package com.main

case class Message(message: String) {
  def getMessage: String = {
    message
  }
}

class CustomerMessage(address: String, message_str: String) extends Message(message_str) {
  def getAddress: String = {
    address
  }
}