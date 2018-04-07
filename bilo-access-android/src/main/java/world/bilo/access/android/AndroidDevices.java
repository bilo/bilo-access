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
import world.bilo.access.Devices;
import world.bilo.access.DevicesEventHandler;
import world.bilo.access.NullDevicesEventHandler;

public class AndroidDevices implements Devices, Output, AdapterListener {
    private final Supervisor supervisor;
    private final Adapter adapter;
    private DevicesEventHandler handler = new NullDevicesEventHandler();
    private boolean connected = false;

    public AndroidDevices(int bluetoothEnableCode, Activity activity) {
        supervisor = new Supervisor(this);
        adapter = new ActivityAdapter(bluetoothEnableCode, this, activity);
    }

    @Override
    public void received(List<Byte> data) {
        handler.received(data);
    }

    @Override
    public void connected() {
        connected = true;
        handler.connected();
    }

    @Override
    public void disconnected() {
        connected = false;
        handler.disconnected();
    }

    @Override
    public void connecting(String message) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public void turnOn() {
        adapter.enable();
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
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void write(List<Byte> data) {
        supervisor.send(data);
    }

    @Override
    public void setEventHandler(DevicesEventHandler handler) {
        this.handler = handler;
    }

    @Override
    public void enabled(Adapter adapter) {
        handler.turnedOn();
    }
}

