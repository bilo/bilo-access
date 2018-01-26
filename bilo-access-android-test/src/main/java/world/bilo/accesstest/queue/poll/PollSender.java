/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.queue.poll;

import java.util.concurrent.ConcurrentLinkedQueue;

import world.bilo.accesstest.queue.MessageSender;

public class PollSender<T> implements MessageSender<T> {
    private final ConcurrentLinkedQueue<T> queue;

    public PollSender(ConcurrentLinkedQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public void send(T message) {
        queue.offer(message);
    }

}
