/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

class Sender extends Thread implements QueueHandler<byte[]> {
    private final ConcurrentLinkedQueue<byte[]> incoming = new ConcurrentLinkedQueue<>();
    private final QueueReceiver<byte[]> queueReceiver = new QueueReceiver<>(incoming, this);
    private final QueueSender<byte[]> queueSender = new QueueSender<>(incoming, this);
    private final OutputStream stream;
    private final SenderHandler handler;

    public Sender(OutputStream stream, SenderHandler handler) {
        this.stream = stream;
        this.handler = handler;
    }

    public void run() {
        while (true) {
            queueReceiver.handle();
        }
    }

    @Override
    public void handle(byte[] message) {
        try {
            stream.write(message);
        } catch (IOException e1) {
            handler.error(e1.getMessage());
        }
    }

    public QueueSender<byte[]> getQueue() {
        return queueSender;
    }
}
