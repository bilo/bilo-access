/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import world.bilo.accesstest.queue.MessageSender;

@Deprecated
class RecvHdl implements ReceiverHandler, SenderHandler {
    private final MessageSender<Event> sender;

    public RecvHdl(MessageSender<Event> sender) {
        this.sender = sender;
    }

    @Override
    public void error(String message) {
        sender.send(new Error(message));
    }

    @Override
    public void received(byte[] data) {
        sender.send(new Received(data));
    }

}
