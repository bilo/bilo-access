/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp;

import java.io.IOException;
import java.io.OutputStream;

import ch.bitzgi.android.bluetooth.spp.event.sender.Abort;
import ch.bitzgi.android.bluetooth.spp.event.sender.Send;
import ch.bitzgi.android.bluetooth.spp.event.sender.Visitor;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Error;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Event;
import ch.bitzgi.android.bluetooth.spp.queue.MessageHandler;
import ch.bitzgi.android.bluetooth.spp.queue.MessageSender;
import ch.bitzgi.android.bluetooth.spp.queue.thread.ThreadQueue;

class Sender extends Thread implements MessageHandler<ch.bitzgi.android.bluetooth.spp.event.sender.Event> {
    private final ThreadQueue<ch.bitzgi.android.bluetooth.spp.event.sender.Event> queue = new ThreadQueue<>(this, this);
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
    public void handle(ch.bitzgi.android.bluetooth.spp.event.sender.Event message) {
        message.accept(dispatcher);
    }

    public MessageSender<ch.bitzgi.android.bluetooth.spp.event.sender.Event> getQueue() {
        return queue.getSender();
    }
}
