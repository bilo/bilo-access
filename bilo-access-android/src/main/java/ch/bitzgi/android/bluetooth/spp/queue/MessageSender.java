/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp.queue;

public interface MessageSender<T> {
    public void send(T message);
}
