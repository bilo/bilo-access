/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth.event.supervisor;

public interface Visitor {
    void visit(Connected event);

    void visit(Connecting event);

    void visit(Disconnected event);

    void visit(Error event);

    void visit(Received event);
}
