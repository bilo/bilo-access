/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import ch.bitzgi.android.bluetooth.adapter.ActivityAdapter;
import ch.bitzgi.android.bluetooth.adapter.Adapter;
import ch.bitzgi.android.bluetooth.adapter.AdapterListener;
import ch.bitzgi.android.bluetooth.spp.Output;
import ch.bitzgi.android.bluetooth.spp.Supervisor;
import world.bilo.access.Device;
import world.bilo.access.DeviceEvents;
import world.bilo.access.Devices;

public class AndroidAccess implements Devices, Output {
    private final Supervisor supervisor;
    private final Adapter adapter;
    private final DeviceEvents listener;

    public AndroidAccess(DeviceEvents listener, Adapter adapter) {
        supervisor = new Supervisor(this);
        this.listener = listener;
        this.adapter = adapter;
    }

    public AndroidAccess(DeviceEvents listener, int bluetoothEnableCode, AdapterListener bluetoothEnableListener, Activity activity) {
        supervisor = new Supervisor(this);
        this.listener = listener;
        adapter = new ActivityAdapter(bluetoothEnableCode, bluetoothEnableListener, activity);
    }

    @Override
    public void received(List<Byte> data) {
        listener.received(data);
    }

    @Override
    public void connected() {
        listener.connected();
    }

    @Override
    public void disconnected() {
        listener.disconnected();
    }

    @Override
    public void connecting(String message) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public void calc() {
        supervisor.poll();
    }

    @Override
    public void disconnect() {
        supervisor.disconnect();
    }

    @Override
    public List<Device> getDevices() {
        List<Device> devices = new ArrayList<Device>();

        for (BluetoothDevice device : adapter.devices()) {
            devices.add(new AndroidDevice(device));
        }

        return devices;
    }

    @Override
    public void connect(Device device) {
        supervisor.connect(((AndroidDevice) device).getDevice());
    }

    @Override
    public void write(List<Byte> data) {
        supervisor.send(data);
    }

}

