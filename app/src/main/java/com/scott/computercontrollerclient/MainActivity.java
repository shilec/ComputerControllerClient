package com.scott.computercontrollerclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.shilec.plugin.api.DataPackge;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button btnSetup;
    ImageView ivImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSetup = (Button) findViewById(R.id.btn_send);
        ivImg = (ImageView) findViewById(R.id.iv_img);
        btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUup();
            }
        });
    }

    public void setUup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = "192.168.0.104";
                int port = 9009;

                try {
                    Socket socket = new Socket(ip,port);
                    InputStream inputStream = socket.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(inputStream);

                    while(true) {
                        Object o = ois.readObject();
                        DataPackge dataPackge = (DataPackge) o;
                        //Log.i("MainAactivity", "====" + dataPackge);

                        final Bitmap bitmap = BitmapFactory.decodeByteArray(dataPackge.datas, 0, dataPackge.datas.length, null);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivImg.setImageBitmap(rotateBitmap(bitmap,90));
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

}
