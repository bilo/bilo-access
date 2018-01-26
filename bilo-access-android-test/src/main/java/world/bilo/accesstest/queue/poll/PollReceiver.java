/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.queue.poll;

import java.util.concurrent.ConcurrentLinkedQueue;

import world.bilo.accesstest.queue.MessageHandler;

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
