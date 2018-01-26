/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import java.util.List;

public class Supervisor {
    private final WorkHandler reader;
    private Worker worker = null;

    public Supervisor(Handler handler) {
        reader = new WorkHandler(handler);
    }

    synchronized public void connect(BluetoothDevice device) {
        assert (worker == null);
        reader.connecting("start connection");
        worker = new Worker(reader, device);
        worker.start();
    }

    synchronized public void disconnect() {
        if (worker != null) {

            worker.getQueue().send(Disconnect.Instance);

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
            w.write(data);
        }
    }

}
