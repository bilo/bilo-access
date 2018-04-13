/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp.event.worker;

public interface Visitor {
    void visit(Disconnect event);

    void visit(ReadError event);
}
