package com.esri.geoevent.transport.awsiotmqtt

import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage

class AwsIotBase extends MqttCallback {
  
  override def connectionLost(cause:Throwable): Unit = ???
  
  override def deliveryComplete(token:IMqttDeliveryToken): Unit = ???
  
  override def messageArrived(topic:String, message:MqttMessage): Unit = ???
  
}