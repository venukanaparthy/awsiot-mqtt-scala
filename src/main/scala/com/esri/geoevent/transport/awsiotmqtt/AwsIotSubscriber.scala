package com.esri.geoevent.transport.awsiotmqtt

import java.util.Random
import java.sql.Timestamp
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttMessage, MqttConnectOptions}
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class AwsIotSubscriber extends AwsIotBase with AwsIotCommand {
  
  override def execute(params:AWSIotParams, topic:String):Unit = {
    
    // MQTT subscriber    
    val brokeUrl = "%s%s:%s".format(params.getProtocol, params.getHost,params.getPort)
    println("connecting to %s".format(brokeUrl))
    
    val client:MqttClient = new MqttClient(brokeUrl, "subid %d".format(new Random().nextInt(100)), new MemoryPersistence())
    client.setCallback(this)
    
    // SocketFactory
    val socketFactory = SocketFactoryUtil.generateFromFilePath(params.getRootCA(), params.getCertificate(), params.getPrivateKey())

    val options:MqttConnectOptions = new MqttConnectOptions()
    options.setSocketFactory(socketFactory)
    options.setCleanSession(true);
    client.connect(options)
    
    client.subscribe(topic)
  }
  
  override def messageArrived(topic:String, message:MqttMessage): Unit = {
    val time = new Timestamp(System.currentTimeMillis()).toString();
    println("Received at %s, Topic: %s,  Message %s".format(time, topic, message.getPayload))
  }
}