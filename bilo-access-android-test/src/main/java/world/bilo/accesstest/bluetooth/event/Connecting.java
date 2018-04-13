/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth.event;

public class Connecting implements ToSupervisor {
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
}
