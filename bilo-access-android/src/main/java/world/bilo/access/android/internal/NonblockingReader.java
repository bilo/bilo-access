/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android.internal;

import java.util.ArrayList;
import java.util.List;

public class NonblockingReader {
  private List<Byte> buffer = new ArrayList<Byte>();

  public List<Byte> read() {
    List<Byte> result;
    List<Byte> newList = new ArrayList<Byte>();

    synchronized (buffer) {
      result = buffer;
      buffer = newList;
    }

    return result;
  }

  public void write(List<Byte> data) {
    synchronized (data) {
      buffer.addAll(data);
    }
  }

}
