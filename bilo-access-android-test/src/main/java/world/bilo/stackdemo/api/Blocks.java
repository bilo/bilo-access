/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo.api;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import com.bloctesian.Block;
import com.bloctesian.Logger;
import com.bloctesian.Timer;
import world.bilo.stackdemo.DisconnectHandler;
import world.bilo.stackdemo.bluetooth.Supervisor;
import com.bloctesian.stream.Stream;
import com.bloctesian.stream.StreamBlocks;
import com.bloctesian.support.JavaTime;
import com.bloctesian.support.PollTimer;
import com.bloctesian.utility.ObservableCollection;

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
