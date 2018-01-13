/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.stackdemo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import world.bilo.stackdemo.DeviceEntry;

import java.util.ArrayList;
import java.util.List;

public class Devices {

    static public List<DeviceEntry> list() {
        List<DeviceEntry> devices = new ArrayList<DeviceEntry>();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            for (BluetoothDevice device : adapter.getBondedDevices()) {
                devices.add(new DeviceEntry(device.getName(), device.getAddress()));
            }
        }
        return devices;
    }

    static public BluetoothDevice find(String address) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter != null) {
            for (BluetoothDevice device : adapter.getBondedDevices()) {
                if (address.equals(device.getAddress())) {
                    return device;
                }
            }
        }

        return null;
    }

}
