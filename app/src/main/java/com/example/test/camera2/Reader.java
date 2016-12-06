package com.example.test.camera2;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Reads in JSON from a Multi cast Socket, on a separate thread.
 * All JSON received is stored in the internal buffer which can be polled
 * using {@link #read()} by differed threads.
 */

public class Reader extends AsyncTask<Void, Void, Void> {
    private final String THREAD_NAME = "Reader";


    /**
     * Default packet size, needs to be large enough to accept most JSON strings.
     */
    private static final int MAX_PACKET_SIZE = 64 * 1024; // 5k
    /**
     * Buffer to store received JSON strings
     */
    private final LinkedBlockingQueue<JsonObject> buffer = new LinkedBlockingQueue<>();
    private final DatagramSocket socket;
    private final JsonParser parser = new JsonParser();

    /**
     * @param port Port that packets are sent to.
     * @throws IOException
     */
    public Reader(int port) throws SocketException {
        super();
        socket = new DatagramSocket(port);
        socket.setSoTimeout(0);
        socket.setReuseAddress(true);
        AsyncTaskCompat.executeParallel(this);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Thread.currentThread().setName(THREAD_NAME);
        DatagramPacket packet = new DatagramPacket(new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
        try {
            while (!isCancelled()) {
                socket.receive(packet);

                //Convert received packet to String after trimming the trailing space.
                String jsonString = new String(packet.getData(), packet.getOffset(), packet.getLength());
                buffer.put(parser.parse(jsonString).getAsJsonObject());
            }
        } catch (IOException | InterruptedException e) {
            if (!isCancelled())
                throw new RuntimeException("Reader IO Exception or Interrupted");
        }
        return null;
    }

    /**
     * @return Takes a packet off of the blocking queue.
     * @throws IOException Writer is cancelled or not running.
     */
    public JsonObject read() throws IOException {
        if (isCancelled())
            throw new IOException("Writer is cancelled or not running");
        return buffer.poll();
    }

    public void stop() {
        this.cancel(true);
        socket.close();
    }

    /**
     * @return True if no received packets are in in the queue to be read.
     */

    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    /**
     * Removes all packets from the blocking queue buffer.
     */
    public void clear() {
        buffer.clear();
    }

    public JsonObject[] readAll() {
        JsonObject[] inputs = buffer.toArray(new JsonObject[0]);
        buffer.clear();
        return inputs;
    }
}