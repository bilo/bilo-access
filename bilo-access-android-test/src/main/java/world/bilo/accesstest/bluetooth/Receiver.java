/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

class Receiver extends Thread {
    private final InputStream inStream;
    private final ReceiverHandler handler;

    public Receiver(InputStream inStream, ReceiverHandler handler) {
        this.inStream = inStream;
        this.handler = handler;
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                int bytes;
                bytes = inStream.read(buffer);
                buffer = Arrays.copyOf(buffer, bytes);
                handler.received(buffer);
            } catch (IOException e) {
                handler.error(e.getMessage());
                break;
            }
        }
    }
}
