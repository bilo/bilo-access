/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

class Sender extends Thread {
    private final ConcurrentLinkedQueue<byte[]> incoming;
    private final OutputStream stream;
    private final SenderHandler handler;

    public Sender(ConcurrentLinkedQueue<byte[]> incoming, OutputStream stream, SenderHandler handler) {
        this.incoming = incoming;
        this.stream = stream;
        this.handler = handler;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                try {
                    sendFromQueue();
                } catch (IOException e1) {
                    handler.error(e1.getMessage());
                    break;
                }
            }
        }
    }

    private void sendFromQueue() throws IOException {
        while (!incoming.isEmpty()) {
            byte[] data = incoming.poll();
            stream.write(data);
        }
        stream.flush();
    }
}
