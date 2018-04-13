/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp;

import java.util.ArrayList;

import ch.bitzgi.android.bluetooth.spp.event.supervisor.Connected;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Connecting;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Disconnected;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Error;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Received;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Visitor;

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
