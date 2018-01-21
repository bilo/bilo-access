/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Queue<T> {
    private final QueueReceiver<T> receiver;
    private final QueueSender<T> sender;

    public Queue(QueueHandler<T> handler, Thread thread) {
        ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();
        receiver = new QueueReceiver<>(queue, handler);
        sender = new QueueSender<>(queue, thread);
    }

    public QueueReceiver<T> getReceiver() {
        return receiver;
    }

    public QueueSender<T> getSender() {
        return sender;
    }
}
