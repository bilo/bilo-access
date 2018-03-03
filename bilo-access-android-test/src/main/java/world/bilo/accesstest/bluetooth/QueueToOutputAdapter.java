/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import world.bilo.accesstest.bluetooth.event.supervisor.Event;
import world.bilo.accesstest.bluetooth.event.supervisor.Visitor;
import world.bilo.accesstest.queue.MessageHandler;

class QueueToOutputAdapter implements MessageHandler<Event> {
    private final Visitor dispatcher;

    public QueueToOutputAdapter(Visitor dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Event message) {
        message.accept(dispatcher);
    }
}

