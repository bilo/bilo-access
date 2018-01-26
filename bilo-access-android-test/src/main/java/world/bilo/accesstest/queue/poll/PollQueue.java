/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.queue.poll;

import java.util.concurrent.ConcurrentLinkedQueue;

import world.bilo.accesstest.queue.MessageHandler;

public class PollQueue<T> {
    private final PollReceiver<T> receiver;
    private final PollSender<T> sender;

    public PollQueue(MessageHandler<T> handler) {
        ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();
        receiver = new PollReceiver<>(queue, handler);
        sender = new PollSender<>(queue);
    }

    public PollReceiver<T> getReceiver() {
        return receiver;
    }

    public PollSender<T> getSender() {
        return sender;
    }
}
