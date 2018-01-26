/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.List;

public class Supervisor {
    private final Output reader;
    private Worker worker = null;

    public Supervisor(Output handler) {
        reader = handler;
    }

    public void connect(BluetoothDevice device) {
        assert (worker == null);
        reader.connecting("start connection");
        worker = new Worker(reader, device);
        worker.start();
    }

    public void disconnect() {
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

    public void send(List<Byte> data) {
        if (worker != null) {
            worker.write(data);
        }
    }

}
