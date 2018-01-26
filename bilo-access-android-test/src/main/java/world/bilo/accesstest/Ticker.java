/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest;

import android.os.Handler;
import android.os.Message;

class Ticker extends Handler {
    private static final int Interval = 200;
    private final TickHandler handler;

    Ticker(TickHandler handler) {
        this.handler = handler;
    }

    public void start() {
        sendEmptyMessageDelayed(MessageId.TICK.ordinal(), Interval);
    }

    @Override
    public void handleMessage(Message msg) {
        MessageId code = MessageId.values()[msg.what];

        switch (code) {
            case TICK:
                sendEmptyMessageDelayed(MessageId.TICK.ordinal(), Interval);
                handler.tick();
                break;
        }
    }
}
