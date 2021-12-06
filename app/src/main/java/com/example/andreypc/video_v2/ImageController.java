package com.example.andreypc.video_v2;

import java.net.Socket;

public class ImageController implements Runnable, FrameListener {

    private SocketManager socketManager;
    private int TotalImagesSent = 0;
    private SocketListener listener;
    private ImageControllerListener imageControllerListener;


    public int getTotalImagesSent() {
        return TotalImagesSent;
    }

    public void setTotalImagesSent(int totalImagesSent) {
        TotalImagesSent = totalImagesSent;
    }

    private FrameListener frameListener;

    public ImageController(String IP, int port){
        super();
        socketManager = new SocketManager(IP, port);
    }

    public ImageController(String IP, int port, SocketListener listener){
        super();
        socketManager = new SocketManager(IP, port);
        this.listener = listener;
    }

    public ImageController(String IP, int port, SocketListener listener, ImageControllerListener imageControllerListener){
        super();
        socketManager = new SocketManager(IP, port);
        this.listener = listener;
        this.imageControllerListener = imageControllerListener;
    }

    @Override
    public void run() {
        socketManager.connect(new SocketListener() {
            @Override
            public void onSocketConnected(Socket imageSocket) {
                frameListener = new ImageManager(imageSocket);
                if (listener != null){
                    listener.onSocketConnected(imageSocket);
                }
            }

            @Override
            public void onConnectionError(String error) {
                if (listener != null){
                    listener.onConnectionError(error);
                }
            }
        });
    }

    @Override
    public void onFrameReady(byte[] image) {
        if (frameListener != null){
            frameListener.onFrameReady(image);
            this.setTotalImagesSent(getTotalImagesSent()+1);
            if (this.imageControllerListener != null){
                this.imageControllerListener.onFrameSent(getTotalImagesSent(), image.length);
            }
        }
    }
}