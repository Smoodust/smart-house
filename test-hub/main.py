import paho.mqtt.client as mqtt
import time
import random
import json

BROKER = "mosquitto"
PORT = 1883
TOPIC = "hubs/data"
PAYLOAD = "Hello MQTT"

mqttc = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
mqttc.connect(BROKER, PORT, 60)

mqttc.loop_start()
for i in range(100):
    msg_info = mqttc.publish(TOPIC, json.dumps({
        "login":"test",
        "pass":"test",
        "deviceId":"kitchen_heater",
        "data": {
            "turnOnOff": True,
            "positionBlinds": random.randint(5, 100)
        }
    }))
    print("message publish try")
    msg_info.wait_for_publish()
    print("published message")
    time.sleep(5)
mqttc.loop_stop()