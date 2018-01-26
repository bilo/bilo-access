/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.api;

import android.os.Handler;

import java.util.List;

import world.bilo.accesstest.MessageId;
import world.bilo.accesstest.bluetooth.Output;

public class WorkHandler implements Output {
    private final Handler mHandler;

    public WorkHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void received(List<Byte> data) {
        mHandler.obtainMessage(MessageId.DATA_RECEIVED.ordinal(), data).sendToTarget();
    }

    @Override
    public void connected() {
        mHandler.obtainMessage(MessageId.DEVICE_CONNECTED.ordinal()).sendToTarget();
    }

    @Override
    public void disconnected() {
        mHandler.obtainMessage(MessageId.DEVICE_DISCONNECTED.ordinal()).sendToTarget();
    }

    @Override
    public void connecting(String message) {
        mHandler.obtainMessage(MessageId.DEVICE_CONNECTING.ordinal(), message).sendToTarget();
    }
}
