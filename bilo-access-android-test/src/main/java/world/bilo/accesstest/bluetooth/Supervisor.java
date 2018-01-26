/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import world.bilo.accesstest.bluetooth.event.Connecting;
import world.bilo.accesstest.bluetooth.event.Disconnect;
import world.bilo.accesstest.bluetooth.event.Send;
import world.bilo.accesstest.bluetooth.event.ToSupervisor;
import world.bilo.accesstest.queue.poll.PollQueue;

public class Supervisor implements Input {
    private final PollQueue<ToSupervisor> queue;
    private Worker worker = null;

    public Supervisor(Output output) {
        queue = new PollQueue<>(new QueueToOutput(output));
    }

    @Override
    public void connect(BluetoothDevice device) {
        assert (worker == null);
        queue.getSender().send(new Connecting("start connection"));
        worker = new Worker(queue.getSender(), device);
        worker.start();
    }

    @Override
    public void disconnect() {
        if (worker != null) {
            worker.getQueue().send(Disconnect.Instance);
            worker = null;
        }
    }

    @Override
    public void send(List<Byte> data) {
        if (worker != null) {
            Send message = new Send(listToArray(data));
            worker.getSendQueue().send(message);
        }
    }

    @Override
    public void poll() {
        queue.getReceiver().poll();
    }

    private byte[] listToArray(List<Byte> data) {
        byte[] message = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            message[i] = data.get(i);
        }
        return message;
    }


}
