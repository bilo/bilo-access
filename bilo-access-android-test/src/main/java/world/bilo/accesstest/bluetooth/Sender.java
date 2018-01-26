/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.io.IOException;
import java.io.OutputStream;

import world.bilo.accesstest.queue.MessageSender;
import world.bilo.accesstest.queue.thread.ThreadQueue;
import world.bilo.accesstest.queue.MessageHandler;

class Sender extends Thread implements MessageHandler<byte[]> {
    private final ThreadQueue<byte[]> queue = new ThreadQueue<>(this, this);
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

    public MessageSender<byte[]> getQueue() {
        return queue.getSender();
    }
}
