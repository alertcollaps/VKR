package com.Client.Client.packet;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.KeyPair;

public class PacketKeys extends OPacket {
    private String key;

    PacketKeys(){

    }
    public PacketKeys(String key){
        this.key = key;
    }


    @Override
    public short getId() {
        return 3;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(key);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        key = dis.readUTF();
    }

    @Override
    public void handle() {
        System.out.println(key);

    }
}
