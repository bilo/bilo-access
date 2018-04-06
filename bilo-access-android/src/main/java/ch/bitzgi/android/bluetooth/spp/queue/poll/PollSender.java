/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.spp.queue.poll;

import java.util.concurrent.ConcurrentLinkedQueue;

import ch.bitzgi.android.bluetooth.spp.queue.MessageSender;

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
