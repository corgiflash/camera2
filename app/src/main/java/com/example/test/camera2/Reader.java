package com.example.test.camera2;

/**
 * Created by xuejing on 2016/11/17.
 */

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Reads in JSON from a Multi cast Socket, on a separate thread.
 * All JSON received is stored in the internal buffer which can be polled
 * using {@link #read()} by differed threads.
 */

public class Reader extends AsyncTask<Void, Void, Void> {
    /**
     * Default packet size, needs to be large enough to accept most JSON strings.
     */
    private static final int PACKET_SIZE = 5 * 1024; // 5k

    /**
     * Buffer to store received JSON strings
     */
    private final LinkedBlockingQueue<JsonObject> buffer = new LinkedBlockingQueue<>();
    private final DatagramSocket socket;
    private final Gson gson = new Gson();

    /**
     * @param port Port that packets are sent to.
     * @throws IOException
     */
    public Reader(int port) throws IOException {
        super();
        socket = new DatagramSocket(port);
        socket.setSoTimeout(0);
        socket.setReuseAddress(true);
        AsyncTaskCompat.executeParallel(this);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            while (!isCancelled()) {
                DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                socket.receive(packet);

                //Convert received packet to String after trimming the trailing space.
                String jsonString = new String(packet.getData()).trim();
                buffer.put(gson.fromJson(jsonString, JsonObject.class));
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
}