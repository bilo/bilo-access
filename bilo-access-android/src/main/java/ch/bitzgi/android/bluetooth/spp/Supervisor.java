/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import ch.bitzgi.android.bluetooth.spp.event.sender.Send;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Connecting;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Event;
import ch.bitzgi.android.bluetooth.spp.event.worker.Disconnect;
import ch.bitzgi.android.bluetooth.spp.queue.poll.PollQueue;

public class Supervisor implements Input {
    private final PollQueue<Event> queue;
    private Worker worker = null;

    public Supervisor(Output output) {
        queue = new PollQueue<>(new QueueToOutputAdapter(new SupervisorToOutput(output)));
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
