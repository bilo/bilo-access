/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.io.IOException;
import java.io.OutputStream;

import world.bilo.accesstest.bluetooth.event.sender.Abort;
import world.bilo.accesstest.bluetooth.event.sender.Send;
import world.bilo.accesstest.bluetooth.event.sender.Visitor;
import world.bilo.accesstest.bluetooth.event.supervisor.Error;
import world.bilo.accesstest.bluetooth.event.supervisor.Event;
import world.bilo.accesstest.queue.MessageHandler;
import world.bilo.accesstest.queue.MessageSender;
import world.bilo.accesstest.queue.thread.ThreadQueue;

class Sender extends Thread implements MessageHandler<world.bilo.accesstest.bluetooth.event.sender.Event> {
    private final ThreadQueue<world.bilo.accesstest.bluetooth.event.sender.Event> queue = new ThreadQueue<>(this, this);
    private final OutputStream stream;
    private final MessageSender<Event> toSupervisor;
    private boolean running = false;
    private final Visitor dispatcher = new Visitor() {
        @Override
        public void visit(Abort event) {
            running = false;
        }

        @Override
        public void visit(Send event) {
            byte[] data = event.getData();
            try {
                stream.write(data);
            } catch (IOException e1) {
                toSupervisor.send(new Error(e1.getMessage()));
            }
        }
    };

    public Sender(OutputStream stream, MessageSender<Event> toSupervisor) {
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
    public void handle(world.bilo.accesstest.bluetooth.event.sender.Event message) {
        message.accept(dispatcher);
    }

    public MessageSender<world.bilo.accesstest.bluetooth.event.sender.Event> getQueue() {
        return queue.getSender();
    }
}
