package com.cucc.vertx.demo.envetbusoracle;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.cucc.vertx.demo.object.Userinfo;


public class MyCodec2 implements MessageCodec<Userinfo, Userinfo> {

    @Override
    public Userinfo decodeFromWire(int pos, Buffer buffer) {
        // TODO Auto-generated method stub
        int length = buffer.getInt(pos);
        pos += 4;
        byte[] encoded = buffer.getBytes(pos, pos + length);
        Userinfo user = null;      
        try {        
            ByteArrayInputStream bis = new ByteArrayInputStream (encoded);        
            ObjectInputStream ois = new ObjectInputStream (bis);        
            user = (Userinfo) ois.readObject();      
            ois.close();   
            bis.close();   
        } catch (IOException ex) {        
            ex.printStackTrace();   
        } catch (ClassNotFoundException ex) {        
            ex.printStackTrace();   
        }      
        return user; 
       
    }

    @Override
    public void encodeToWire(Buffer buffer, Userinfo user) {
        // TODO Auto-generated method stub
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(user);        
            oos.flush();         
            bytes = bos.toByteArray ();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            ex.printStackTrace();   
        }      
        buffer.appendInt(bytes.length);
        Buffer buff = Buffer.buffer(bytes);
        buffer.appendBuffer(buff);    
        
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return "UserInfo";
    }

    @Override
    public byte systemCodecID() {
        // TODO Auto-generated method stub
        return -1;
    }

    @Override
    public Userinfo transform(Userinfo user) {
        // TODO Auto-generated method stub
        return user;
    }



}