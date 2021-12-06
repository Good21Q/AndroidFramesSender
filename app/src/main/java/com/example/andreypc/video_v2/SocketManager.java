package com.example.andreypc.video_v2;

import java.io.IOException;
import java.net.Socket;

public class SocketManager {

    private Socket socket;

    private String server_ip;
    private int server_port;

    public SocketManager(String IP, int port){
        this.server_ip = IP;
        this.server_port = port;
    }

    public void connect(SocketListener listener){
        try {
            socket = new Socket(this.server_ip, this.server_port);
            socket.setSoTimeout(5000);
            listener.onSocketConnected(socket);
        }catch (IOException e){
            listener.onConnectionError(e.getMessage());
        }
    }
}
