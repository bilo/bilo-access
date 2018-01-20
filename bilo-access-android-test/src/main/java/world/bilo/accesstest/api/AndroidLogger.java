/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.api;

import android.util.Log;
import world.bilo.stack.Logger;

public class AndroidLogger implements Logger {
    static final private String Name = "bilo.stackdemo";

    @Override
    public void error(String message) {
        Log.e(Name, message);
    }

    @Override
    public void debug(String message) {
        Log.d(Name, message);
    }
}
