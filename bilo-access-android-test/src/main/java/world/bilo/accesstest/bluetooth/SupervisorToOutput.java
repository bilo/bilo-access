/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.util.ArrayList;

import world.bilo.accesstest.bluetooth.event.supervisor.Connected;
import world.bilo.accesstest.bluetooth.event.supervisor.Connecting;
import world.bilo.accesstest.bluetooth.event.supervisor.Disconnected;
import world.bilo.accesstest.bluetooth.event.supervisor.Error;
import world.bilo.accesstest.bluetooth.event.supervisor.Received;
import world.bilo.accesstest.bluetooth.event.supervisor.Visitor;

class SupervisorToOutput implements Visitor {
    private final Output output;

    public SupervisorToOutput(Output output) {
        this.output = output;
    }

    @Override
    public void visit(Connected event) {
        output.connected();
    }

    @Override
    public void visit(Connecting event) {
        output.connecting(event.getMessage());
    }

    @Override
    public void visit(Disconnected event) {
        output.disconnected();
    }

    @Override
    public void visit(Error event) {
        output.error(event.getMessage());
    }

    @Override
    public void visit(Received event) {
        output.received(arrayToList(event.getData()));
    }

    private ArrayList<Byte> arrayToList(byte[] message) {
        ArrayList<Byte> im = new ArrayList<Byte>();
        for (byte symbol : message) {
            im.add(symbol);
        }
        return im;
    }
}
