package com.esri.geoevent.transport.awsiotmqtt

import java.util.Random
import java.sql.Timestamp
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttMessage, MqttConnectOptions}
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class AwsIotPublisher extends AwsIotBase with AwsIotCommand {
  
  override def execute(params:AWSIotParams, topic:String, msg:String):Unit = {
          
    // MQTT client    
    val brokeUrl = "%s%s:%s".format(params.getProtocol, params.getHost,params.getPort)
    println("connecting to %s".format(brokeUrl))
    
    val client:MqttClient = new MqttClient(brokeUrl, "pubid %d".format(new Random().nextInt(100)), new MemoryPersistence())
    //client.setCallback(new PublishMqttCallback)
    
    // SocketFactory    
    val socketFactory = SocketFactoryUtil.generateFromFilePath(params.getRootCA(), params.getCertificate(), params.getPrivateKey())

    val options:MqttConnectOptions = new MqttConnectOptions()
    options.setSocketFactory(socketFactory)
    options.setCleanSession(true);
    client.connect(options)

    val message:MqttMessage = new MqttMessage(msg.getBytes("UTF-8"))
    message.setQos(params.getQos)
    client.publish(topic, message)
    val time = new Timestamp(System.currentTimeMillis()).toString()
    println("Sent at %s, Topic: %s,  Message %s".format(time, topic, message.getPayload))
  }
}