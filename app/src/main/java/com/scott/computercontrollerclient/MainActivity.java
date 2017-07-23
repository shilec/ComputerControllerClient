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

import com.afollestad.materialdialogs.MaterialDialog;
import com.shilec.plugin.api.common.Contacts;
import com.shilec.plugin.api.common.DataPackge;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStart;
    Button btnRefresh;
    Button btnClick;
    ScreenImageView ivImg;

    ObjectInputStream ois;
    ObjectOutputStream oos;

    String ip = "192.168.199.184";
    int port = 9009;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btn_start);
        btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnClick = (Button) findViewById(R.id.btn_click);

        btnClick.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        ivImg = (ScreenImageView) findViewById(R.id.iv_img);
        //ivImg.setOnMouseListenner(this);
        ivImg.setOnClickListener(this);
    }

//    public void setUup() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String ip = "192.168.0.104";
//                int port = 9009;
//
//                try {
//                    Socket socket = new Socket(ip,port);
//                    InputStream inputStream = socket.getInputStream();
//                    ObjectInputStream ois = new ObjectInputStream(inputStream);
//
//                    while(true) {
//                        Object o = ois.readObject();
//                        DataPackge dataPackge = (DataPackge) o;
//                        //Log.i("MainAactivity", "====" + dataPackge);
//
//                        final Bitmap bitmap = BitmapFactory.decodeByteArray(dataPackge.datas, 0, dataPackge.datas.length, null);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ivImg.setImageBitmap(rotateBitmap(bitmap,90));
//                            }
//                        });
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click: {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mouseClick();
                    }
                }).start();

            }break;
            case R.id.btn_refresh: {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        screenRefresh();
                    }
                }).start();

            }break;
            case R.id.btn_start: {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            startClient();
//                            screenRefresh();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
                new MaterialDialog.Builder(this)
                        .title("progress")
                        .content("wait")
                        .progress(true, 0)
                        .show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            startClient();
                           // screenRefresh();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }break;

        }
    }


    private void startClient() throws Exception {
//        Socket socket = new Socket(ip,port);
//        ois = new ObjectInputStream(socket.getInputStream());
//        oos = new ObjectOutputStream(socket.getOutputStream());
//
//        while(true) {
//            DataPackge data = (DataPackge) ois.readObject();
//            switch (data.code) {
//                case Contacts.Command.CMD_PC_INFO: {
//                    onScreenRefresh(data);
//                }break;
//            }
//        }
    }

//    private void onScreenRefresh(final DataPackge data) {
//        Log.i("shilec","=======" + data.data);
//        final Bitmap bitmap = BitmapFactory.decodeByteArray(data.fileDatas, 0, data.fileDatas.length, null);
//        final Bitmap bitmap1 = rotateBitmap(bitmap,90);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ivImg.setImageBitmap(bitmap1);
//                try {
//                    JSONObject jObject = new JSONObject(data.data);
//                    jObject = jObject.getJSONObject("mouseInfo");
//                    Log.i("shilec","mouseInfo:" + jObject);
//                    int x = jObject.optInt("x");
//                    int y = jObject.optInt("y");
//                    ivImg.setMousePoint(1080-y,x);
//                    Log.i("shilec","x = " + x + ",y = " + y);
//                    Log.i("shilec","Ix = " + ivImg.getWidth() + ",Iy = " + ivImg.getHeight());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void screenRefresh() {
        DataPackge data = new DataPackge();
        data.code = Contacts.Command.CMD_PC_INFO;
        try {
            oos.writeObject(data);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mouseClick() {
        DataPackge data= new DataPackge();
        data.code = Contacts.Command.CMD_MOUSE_LEFT_CLICK;
        try {
            oos.writeObject(data);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onMove(final float x,final float y) {
//        Log.i("Click","click x = " + x + ",y = " + y);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DataPackge data = new DataPackge();
//                data.code = Contacts.Command.CMD_MOUSE_MOVE;
//                data.data = "{\"x\":" + (1080 - x) + ",\"y\":" + y + "}";
//                try {
//                    oos.writeObject(data);
//                    oos.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }

}
