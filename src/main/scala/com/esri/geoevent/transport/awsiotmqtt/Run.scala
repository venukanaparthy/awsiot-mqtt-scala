package com.esri.geoevent.transport.awsiotmqtt

import scala.io.Source
import com.typesafe.config.ConfigFactory

object Run {
 
  def main(args: Array[String]) {
    
    if (args.length < 1){
       printUsage()
       return;
    }      
    
    var topic =  "geoevent/test"
    var streamFile = ""
    val params = new AWSIotParams();
    
    if(args(0) == "publish"){
      topic =  args(1)
      streamFile =  args(2)         
      val publisher = new AwsIotPublisher();           
      for(trip <- Source.fromFile(streamFile).getLines.drop(1)){              
         //println(trip)
         publisher.execute(params, topic, trip);
         Thread.sleep(1000)
      }                 
    }else if (args(0) == "consume"){
      val subscriber = new AwsIotSubscriber();
        topic =  args(1)
      subscriber.execute(params, topic)      
    }else {
      printUsage()
    }
  }
  
  def printUsage():Unit = {
     println("Usage (Publisher) :AWSIOTMQTTSpark publish topic streamFile")
     println("Usage (Consumer) :AWSIOTMQTTSpark consume topic")
  }
}