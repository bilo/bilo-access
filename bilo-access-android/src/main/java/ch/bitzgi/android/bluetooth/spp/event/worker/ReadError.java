/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp.event.worker;

public class ReadError implements Event {
    static public final ReadError Instance = new ReadError();

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
