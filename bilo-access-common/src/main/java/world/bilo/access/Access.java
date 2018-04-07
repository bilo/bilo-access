/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.List;

import world.bilo.stack.Block;
import world.bilo.stack.stream.Stream;
import world.bilo.stack.stream.StreamBlocks;
import world.bilo.stack.support.PollTimer;
import world.bilo.stack.support.Time;
import world.bilo.util.SetChangeDetector;
import world.bilo.util.UniqueOrderedList;
import world.bilo.util.ValueSet;

public class Access implements DevicesEventHandler {
    private final Devices devices;
    private final StreamBlocks stack;
    private final Stream stream = new Stream() {
        @Override
        public void newData(List<Byte> list) {
            devices.write(list);
        }
    };
    private final PollTimer timer;
    private final LoggerDistributor stackDistributor = new LoggerDistributor();
    private final SetChangeDetector<Block> model = new SetChangeDetector<Block>(new ValueSet<Block>());
    private final UniqueOrderedList<ConnectionChangeObserver> connectionChangeObserver = new UniqueOrderedList<>();

    public Access(Devices devices, Time time) {
        devices.setEventHandler(this);
        this.devices = devices;
        timer = new PollTimer(time);
        stack = new StreamBlocks(stream, timer, stackDistributor);
    }

    public void calc() {
        devices.calc();
        timer.poll();
    }

    public SetChangeDetector<Block> getModel() {
        return model;
    }

    public StreamBlocks getStack() {
        return stack;
    }

    @Override
    public void turnedOn() {
        for (ConnectionChangeObserver observer : connectionChangeObserver) {
            observer.turnedOn();
        }
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
    public void received(List<Byte> data) {
        stack.newData(data);
        model.changed(new ValueSet<>(stack.getBlocks().items()));
    }

    public UniqueOrderedList<ConnectionChangeObserver> getConnectionChangeObserver() {
        return connectionChangeObserver;
    }

    public Devices getDevices() {
        return devices;
    }
}
