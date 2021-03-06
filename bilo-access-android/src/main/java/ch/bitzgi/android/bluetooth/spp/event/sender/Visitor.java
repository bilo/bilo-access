/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp.event.sender;

public interface Visitor {
    public void visit(Abort event);

    public void visit(Send event);
}
