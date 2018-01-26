/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

import java.util.List;

public interface Output {
    public void received(List<Byte> data);

    public void connected();

    public void disconnected();

    public void connecting(String message);

}
