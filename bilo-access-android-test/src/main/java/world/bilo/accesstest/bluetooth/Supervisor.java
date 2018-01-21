/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import world.bilo.stack.Logger;

public class Supervisor {
    private final WorkHandler reader;
    private final Logger logger;
    private Worker worker = null;
    private final ConcurrentLinkedQueue<Event> toWorker = new ConcurrentLinkedQueue<>();

    public Supervisor(Handler handler, Logger logger) {
        reader = new WorkHandler(handler);
        this.logger = logger;
    }

    synchronized public void connect(BluetoothDevice device) {
        assert (worker == null);
        reader.connecting("start connection");
        worker = new Worker(logger, reader, device, toWorker);
        worker.start();
    }

    synchronized public void disconnect() {
        if (worker != null) {

//            worker.cancel();
            toWorker.offer(Disconnect.Instance);
            worker.interrupt();

            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            worker = null;
        }
    }

    public void newData(List<Byte> data) {
        Worker w = worker;
        if (w != null) {
            // Perform the write unsynchronized
            w.write(data);
        }
    }

}
