/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.spp.queue.thread;

import ch.bitzgi.android.bluetooth.spp.queue.poll.PollReceiver;

public class ThreadReceiver<T> {
    private final PollReceiver<T> queue;

    public ThreadReceiver(PollReceiver<T> queue) {
        this.queue = queue;
    }

    public void handle() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            queue.poll();
        }
    }
}
