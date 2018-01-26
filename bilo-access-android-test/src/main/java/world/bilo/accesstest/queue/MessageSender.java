/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.queue;

public interface MessageSender<T> {
    public void send(T message);
}
