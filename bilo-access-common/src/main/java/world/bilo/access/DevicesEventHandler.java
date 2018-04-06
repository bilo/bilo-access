/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.List;

public interface DevicesEventHandler {
    public void connected();

    public void disconnected();

    public void received(List<Byte> data);
}
