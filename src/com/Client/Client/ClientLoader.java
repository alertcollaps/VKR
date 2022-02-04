package com.Client.Client;

import com.Client.Client.packet.PacketAuthorize;
import com.Client.Client.packet.PacketManager;
import com.Client.Client.packet.OPacket;
import com.Client.Client.packet.PacketMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientLoader {
    private static Socket socket;
    private static String host = "localhost";
    private static int port = 8888;
    private static String nickname = "default";
    private static boolean sentNickname = false;

    public static void main(String[] args) {
        connect();
        handle();
        end();
    }

    private static void connect(){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handle(){
        Thread handler = new Thread(){
            @Override
            public void run(){
                while(true){
                    try{
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        if (dis.available() <= 0){
                            ClientLoader.sleep();
                            continue;
                        }
                        short id = dis.readShort();

                        OPacket packet = PacketManager.getPacket(id);
                        packet.read(dis);
                        packet.handle();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }

                }
            }
        };
        handler.start();
        readChat();

    }


    private static void readChat() {
        Scanner scan = new Scanner(System.in);
        while (true){
            if (scan.hasNextLine()){
                String line = scan.nextLine();
                if (line == "/end"){
                    end();
                    return;
                }
                if (!sentNickname){
                    sendPacket(new PacketAuthorize(line));
                    sentNickname = true;
                    continue;
                }
                sendPacket(new PacketMessage(null, line));
            }
        }
    }

    private static void end(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void sendPacket(OPacket packet){
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeShort(packet.getId());
            packet.write(dos);
            dos.flush();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void sleep(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
