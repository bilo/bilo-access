/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.queue;

public interface MessageHandler<T> {
    void handle(T message);
}
