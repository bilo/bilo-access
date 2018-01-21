/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

interface ReceiverHandler {
    void error(String message);

    void received(byte[] data);
}
