/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth.event.supervisor;

import java.util.Arrays;

public class Received implements Event {
    private final byte[] data;

    public Received(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Received received = (Received) o;

        return Arrays.equals(data, received.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
