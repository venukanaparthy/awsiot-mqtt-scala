# awsiot-mqtt-scala
awsiot publisher-consumer in scala

1. Configure AWSIot certificates in resource\application.conf

	awsiot {

		endpoint = "xxxxxxx.iot.us-west-2.amazonaws.com" 

		port = "8883"

		protocol = "ssl://"

		qos = "0"

		rootCA = "C:/certs/rootCA.pem"

		privateKey = "C:/certs/private.pem"

		certificate = "C:/certs/cert.pem"

	}

	<a href="http://docs.aws.amazon.com/iot/latest/developerguide/identity-in-iot.html" target="_blank">AWS Iot Security</a>

2. Build, mvn clean package

3. Run consumer,
   java -jar target/awsiot-mqtt-scala-0.1.jar consume topic-name

4. Run publisher,
   java -jar target/awsiot-mqtt-scala-0.1.jar publish topic-name data/trips.txt	