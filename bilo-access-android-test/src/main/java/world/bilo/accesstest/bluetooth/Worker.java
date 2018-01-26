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

import world.bilo.accesstest.queue.MessageSender;
import world.bilo.accesstest.queue.thread.ThreadQueue;
import world.bilo.accesstest.queue.MessageHandler;

class Worker extends Thread implements MessageHandler<Event> {
    private final Output dataListener;
    private final BluetoothDevice device;
    private final ThreadQueue<Event> queue = new ThreadQueue<>(this, this);
    private BluetoothSocket socket;
    private Receiver receiver;
    private Sender sender;

    public Worker(Output dataListener, BluetoothDevice device) {
        this.dataListener = dataListener;
        this.device = device;
    }

    public void run() {
        dataListener.connecting("connection started");

        socket = getBluetoothSocket(device);
        InputStream inStream = getInputStream(socket);

        setName("ConnectThread");

        if (!connect()) {
            dataListener.connecting("connection failed");
            dataListener.disconnected();
            return;
        }

        RecvHdl handler = new RecvHdl(getQueue());
        receiver = new Receiver(inStream, handler);

        sender = new Sender(getOutputStream(socket), handler);

        receiver.start();
        sender.start();

        ///////////////////////////////////////

        dataListener.connecting("connected");
        dataListener.connected();

        while (socket.isConnected()) {
            queue.getReceiver().handle();
        }

        ///////////////////////////////////////

        try {
            sender.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dataListener.disconnected();
    }

    @Override
    public void handle(Event event) {
        if (event instanceof Disconnect) {
            cancel();
        } else if (event instanceof Received) {
            Received received = (Received) event;
            dataListener.received(arrayToList(received.getData()));
        } else if (event instanceof Error) {
            cancel();
        }
    }

    public void write(List<Byte> data) {
        sender.getQueue().send(listToArray(data));
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    private InputStream getInputStream(BluetoothSocket socket) {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
        }

        throw new RuntimeException("could not get input stream");
    }

    private OutputStream getOutputStream(BluetoothSocket socket) {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
        }

        throw new RuntimeException("could not get output stream");
    }

    private BluetoothSocket getBluetoothSocket(BluetoothDevice device) {
        UUID sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            dataListener.connecting("try createInsecureRfcommSocketToServiceRecord");
            BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(sppUuid);
            dataListener.connecting("ok createInsecureRfcommSocketToServiceRecord");
            return socket;
        } catch (IOException e) {
            dataListener.connecting("failed createInsecureRfcommSocketToServiceRecord");
        }

        try {
            dataListener.connecting("try createRfcommSocketToServiceRecord");
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(sppUuid);
            dataListener.connecting("ok createRfcommSocketToServiceRecord");
            return socket;
        } catch (IOException e) {
            dataListener.connecting("failed createRfcommSocketToServiceRecord");
        }

        try {
            dataListener.connecting("try createRfcommSocket");
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
            dataListener.connecting("ok createRfcommSocket");
            return socket;
        } catch (NoSuchMethodException e) {
            dataListener.connecting("failed createRfcommSocket");
        } catch (IllegalAccessException e) {
            dataListener.connecting("failed createRfcommSocket");
        } catch (InvocationTargetException e) {
            dataListener.connecting("failed createRfcommSocket");
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
            // Close the socket
            try {
                socket.close();
            } catch (IOException e2) {
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

    public MessageSender<Event> getQueue() {
        return queue.getSender();
    }

}
