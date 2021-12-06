package com.example.andreypc.video_v2;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Handler;

public class ImageManager implements FrameListener{

    private Socket dataSocket;

    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    public ImageManager(Socket socket){
        this.dataSocket = socket;

        try {
            dataOutputStream = new DataOutputStream(dataSocket.getOutputStream());
            dataInputStream = new DataInputStream(dataSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFrameReady(byte[] image) {
        try {
            dataOutputStream.write(image);
            dataOutputStream.flush();
            //int status_recv_image = dataInputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}