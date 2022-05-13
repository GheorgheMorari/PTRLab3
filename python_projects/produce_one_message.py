import socket

if __name__ == '__main__':
    # Send tcp message to 127.0.0.1:xxxxx
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(('localhost', 9000))

    file = open("partial_docker_message.json", "r", encoding="utf-8")
    file_contents = file.read()
    s.send(file_contents.encode('utf-8'))
    response = s.recv(1024)
    s.close()
    print(response)
