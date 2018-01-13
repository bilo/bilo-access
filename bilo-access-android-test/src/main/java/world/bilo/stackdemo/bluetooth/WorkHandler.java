/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo.bluetooth;

import android.os.Handler;

import world.bilo.stackdemo.MessageId;

import java.util.List;

public class WorkHandler {
    private final Handler mHandler;

    public WorkHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }


    public void write(List<Byte> data) {
        mHandler.obtainMessage(MessageId.DATA_RECEIVED, data).sendToTarget();
    }

    public void connected() {
        mHandler.obtainMessage(MessageId.DEVICE_CONNECTED).sendToTarget();
    }

    public void disconnected() {
        mHandler.obtainMessage(MessageId.DEVICE_DISCONNECTED).sendToTarget();
    }
}
