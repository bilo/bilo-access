/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo.api;

import android.os.Handler;
import android.os.Message;

import world.bilo.stackdemo.DisconnectHandler;
import world.bilo.stackdemo.MessageId;
import com.bloctesian.stream.StreamBlocks;

import java.util.List;

public class BlueReceiver extends Handler {
    private final StreamBlocks blocks;
    private final DisconnectHandler disconnectHandler;

    public BlueReceiver(StreamBlocks blocks, DisconnectHandler disconnectHandler) {
        this.blocks = blocks;
        this.disconnectHandler = disconnectHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MessageId.DEVICE_CONNECTED: {
                blocks.start();
                break;
            }
            case MessageId.DEVICE_DISCONNECTED: {
                blocks.stop();
                disconnectHandler.disconnected();
                break;
            }
            case MessageId.DATA_RECEIVED: {
                List<Byte> data = (List<Byte>) msg.obj;
                blocks.newData(data);
                break;
            }
        }
    }
}
