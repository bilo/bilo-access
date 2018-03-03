/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth.event.sender;

public interface Visitor {
    public void visit(Abort event);
    public void visit(Send event);
}
