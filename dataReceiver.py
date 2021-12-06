import socket
import threading
import socket, numpy as np, cv2

bind_ip = '192.168.1.58'
bind_port = 9191

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((bind_ip, bind_port))
server.listen(5)

print('Listening on {}:{}'.format(bind_ip, bind_port))


def handle_client_connection(client_socket):
    while True:
        try:
            request = client_socket.recv(12000)

            mg = cv2.imdecode(np.fromstring(request, np.uint8), 1)

            mg = cv2.resize(mg, (1280, 720))

            cv2.imshow("1", mg)
            cv2.waitKey(1)
        except Exception as e:
            print(e)

while True:
    client_sock, address = server.accept()
    print('Accepted connection from {}:{}'.format(address[0], address[1]))
    client_handler = threading.Thread(
        target=handle_client_connection,
        args=(client_sock,)
    )
    client_handler.start()
