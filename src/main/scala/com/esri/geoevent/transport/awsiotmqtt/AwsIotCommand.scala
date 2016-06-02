package com.esri.geoevent.transport.awsiotmqtt

import java.util.ArrayList

trait AwsIotCommand {
  
  //val parameters = new ArrayList[String]()
  
  def validate(): Unit = ???
  
  def execute(params:AWSIotParams, topic:String, msg:String):Unit = ???
  
  def execute(params:AWSIotParams, topic:String):Unit = ??? 
  
}