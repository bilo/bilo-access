/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.spp.queue.poll;

import java.util.concurrent.ConcurrentLinkedQueue;

import ch.bitzgi.android.bluetooth.spp.queue.MessageHandler;

public class PollReceiver<T> {
    private final ConcurrentLinkedQueue<T> queue;
    private final MessageHandler<T> handler;

    public PollReceiver(ConcurrentLinkedQueue<T> queue, MessageHandler<T> handler) {
        this.queue = queue;
        this.handler = handler;
    }

    public void poll() {
        while (!queue.isEmpty()) {
            T message = queue.poll();
            handler.handle(message);
        }
    }
}
