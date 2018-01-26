/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.io.IOException;
import java.io.OutputStream;

import world.bilo.accesstest.bluetooth.event.Abort;
import world.bilo.accesstest.bluetooth.event.Error;
import world.bilo.accesstest.bluetooth.event.Send;
import world.bilo.accesstest.bluetooth.event.ToSender;
import world.bilo.accesstest.bluetooth.event.ToSupervisor;
import world.bilo.accesstest.queue.MessageHandler;
import world.bilo.accesstest.queue.MessageSender;
import world.bilo.accesstest.queue.thread.ThreadQueue;

class Sender extends Thread implements MessageHandler<ToSender> {
    private final ThreadQueue<ToSender> queue = new ThreadQueue<>(this, this);
    private final OutputStream stream;
    private final MessageSender<ToSupervisor> toSupervisor;
    private boolean running = false;

    public Sender(OutputStream stream, MessageSender<ToSupervisor> toSupervisor) {
        this.stream = stream;
        this.toSupervisor = toSupervisor;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            queue.getReceiver().handle();
        }
    }

    @Override
    public void handle(ToSender message) {
        //FIXME use visitor pattern
        if (message instanceof Send) {
            byte[] data = ((Send) message).getData();
            try {
                stream.write(data);
            } catch (IOException e1) {
                toSupervisor.send(new Error(e1.getMessage()));
            }
        } else if (message instanceof Abort) {
            running = false;
        }
    }

    public MessageSender<ToSender> getQueue() {
        return queue.getSender();
    }
}
