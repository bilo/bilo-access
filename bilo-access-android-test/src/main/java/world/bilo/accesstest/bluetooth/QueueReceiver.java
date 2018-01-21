/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.util.concurrent.ConcurrentLinkedQueue;

class QueueReceiver<T> {
    private final ConcurrentLinkedQueue<T> queue;
    private final QueueHandler<T> handler;

    QueueReceiver(ConcurrentLinkedQueue<T> queue, QueueHandler<T> handler) {
        this.queue = queue;
        this.handler = handler;
    }

    public void handle() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            while (!queue.isEmpty()) {
                T message = queue.poll();
                handler.handle(message);
            }
        }
    }
}
