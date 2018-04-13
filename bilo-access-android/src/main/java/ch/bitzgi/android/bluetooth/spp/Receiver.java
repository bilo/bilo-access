/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.spp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import ch.bitzgi.android.bluetooth.spp.event.supervisor.Event;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Received;
import ch.bitzgi.android.bluetooth.spp.event.worker.ReadError;
import ch.bitzgi.android.bluetooth.spp.queue.MessageSender;

class Receiver extends Thread {
    private final InputStream inStream;
    private final MessageSender<Event> toSupervisor;
    private final MessageSender<ch.bitzgi.android.bluetooth.spp.event.worker.Event> toWorker;

    public Receiver(InputStream inStream, MessageSender<Event> toSupervisor, MessageSender<ch.bitzgi.android.bluetooth.spp.event.worker.Event> toWorker) {
        this.inStream = inStream;
        this.toSupervisor = toSupervisor;
        this.toWorker = toWorker;
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
                toWorker.send(ReadError.Instance);
                break;
            }
        }
    }
}
