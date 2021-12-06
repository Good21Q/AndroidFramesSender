package com.example.andreypc.video_v2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;

public class MainActivity extends AppCompatActivity implements SocketListener, ImageControllerListener {

    public TextView serverStatus;
    private Button connectButton;
    private EditText ipEditText;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverStatus = findViewById(R.id.textView);
        connectButton = findViewById(R.id.button);
        ipEditText = findViewById(R.id.editTextIP);

        context = this;

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    Camera camera = Camera.open();
                    ImageController imageController = new ImageController(ipEditText.getText().toString(), 9191, (MainActivity) context, (MainActivity) context);

                    CamView mPreview = new CamView(getApplicationContext(), camera, imageController);

                    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                    preview.addView(mPreview);

                    new Thread(imageController).start();

                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Camera opening error " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onSocketConnected(Socket imageSocket) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Socket connected", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnectionError(final String error) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Connection error: "+error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFrameSent(final int totalFrames, final int frameSize) {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                serverStatus.setText("Frames sent: " + totalFrames + ". Frame size: "+frameSize);
            }
        });
    }
}