/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package ch.bitzgi.android.bluetooth.spp.event.worker;

public class Disconnect implements Event {
    static public final Disconnect Instance = new Disconnect();

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(this.getClass());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
