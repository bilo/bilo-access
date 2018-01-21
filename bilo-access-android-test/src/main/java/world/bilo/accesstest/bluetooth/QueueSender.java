/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.util.concurrent.ConcurrentLinkedQueue;

class QueueSender<T> {
    private final ConcurrentLinkedQueue<T> queue;
    private final Thread thread;

    public QueueSender(ConcurrentLinkedQueue<T> queue, Thread thread) {
        this.queue = queue;
        this.thread = thread;
    }

    public void send(T message) {
        queue.offer(message);
        thread.interrupt();
    }

}
