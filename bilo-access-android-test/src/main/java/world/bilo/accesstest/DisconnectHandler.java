/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest;

public interface DisconnectHandler {
    public void disconnected();

    public void connecting(String message);
}
