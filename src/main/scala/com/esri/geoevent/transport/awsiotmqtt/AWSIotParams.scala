package com.esri.geoevent.transport.awsiotmqtt

import com.typesafe.config.ConfigFactory

class AWSIotParams {
  
  private val _config =  ConfigFactory.load()
  
  def getHost():String = {
    return _config.getString("awsiot.endpoint")
  }
  
   def getPort():String = {
    return _config.getString("awsiot.port")
  }
  
   def getProtocol():String = {
     return _config.getString("awsiot.protocol")
   }
   
   def getRootCA():String = {
     return _config.getString("awsiot.rootCA")
   }
   
   def getCertificate():String = {
     return _config.getString("awsiot.certificate")
   }
   
   def getPrivateKey(): String = {
      return _config.getString("awsiot.privateKey")
   }   
   
   def getQos():Int = {
     return _config.getString("awsiot.qos").toInt
   }
}