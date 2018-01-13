/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android.internal;

import android.bluetooth.BluetoothDevice;

import java.util.List;


import world.bilo.access.PollStream;
import world.bilo.access.State;

public class Supervisor implements PollStream {
  private final NonblockingReader reader = new NonblockingReader();
  private Worker worker = null;

  synchronized public State getState() {
    if (worker == null) {
      return State.Disconnected;
    } else {
      return worker.getConnectionState();
    }
  }

  synchronized public void connect(BluetoothDevice device) {
    assert (worker == null);
    worker = new Worker(new AndroidLogger(), reader, device);
    worker.start();
  }

  synchronized public void disconnect() {
    if (worker != null) {
      worker.cancel();
      worker = null;
    }
  }

  @Override
  public List<Byte> read() {
    return reader.read();
  }

  @Override
  public void newData(List<Byte> data) {
    Worker w = worker;
    if (w != null) {
      // Perform the write unsynchronized
      w.write(data);
    }
  }

}
