/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.util.concurrent.ConcurrentLinkedQueue;

@Deprecated
class RecvHdl implements ReceiverHandler, SenderHandler {
    private final QueueSender<Event> sender;

    public RecvHdl(QueueSender<Event> sender) {
        this.sender = sender;
    }

    public RecvHdl(ConcurrentLinkedQueue<Event> toWorker, Thread worker) {
        sender = new QueueSender<>(toWorker, worker);
    }

    @Override
    public void error(String message) {
        sender.send(new Error(message));
    }

    @Override
    public void received(byte[] data) {
        sender.send(new Received(data));
    }

}
