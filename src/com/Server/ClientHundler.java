package com.Server;

import com.Server.packet.OPacket;


import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHundler extends Thread {
    private final Socket client;
    private String nickname = "No client";

    public ClientHundler(Socket client){
        this.client = client;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void run() {
        while(true){
            if (!readData()){
                sleep();
            }

        }
    }

    private static void sleep(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private boolean readData(){
        try{
            DataInputStream dis = new DataInputStream(client.getInputStream());
            if (dis.available() <= 0){
                 return false;
            }
            short id = dis.readShort();
            OPacket packet = PacketManager.getPacket(id);
            packet.setSocket(client);
            packet.read(dis);
            packet.handle();

        }catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public void invalidate() {
        ServerLoader.invalidate(client);
    }
}
