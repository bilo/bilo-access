/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access;

import java.util.List;

public interface DeviceEvents
{
    void connected();

    void disconnected();

    public void received(List<Byte> data);
}
