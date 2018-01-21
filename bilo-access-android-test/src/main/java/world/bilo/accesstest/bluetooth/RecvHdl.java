/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.util.concurrent.ConcurrentLinkedQueue;

@Deprecated
class RecvHdl implements ReceiverHandler, SenderHandler {
    private final ConcurrentLinkedQueue<Event> toWorker;
    private final Thread worker;

    public RecvHdl(ConcurrentLinkedQueue<Event> toWorker, Thread worker) {
        this.toWorker = toWorker;
        this.worker = worker;
    }

    @Override
    public void error(String message) {
        send(new Error(message));
    }

    @Override
    public void received(byte[] data) {
        send(new Received(data));
    }

    private void send(Event event) {
        toWorker.offer(event);
        worker.interrupt();
    }

}
