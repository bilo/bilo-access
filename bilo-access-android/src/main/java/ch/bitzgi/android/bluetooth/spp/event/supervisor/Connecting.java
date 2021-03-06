/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp.event.supervisor;

public class Connecting implements Event {
    private final String message;

    public Connecting(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connecting error = (Connecting) o;

        return message.equals(error.message);
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
