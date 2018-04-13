/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.List;

public class NullDevicesEventHandler implements DevicesEventHandler {
    @Override
    public void turnedOn() {

    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }

    @Override
    public void received(List<Byte> data) {

    }
}
