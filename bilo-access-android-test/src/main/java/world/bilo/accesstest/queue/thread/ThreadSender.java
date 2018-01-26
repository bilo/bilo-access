/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.queue.thread;

import world.bilo.accesstest.queue.MessageSender;
import world.bilo.accesstest.queue.poll.PollSender;

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
