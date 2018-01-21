/*
 * Copyright 2017 Urs Fässler
 * SPDX-License-Identifier: GPL-3.0
 */

package world.bilo.accesstest.bluetooth;

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
import java.util.concurrent.ConcurrentLinkedQueue;

import world.bilo.stack.Logger;

interface ReceiverHandler {
    void error(String message);

    void received(byte[] data);
}

class RecvHdl implements ReceiverHandler {
    private final ConcurrentLinkedQueue<Event> toWorker;
    private final Thread worker;

    public RecvHdl(ConcurrentLinkedQueue<Event> toWorker, Thread worker) {
        this.toWorker = toWorker;
        this.worker = worker;
    }

    @Override
    public void error(String message) {
        send(new Error(message));
    }

    @Override
    public void received(byte[] data) {
        send(new Received(data));
    }

    private void send(Event event) {
        toWorker.offer(event);
        worker.interrupt();
    }

}

class Receiver extends Thread {
    private final InputStream inStream;
    private final ReceiverHandler handler;

    public Receiver(InputStream inStream, ReceiverHandler handler) {
        this.inStream = inStream;
        this.handler = handler;
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                int bytes;
                bytes = inStream.read(buffer);
                buffer = Arrays.copyOf(buffer, bytes);
                handler.received(buffer);
            } catch (IOException e) {
                handler.error(e.getMessage());
                break;
            }
        }
    }

}

public class Worker extends Thread {
    private final Logger logger;
    private final WorkHandler dataListener;
    private final BluetoothDevice device;
    private final ConcurrentLinkedQueue<Event> incoming;
    private BluetoothSocket socket;
    private OutputStream outStream;
    private Receiver receiver;

    public Worker(Logger logger, WorkHandler dataListener, BluetoothDevice device, ConcurrentLinkedQueue<Event> incoming) {
        this.logger = logger;
        this.dataListener = dataListener;
        this.device = device;
        this.incoming = incoming;
    }

    public void run() {
        dataListener.connecting("connection started");

        socket = getBluetoothSocket(device);
        InputStream inStream = getInputStream(socket);
        outStream = getOutputStream(socket);

        logger.debug("bluetooth connection started");
        setName("ConnectThread");

        if (!connect()) {
            dataListener.connecting("connection failed");
            logger.debug("bluetooth connection failed");
            dataListener.disconnected();
            return;
        }

        dataListener.connecting("connected");
        dataListener.connected();

        logger.debug("bluetooth connected");

        RecvHdl handler = new RecvHdl(incoming, this);
        receiver = new Receiver(inStream, handler);
        receiver.start();

        while (socket.isConnected()) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                while (!incoming.isEmpty()) {
                    Event event = incoming.poll();
                    if (event instanceof Disconnect) {
                        cancel();
                    } else if (event instanceof Received) {
                        Received received = (Received) event;
                        dataListener.write(arrayToList(received.getData()));
                    } else if(event instanceof Error) {
                        cancel();
                    }
                }
            }
        }

        try {
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.debug("bluetooth disconnecting");

        dataListener.disconnected();
    }

    public void write(List<Byte> data) {
        try {
            outStream.write(listToArray(data));
        } catch (IOException e) {
            logger.error("bluetooth write error");
            cancel();
        }
    }

    public void cancel() {
        logger.debug("canceling bluetooth connection");

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
            dataListener.connecting("try createInsecureRfcommSocketToServiceRecord");
            BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(sppUuid);
            logger.debug("created insecure socket with spp uuid");
            dataListener.connecting("ok createInsecureRfcommSocketToServiceRecord");
            return socket;
        } catch (IOException e) {
            dataListener.connecting("failed createInsecureRfcommSocketToServiceRecord");
            logger.error("bluetooth createInsecureRfcommSocketToServiceRecord failed");
        }

        try {
            dataListener.connecting("try createRfcommSocketToServiceRecord");
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(sppUuid);
            logger.debug("created secure socket with spp uuid");
            dataListener.connecting("ok createRfcommSocketToServiceRecord");
            return socket;
        } catch (IOException e) {
            dataListener.connecting("failed createRfcommSocketToServiceRecord");
            logger.error("bluetooth createRfcommSocketToServiceRecord failed");
        }

        try {
            dataListener.connecting("try createRfcommSocket");
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
            logger.debug("created rfcomm socket with reflection");
            dataListener.connecting("ok createRfcommSocket");
            return socket;
        } catch (NoSuchMethodException e) {
            dataListener.connecting("failed createRfcommSocket");
            logger.error("bluetooth getMethod(createRfcommSocket) failed with " + e.getMessage());
        } catch (IllegalAccessException e) {
            dataListener.connecting("failed createRfcommSocket");
            logger.error("bluetooth getMethod(invoke) failed with " + e.getMessage());
        } catch (InvocationTargetException e) {
            dataListener.connecting("failed createRfcommSocket");
            logger.error("bluetooth getMethod(invoke) failed with " + e.getMessage());
        }

        dataListener.connecting("no method to get bluetooth socket worked");

        throw new RuntimeException("Could not get bluetooth socket");
    }

    private boolean connect() {
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            socket.connect();
            return true;
        } catch (IOException e) {
            logger.debug("bluetooth unable to connect (" + e.getMessage() + ")");
            // Close the socket
            try {
                socket.close();
            } catch (IOException e2) {
                logger.error("bluetooth unable to close() socket during connection failure");
            }
            return false;
        }
    }

    private byte[] listToArray(List<Byte> data) {
        byte[] message = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            message[i] = data.get(i);
        }
        return message;
    }

    private ArrayList<Byte> arrayToList(byte[] message) {
        ArrayList<Byte> im = new ArrayList<Byte>();
        for (byte symbol : message) {
            im.add(symbol);
        }
        return im;
    }

}
