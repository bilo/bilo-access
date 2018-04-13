/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp.event.supervisor;

public interface Visitor {
    void visit(Connected event);

    void visit(Connecting event);

    void visit(Disconnected event);

    void visit(Error event);

    void visit(Received event);
}
