/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android;

import android.util.Log;

import world.bilo.stack.Logger;

public class AndroidLogger implements Logger {
  static final private String Name = "bilo.library";

  public void error(String message) {
    Log.e(Name, message);
  }

  @Override
  public void debug(String message) {
    Log.d(Name, message);
  }
}
