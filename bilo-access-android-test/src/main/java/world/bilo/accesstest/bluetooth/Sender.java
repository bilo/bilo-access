/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.io.IOException;
import java.io.OutputStream;

import world.bilo.accesstest.queue.Queue;
import world.bilo.accesstest.queue.QueueHandler;
import world.bilo.accesstest.queue.QueueSender;

class Sender extends Thread implements QueueHandler<byte[]> {
    private final Queue<byte[]> queue = new Queue<>(this, this);
    private final OutputStream stream;
    private final SenderHandler handler;

    public Sender(OutputStream stream, SenderHandler handler) {
        this.stream = stream;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (true) {
            queue.getReceiver().handle();
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
        return queue.getSender();
    }
}
