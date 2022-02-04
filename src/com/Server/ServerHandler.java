package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerHandler extends Thread{

    private final ServerSocket server;

    ServerHandler(ServerSocket server){
        this.server = server;
    }

    @Override
    public void run() {
        while(true){
            Socket client;
            try {
                client = server.accept();
                ClientHundler handler = new ClientHundler(client);
                handler.start();
                ServerLoader.handlers.put(client, handler);
            } catch (SocketException ex){
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            ServerLoader.sleep();
        }
    }
}
