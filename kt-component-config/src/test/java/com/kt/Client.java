package com.kt;

import jdk.jfr.events.SocketReadEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8686);
            System.out.println(socket.isConnected());
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("hello".getBytes(StandardCharsets.UTF_8));
            Thread.sleep(8000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
