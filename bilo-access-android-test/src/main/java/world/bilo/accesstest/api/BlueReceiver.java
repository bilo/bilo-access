/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.api;

import android.os.Handler;
import android.os.Message;

import world.bilo.accesstest.DisconnectHandler;
import world.bilo.accesstest.MessageId;
import world.bilo.stack.stream.StreamBlocks;
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
        MessageId id = MessageId.values()[msg.what];

        switch (id) {
            case DEVICE_CONNECTED: {
                blocks.start();
                break;
            }
            case DEVICE_DISCONNECTED: {
                blocks.stop();
                disconnectHandler.disconnected();
                break;
            }
            case DATA_RECEIVED: {
                List<Byte> data = (List<Byte>) msg.obj;
                blocks.newData(data);
                break;
            }
        }
    }
}
