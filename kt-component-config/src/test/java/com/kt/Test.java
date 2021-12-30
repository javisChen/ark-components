package com.kt;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket;
        try {
            serverSocket = new ServerSocket(8686);
            while (true) {
                socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                int available = inputStream.available();
                byte[] bytes = new byte[1024];
                int read = 0;
                while ((read = inputStream.read(bytes)) != -1) {
                    System.out.println(new String(bytes, 0 , read));
                }
                Thread.sleep(10000);
                socket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null)
                try {
                    serverSocket.close();
                } catch (Exception e) {

                }

        }
    }
}
