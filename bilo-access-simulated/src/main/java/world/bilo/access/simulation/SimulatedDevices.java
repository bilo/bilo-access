/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import world.bilo.access.Device;
import world.bilo.access.Devices;
import world.bilo.access.DevicesEventHandler;
import world.bilo.access.NullDevicesEventHandler;
import world.bilo.stack.support.Time;

public class SimulatedDevices implements Devices {
    private final List<SimulatedDevice> simulatedDevices;
    private final Time time;
    private DevicesEventHandler handler = new NullDevicesEventHandler();
    private Stream stream = new NullStream();
    private final LinkedList<Event> events = new LinkedList<>();

    public SimulatedDevices(SimpleModels models, Time time) {
        simulatedDevices = createDeviceList(models);
        this.time = time;
    }

    @Override
    public void disconnect() {
        stream = new NullStream();
        events.add(Event.Disconnected);
    }

    @Override
    public void write(List<Byte> data) {
        stream.write(data);
    }

    @Override
    public void setEventHandler(DevicesEventHandler handler) {
        this.handler = handler;
    }

    @Override
    public void turnOn() {
        events.add(Event.TurnedOn);
    }

    @Override
    public void calc() {
        List<Byte> received = stream.read();
        if (!received.isEmpty()) {
            handler.received(received);
        }


        while (!events.isEmpty()) {
            Event event = events.pop();

            switch (event) {
                case TurnedOn:
                    handler.turnedOn();
                    break;
                case Connected:
                    handler.connected();
                    break;
                case Disconnected:
                    handler.disconnected();
                    break;
            }
        }
    }

    @Override
    public List<SimulatedDevice> getDevices() {
        return simulatedDevices;
    }

    @Override
    public void connect(Device device) {
        stream = new PlayingSimulator(time, ModelSerializer.serialize(((SimulatedDevice) device).getModel().blocks()));
        events.add(Event.Connected);
    }

    private List<SimulatedDevice> createDeviceList(SimpleModels models) {
        List<SimulatedDevice> result = new ArrayList<>();

        for (SimpleModel model : models.getModels()) {
            result.add(new SimulatedDevice(model));
        }

        return result;
    }

}
