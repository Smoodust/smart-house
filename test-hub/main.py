import paho.mqtt.client as mqtt
import random
import json
import time

BROKER = "mosquitto"
PORT = 1883
PUB_TOPIC = "hubs/data"
SUB_TOPIC = "hubs/test/command"

# Callback for incoming messages on the subscribed topic
def on_message(client, userdata, msg):
    print(f"Received on {msg.topic}: {msg.payload.decode()}")
    payload = json.loads(msg.payload.decode())
    payload["login"] = "test"
    payload["pass"] = "test"
    msg_info = mqttc.publish(PUB_TOPIC, json.dumps(payload))

mqttc = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
mqttc.username_pw_set("test", "test")
mqttc.on_message = on_message

# Connect and subscribe
mqttc.connect(BROKER, PORT, 60)
mqttc.subscribe(SUB_TOPIC)
mqttc.loop_start()

try:
    while True:
        payload = json.dumps({
            "login": "test",
            "pass": "test",
            "deviceId": "kitchen_heater",
            "data": {
                "turnOnOff": True,
                "temperature": random.randint(5, 100)
            }
        })
        msg_info = mqttc.publish(PUB_TOPIC, payload)
        print("message publish try")
        time.sleep(10)
except Exception as e:
    print(f"Error: {e}")
finally:
    print("Stopping and disconnecting...")
    mqttc.loop_stop()
    mqttc.disconnect()