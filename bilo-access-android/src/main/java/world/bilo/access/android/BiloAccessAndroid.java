/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android;

import world.bilo.access.Bilo;
import world.bilo.access.android.internal.AndroidLogger;
import world.bilo.access.android.internal.BlueDevices;
import world.bilo.access.android.internal.Supervisor;
import world.bilo.stack.support.JavaTime;

public class BiloAccessAndroid {
  private Supervisor blueCom = null;
  private Bilo bilo = null;

  public void create() {
    JavaTime time = new JavaTime();
    blueCom = new Supervisor();
    bilo = new Bilo(new BlueDevices(blueCom), time);
    bilo.getLoggerObservable().addListener(new AndroidLogger());
  }

  public void destroy() {
    if (blueCom != null) {
      blueCom.disconnect();
    }
  }

  public Bilo getAccess() {
    return bilo;
  }

}
