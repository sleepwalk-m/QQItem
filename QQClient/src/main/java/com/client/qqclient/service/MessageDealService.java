package com.client.qqclient.service;

import qqcommon.constant.MessageType;
import qqcommon.entity.Message;
import qqcommon.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * @author: Mask.m
 * @create: 2022/02/01 16:58
 * @description: 处理私聊消息的类
 */
public class MessageDealService {


    /**
     * 给单用户发消息
     *
     * @param sender
     * @param receiver
     * @param content
     */
    public void sendMessageToOne(String sender, String receiver, String content) {
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_COMM_MES.getCode());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSendTime(LocalDateTime.now().toString());

        // 获取output
        ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(sender);
        Socket socket = clientConnectServerThread.getSocket();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 群发消息
     *
     * @param sender
     * @param content
     */
    public void sendMessageToMany(String sender, String content) {
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_FORWARD_ALL.getCode());
        message.setSender(sender);
        message.setContent(content);
        message.setSendTime(LocalDateTime.now().toString());

        // 获取output
        ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(sender);
        Socket socket = clientConnectServerThread.getSocket();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送文件
     *
     * @param sender
     * @param receiver
     * @param src      发送路径
     * @param dest     接收路径
     */
    public void sendFileToOne(String sender, String receiver, String src, String dest) {
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_FILE.getCode());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSrc(src);
        message.setDest(dest);
        message.setSendTime(LocalDateTime.now().toString());
        FileInputStream fis = null;
        try {
            // 开始读取文件
            fis = new FileInputStream(src);
            byte[] fileBytes = new byte[(int) new File(src).length()];
            fis.read(fileBytes);

            message.setFileBytes(fileBytes);
            message.setFileLen(fileBytes.length);
            // 获取output
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(sender);
            Socket socket = clientConnectServerThread.getSocket();

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(sender + "给" + receiver +"发送了文件，目录在：" + dest);
    }
}
