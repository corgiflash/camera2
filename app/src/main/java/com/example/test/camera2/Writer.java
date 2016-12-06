package com.example.test.camera2;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */

public class Writer extends AsyncTask<Void, Void, Void> {
    private final int PORT;
    private final DatagramSocket socket;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ArrayList<InetAddress> addresses = new ArrayList<>();
    private final LinkedBlockingQueue<Object> requests = new LinkedBlockingQueue<>();
    private final String THREAD_NAME = "Writer";

    /**
     * @param port The port that the packets will be sent to.
     * @throws IOException
     */
    public Writer(int port) throws SocketException {
        super();
        this.PORT = port;
        this.socket = new DatagramSocket();
        AsyncTaskCompat.executeParallel(this);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Thread.currentThread().setName(THREAD_NAME);
        try {
            while (!isCancelled()) {
                //Convert object to JSON then byte array to send
                byte[] bytes;
                Object obj = requests.take();
                if (obj instanceof JSONObject)
                    bytes = obj.toString().getBytes();
                else
                    bytes = gson.toJson(obj).getBytes();
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                packet.setPort(PORT);

                for (InetAddress address : this.addresses) {
                    packet.setAddress(address);
                    socket.send(packet);
                }
            }
        } catch (IOException | InterruptedException ignored) {
        }
        return null;
    }

    public void stop() {
        // Interrupt udp socket
        socket.close();

        // wake up blocking queue
        if (requests.isEmpty()) requests.add(new DatagramPacket(new byte[1], 1));
        else requests.clear();
    }

    public void disconnect(Object item) {
        try {
            this.write(item); //write packet with disconnect message
            while(!requests.isEmpty()) Thread.sleep(299);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds item to the blocking queue.
     *
     * @param item Object will be added to blocking queue then converted
     *             to JSON before being sent as a byte array to all addresses.
     * @throws IOException Writer is cancelled, not running, or has no addresses to write to.
     */
    public void write(Object item) throws IOException {
        if (isCancelled())
            throw new IOException("Writer is cancelled or not running");
        else if (addresses.isEmpty())
            throw new IOException("Writer has no addresses to write to, dam harout");
        requests.add(item);
    }

    public boolean hasAddresses() {
        return !addresses.isEmpty();
    }

    /**
     * @param address Address that packets will be sent to.
     */
    public void addAddress(InetAddress address) {
        addresses.add(address);
    }

    /**
     * @param address Address that's been added to Writer.
     */
    public void removeAddress(InetAddress address) {
        addresses.remove(address);
    }
}
