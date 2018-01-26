/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.api;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import world.bilo.accesstest.DisconnectHandler;
import world.bilo.accesstest.bluetooth.Supervisor;
import world.bilo.stack.Block;
import world.bilo.stack.Logger;
import world.bilo.stack.Timer;
import world.bilo.stack.stream.Stream;
import world.bilo.stack.stream.StreamBlocks;
import world.bilo.stack.support.JavaTime;
import world.bilo.stack.support.PollTimer;
import world.bilo.stack.utility.ObservableCollection;

import java.util.ArrayList;
import java.util.List;

public class Blocks {
    final private Logger logger = new AndroidLogger();
    final private Handler handler;
    final private Supervisor supervisor;

    public Blocks(DisconnectHandler disconnectHandler) {
        handler = new BlueReceiver(disconnectHandler);
        supervisor = new Supervisor(handler, logger);
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
