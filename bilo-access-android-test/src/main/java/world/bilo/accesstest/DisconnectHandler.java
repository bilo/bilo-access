/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest;

import java.util.List;

public interface DisconnectHandler {
    public void disconnected();

    public void connecting(String message);

    void connected();

    void received(List<Byte> data);
}
