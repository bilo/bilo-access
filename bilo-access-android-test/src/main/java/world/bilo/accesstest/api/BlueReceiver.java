/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.api;

import android.os.Handler;
import android.os.Message;

import java.util.List;

import world.bilo.accesstest.DisconnectHandler;
import world.bilo.accesstest.MessageId;

public class BlueReceiver extends Handler {
    private final DisconnectHandler disconnectHandler;

    public BlueReceiver(DisconnectHandler disconnectHandler) {
        this.disconnectHandler = disconnectHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        MessageId id = MessageId.values()[msg.what];

        switch (id) {
            case DEVICE_CONNECTED: {
                disconnectHandler.connected();
                break;
            }
            case DEVICE_DISCONNECTED: {
                disconnectHandler.disconnected();
                break;
            }
            case DEVICE_CONNECTING: {
                disconnectHandler.connecting((String) msg.obj);
                break;
            }
            case DATA_RECEIVED: {
                List<Byte> data = (List<Byte>) msg.obj;
                disconnectHandler.received(data);
                break;
            }
        }
    }
}
