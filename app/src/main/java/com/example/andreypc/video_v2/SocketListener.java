package com.example.andreypc.video_v2;

import java.net.Socket;

public interface SocketListener {
    void onSocketConnected(Socket imageSocket);
    void onConnectionError(String error);
}
