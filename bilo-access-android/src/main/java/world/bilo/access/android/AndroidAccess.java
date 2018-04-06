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
import ch.bitzgi.android.bluetooth.adapter.AdapterListener;
import ch.bitzgi.android.bluetooth.spp.Output;
import ch.bitzgi.android.bluetooth.spp.Supervisor;
import world.bilo.access.Access;
import world.bilo.access.ConnectionChangeObserver;
import world.bilo.access.Device;
import world.bilo.access.LoggerDistributor;
import world.bilo.access.android.internal.BlueDevice;
import world.bilo.stack.Block;
import world.bilo.stack.stream.Stream;
import world.bilo.stack.stream.StreamBlocks;
import world.bilo.stack.support.PollTimer;
import world.bilo.stack.support.Time;
import world.bilo.util.SetChangeDetector;
import world.bilo.util.UniqueOrderedList;
import world.bilo.util.ValueSet;

public class BiloAccessAndroid implements Access, Output {
    private final UniqueOrderedList<ConnectionChangeObserver> connectionChangeObserver = new UniqueOrderedList<>();
    private final Supervisor supervisor;
    private final ActivityAdapter adapter;
    private final PollTimer timer;
    private final LoggerDistributor stackDistributor = new LoggerDistributor();
    private final StreamBlocks stack;
    private final SetChangeDetector<Block> model = new SetChangeDetector<Block>(new ValueSet<Block>());
    private final Stream stream = new Stream() {
        @Override
        public void newData(List<Byte> list) {
            supervisor.send(list);
        }
    };

    public BiloAccessAndroid(int bluetoothEnableCode, AdapterListener bluetoothEnableListener, Activity activity, Time time) {
        supervisor = new Supervisor(this);
        adapter = new ActivityAdapter(bluetoothEnableCode, bluetoothEnableListener, activity);

        timer = new PollTimer(time);
        stack = new StreamBlocks(stream, timer, stackDistributor);
    }

    @Override
    public void received(List<Byte> data) {
        stack.newData(data);
        model.changed(new ValueSet<>(stack.getBlocks().items()));
    }

    @Override
    public void connected() {
        stack.start();
        for (ConnectionChangeObserver observer : connectionChangeObserver) {
            observer.connected();
        }
    }

    @Override
    public void disconnected() {
        for (ConnectionChangeObserver observer : connectionChangeObserver) {
            observer.disconnected();
        }
        stack.stop();
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
        timer.poll();
    }

    @Override
    public UniqueOrderedList<ConnectionChangeObserver> getConnectionChangeObserver() {
        return null; //FIXME implement this
    }

    @Override
    public void disconnect() {
        supervisor.disconnect();
    }

    @Override
    public List<Device> getDevices() {
        List<Device> devices = new ArrayList<Device>();

        for (BluetoothDevice device : adapter.devices()) {
            devices.add(new BlueDevice(device));
        }

        return devices;
    }

    @Override
    public void connect(Device device) {
        supervisor.connect(((BlueDevice) device).getDevice());
    }

}
