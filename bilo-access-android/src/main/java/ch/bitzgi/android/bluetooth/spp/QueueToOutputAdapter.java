/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp;

import ch.bitzgi.android.bluetooth.spp.event.supervisor.Event;
import ch.bitzgi.android.bluetooth.spp.event.supervisor.Visitor;
import ch.bitzgi.android.bluetooth.spp.queue.MessageHandler;

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

