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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import world.bilo.stack.Logger;


public class Worker extends Thread {
    private final Logger logger;
    private final WorkHandler dataListener;
    private final BluetoothDevice device;
    private final ConcurrentLinkedQueue<Event> incoming;
    private BluetoothSocket socket;
    private Receiver receiver;
    private Sender sender;
    private ConcurrentLinkedQueue<byte[]> sendQueue;

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

        logger.debug("bluetooth connection started");
        setName("ConnectThread");

        if (!connect()) {
            dataListener.connecting("connection failed");
            logger.debug("bluetooth connection failed");
            dataListener.disconnected();
            return;
        }

        RecvHdl handler = new RecvHdl(incoming, this);
        receiver = new Receiver(inStream, handler);

        sendQueue = new ConcurrentLinkedQueue<>();
        sender = new Sender(sendQueue, getOutputStream(socket), handler);

        receiver.start();
        sender.start();

        ///////////////////////////////////////

        dataListener.connecting("connected");
        dataListener.connected();

        logger.debug("bluetooth connected");


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
                    } else if (event instanceof Error) {
                        cancel();
                    }
                }
            }
        }

        ///////////////////////////////////////

        try {
            sender.join();
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.debug("bluetooth disconnecting");

        dataListener.disconnected();
    }

    public void write(List<Byte> data) {
        sendQueue.offer(listToArray(data));
        sender.interrupt();
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
