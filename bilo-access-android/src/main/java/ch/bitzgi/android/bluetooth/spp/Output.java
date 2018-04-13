/*
 * Copyright 2018 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package ch.bitzgi.android.bluetooth.spp;

import java.util.List;

public interface Output {
    public void received(List<Byte> data);

    public void connected();

    public void disconnected();

    public void connecting(String message);

    public void error(String message);
}
