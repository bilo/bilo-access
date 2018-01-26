/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.queue.thread;

import world.bilo.accesstest.queue.MessageHandler;
import world.bilo.accesstest.queue.poll.PollQueue;

public class ThreadQueue<T> {
    private final ThreadReceiver<T> receiver;
    private final ThreadSender<T> sender;

    public ThreadQueue(MessageHandler<T> handler, Thread thread) {
        PollQueue<T> pollQueue = new PollQueue<>(handler);
        receiver = new ThreadReceiver<>(pollQueue.getReceiver());
        sender = new ThreadSender<>(pollQueue.getSender(), thread);
    }

    public ThreadReceiver<T> getReceiver() {
        return receiver;
    }

    public ThreadSender<T> getSender() {
        return sender;
    }
}
