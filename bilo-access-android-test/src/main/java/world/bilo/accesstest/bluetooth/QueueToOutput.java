/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.util.ArrayList;

import world.bilo.accesstest.bluetooth.event.Connected;
import world.bilo.accesstest.bluetooth.event.Connecting;
import world.bilo.accesstest.bluetooth.event.Disconnected;
import world.bilo.accesstest.bluetooth.event.Error;
import world.bilo.accesstest.bluetooth.event.Received;
import world.bilo.accesstest.bluetooth.event.ToSupervisor;
import world.bilo.accesstest.queue.MessageHandler;

class QueueToOutput implements MessageHandler<ToSupervisor> {
    private final Output output;

    public QueueToOutput(Output output) {
        this.output = output;
    }

    @Override
    public void handle(ToSupervisor message) {
        //FIXME use visitor pattern
        if (message instanceof Connected) {
            output.connected();
        } else if (message instanceof Connecting) {
            output.connecting(((Connecting) message).getMessage());
        } else if (message instanceof Disconnected) {
            output.disconnected();
        } else if (message instanceof Error) {
            output.error(((Error) message).getMessage());
        } else if (message instanceof Received) {
            output.received(arrayToList(((Received) message).getData()));
        }
    }

    private ArrayList<Byte> arrayToList(byte[] message) {
        ArrayList<Byte> im = new ArrayList<Byte>();
        for (byte symbol : message) {
            im.add(symbol);
        }
        return im;
    }

}
