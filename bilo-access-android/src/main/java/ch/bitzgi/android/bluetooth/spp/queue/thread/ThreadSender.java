/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.spp.queue.thread;

import ch.bitzgi.android.bluetooth.spp.queue.MessageSender;
import ch.bitzgi.android.bluetooth.spp.queue.poll.PollSender;

public class ThreadSender<T> implements MessageSender<T> {
    private final PollSender<T> sender;
    private final Thread thread;

    public ThreadSender(PollSender<T> sender, Thread thread) {
        this.sender = sender;
        this.thread = thread;
    }

    @Override
    public void send(T message) {
        sender.send(message);
        thread.interrupt();
    }

}
