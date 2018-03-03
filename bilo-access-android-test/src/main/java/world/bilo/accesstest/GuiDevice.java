/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest;

import android.bluetooth.BluetoothDevice;

class GuiDevice {
    public final BluetoothDevice device;

    GuiDevice(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return device.getName() + "\n" + device.getAddress();
    }
}
