/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: Apache-2.0
 */

package world.bilo.access.android.internal;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import world.bilo.stack.Logger;


public class Worker extends Thread {
  private final Logger logger;
  private final NonblockingReader dataListener;
  private final BluetoothSocket socket;
  private final InputStream inStream;
  private final OutputStream outStream;
  private volatile world.bilo.access.State state = world.bilo.access.State.Disconnected;

  public world.bilo.access.State getConnectionState() {
    return state;
  }

  public Worker(Logger logger, NonblockingReader dataListener, BluetoothDevice device) {
    this.logger = logger;
    this.dataListener = dataListener;

    socket = getBluetoothSocket(device);
    inStream = getInputStream(socket);
    outStream = getOutputStream(socket);
  }

  public void run() {
    logger.debug("bluetooth connection started");
    setName("ConnectThread");

    state = world.bilo.access.State.Connecting;

    if (!connect()) {
      state = world.bilo.access.State.Disconnected;
      logger.debug("bluetooth connection failed");
      return;
    }

    state = world.bilo.access.State.Connected;

    logger.debug("bluetooth connected");

    // Keep listening to the InputStream while connected
    while (state == world.bilo.access.State.Connected) {
      read();
    }

    logger.debug("bluetooth disconnecting");

    state = world.bilo.access.State.Disconnected;
  }

  private void read() {
    try {
      byte[] buffer = new byte[1024];
      int bytes;
      bytes = inStream.read(buffer);
      buffer = Arrays.copyOf(buffer, bytes);
      dataListener.write(arrayToList(buffer));
    } catch (IOException e) {
      if (state == world.bilo.access.State.Connected) {
        logger.error("bluetooth read error");
      }
      cancel();
    }
  }

  public void write(List<Byte> data) {
    try {
      outStream.write(listToArray(data));
    } catch (IOException e) {
      if (state == world.bilo.access.State.Connected) {
        logger.error("bluetooth write error");
      }
      cancel();
    }
  }

  public void cancel() {
    logger.debug("canceling bluetooth connection");
    state = world.bilo.access.State.Disconnecting;
    try {
      socket.close();
    } catch (IOException e) {
      logger.error("bluetooth close() of connect socket failed");
    }
  }

  private InputStream getInputStream(BluetoothSocket socket) {
    try {
      return socket.getInputStream();
    } catch (IOException e) {
      logger.error("could not get bluetooth input stream");
    }

    throw new RuntimeException("could not get input stream");
  }

  private OutputStream getOutputStream(BluetoothSocket socket) {
    try {
      return socket.getOutputStream();
    } catch (IOException e) {
      logger.error("could not get bluetooth output stream");
    }

    throw new RuntimeException("could not get output stream");
  }

  private BluetoothSocket getBluetoothSocket(BluetoothDevice device) {
    UUID sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    try {
      BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(sppUuid);
      logger.debug("created insecure socket with spp uuid");
      return socket;
    } catch (IOException e) {
      logger.error("bluetooth createInsecureRfcommSocketToServiceRecord failed");
    }

    try {
      BluetoothSocket socket = device.createRfcommSocketToServiceRecord(sppUuid);
      logger.debug("created secure socket with spp uuid");
      return socket;
    } catch (IOException e) {
      logger.error("bluetooth createRfcommSocketToServiceRecord failed");
    }

    try {
      Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
      BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
      logger.debug("created rfcomm socket with reflection");
      return socket;
    } catch (NoSuchMethodException e) {
      logger.error("bluetooth getMethod(createRfcommSocket) failed with " + e.getMessage());
    } catch (IllegalAccessException e) {
      logger.error("bluetooth getMethod(invoke) failed with " + e.getMessage());
    } catch (InvocationTargetException e) {
      logger.error("bluetooth getMethod(invoke) failed with " + e.getMessage());
    }

    throw new RuntimeException("Could not get bluetooth socket");
  }

  private boolean connect() {
    try {
      // This is a blocking call and will only return on a
      // successful connection or an exception
      socket.connect();
      return true;
    } catch (IOException e) {
      logger.debug("bluetooth unable to connect");
      // Close the socket
      try {
        socket.close();
      } catch (IOException e2) {
        logger.error("bluetooth unable to close() socket during connection failure");
      }
      return false;
    }
  }

  private ArrayList<Byte> arrayToList(byte[] message) {
    ArrayList<Byte> im = new ArrayList<Byte>();
    for (byte symbol : message) {
      im.add(symbol);
    }
    return im;
  }

  private byte[] listToArray(List<Byte> data) {
    byte[] message = new byte[data.size()];
    for (int i = 0; i < data.size(); i++) {
      message[i] = data.get(i);
    }
    return message;
  }

}
