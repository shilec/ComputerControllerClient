package com.scott.computercontrollerclient.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.scott.computercontrollerclient.activity.ControlMouseActivity;
import com.scott.computercontrollerclient.utils.BitmapUtils;
import com.scott.computercontrollerclient.utils.Logger;
import com.shilec.plugin.api.common.DataPackge;
import com.shilec.plugin.api.common.ICommadReuqester;
import com.shilec.plugin.api.common.Contacts;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/23 0023.
 */

public class BehaviorRequester implements ICommadReuqester<ControlMouseActivity> {

    @Override
    public void onRequestCommand(DataPackge dataPackge, ControlMouseActivity widget) {
        switch (dataPackge.code) {
            case Contacts.Command.CMD_PC_INFO:
                onGetPcInfo(dataPackge, widget);
                break;
            case Contacts.Command.CMD_MOUSE_CLICK:
                onMouseClick(dataPackge, widget);
                break;
            case Contacts.Command.CMD_MOUSE_MOVE:
                onMouseMove(dataPackge, widget);
                break;
        }
    }

    private void onMouseClick(DataPackge dataPackge, ControlMouseActivity widget) {
    }

    private void onMouseMove(DataPackge dataPackge, ControlMouseActivity widget) {
    }

    private void onGetPcInfo(DataPackge dataPackge, ControlMouseActivity widget) {
        final Bitmap bitmap = BitmapFactory.decodeByteArray(dataPackge.fileDatas,
                0, dataPackge.fileDatas.length, null);
        final Bitmap bitmap1 = BitmapUtils.rotateBitmap(bitmap, 90);
        widget.ivImg.setImageBitmap(bitmap1);
        try {
            JSONObject jObject = new JSONObject(dataPackge.data);
            jObject = jObject.getJSONObject("mouseInfo");
            Logger.i("shilec", "mouseInfo:" + jObject);
            int x = jObject.optInt("x");
            int y = jObject.optInt("y");
            widget.ivImg.setMousePoint(1080 - y, x);
            Logger.i("shilec", "x = " + x + ",y = " + y);
            Logger.i("shilec", "Ix = " + widget.ivImg.getWidth() + ",Iy = " + widget.ivImg.getHeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
