/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import world.bilo.accesstest.bluetooth.event.Error;
import world.bilo.accesstest.bluetooth.event.Received;
import world.bilo.accesstest.bluetooth.event.ToSupervisor;
import world.bilo.accesstest.queue.MessageSender;

class Receiver extends Thread {
    private final InputStream inStream;
    private final MessageSender<ToSupervisor> toSupervisor;

    public Receiver(InputStream inStream, MessageSender<ToSupervisor> toSupervisor) {
        this.inStream = inStream;
        this.toSupervisor = toSupervisor;
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                int bytes;
                bytes = inStream.read(buffer);
                buffer = Arrays.copyOf(buffer, bytes);
                toSupervisor.send(new Received(buffer));
            } catch (IOException e) {
                toSupervisor.send(new Error(e.getMessage()));
                break;
            }
        }
    }
}
