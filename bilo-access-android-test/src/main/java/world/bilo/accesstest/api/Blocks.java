/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.api;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import java.util.List;

import world.bilo.accesstest.DisconnectHandler;
import world.bilo.accesstest.bluetooth.Supervisor;

public class Blocks {
    final private Handler handler;
    final private Supervisor supervisor;

    public Blocks(DisconnectHandler disconnectHandler) {
        handler = new BlueReceiver(disconnectHandler);
        supervisor = new Supervisor(handler);
    }

    public void disconnect() {
        supervisor.disconnect();
    }

    public void connect(BluetoothDevice device) {
        supervisor.connect(device);
    }

    public void send(List<Byte> data) {
        supervisor.newData(data);
    }
}
