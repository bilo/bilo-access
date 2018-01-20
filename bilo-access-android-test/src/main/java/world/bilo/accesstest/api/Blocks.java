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

import java.util.List;

public class Blocks {
    final private Stream output = new Stream() {
        @Override
        public void newData(List<Byte> data) {
            supervisor.newData(data);
        }
    };
    final private Timer timer = new PollTimer(new JavaTime());
    final private Logger logger = new AndroidLogger();
    final private StreamBlocks blocks = new StreamBlocks(output, timer, logger);
    final private Handler handler;
    final private Supervisor supervisor;

    public Blocks(DisconnectHandler disconnectHandler) {
        handler = new BlueReceiver(blocks, disconnectHandler);
        supervisor = new Supervisor(handler, logger);
    }

    public void disconnect() {
        supervisor.disconnect();
    }

    public void connect(BluetoothDevice device) {
        supervisor.connect(device);
    }

    public ObservableCollection<Block> getBlocks() {
        return blocks.getBlocks();
    }

    public Block getBase() {
        return blocks.getBase();
    }

}
