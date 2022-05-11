import json
import socket
import threading
import requests
import sseclient

# Producer is a wrapper for the docker container producer.
# It opens the two sse streams and sends the data to the message broker.
# The sse streams are located at http://localhost:4000/tweets/1 and http://localhost:4000/tweets/2
# The producer must subscribe first to the message broker, at the address http://localhost:8080/prod_subscribe, which returns an address.
# Then the producer must forward all the messages to the message broker, at the address returned by the subscription. http://localhost:808x/publish

first_stream_url = 'http://localhost:4000/tweets/1'
second_stream_url = 'http://localhost:4000/tweets/2'

producer_host = 'localhost'
producer_port = 9000


class Producer:
    def __init__(self, sse_url, broker_host, broker_port):
        self.sse_url = sse_url
        self.broker_host = broker_host
        self.broker_port = broker_port
        self.messages_sent = 0
        self.messages_received = 0
        self.work = True

    def forward(self):

        # Forward all the messages to the message broker
        # Open the sse streams
        response = requests.get(self.sse_url, stream=True, headers={'Accept': 'text/event-stream'})
        messages = sseclient.SSEClient(response).events()

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((self.broker_host, self.broker_port))

        while self.work:
            try:
                msg = next(messages)
                json.loads(msg.data)
            except:
                continue
            s.send(msg.data.encode('utf-8'))
            response = s.recv(1024)
            self.messages_sent += 1

            if response == b'OK':
                self.messages_received += 1

        s.close()
        print("Connection closed")
        print("Sent " + str(self.messages_sent) + " messages to the message broker")
        print("Message Broker received: " + str(self.messages_received) + " messages")


if __name__ == '__main__':
    # Read host and port from the arguments

    producer1 = Producer(first_stream_url, producer_host, producer_port)

    producer_port += 1
    producer2 = Producer(second_stream_url, producer_host, producer_port)

    t1 = threading.Thread(target=producer1.forward)
    t2 = threading.Thread(target=producer2.forward)
    t1.start()
    t2.start()

    input()
    producer1.work = False
    producer2.work = False

    t1.join()
    t2.join()
    print("Done")
