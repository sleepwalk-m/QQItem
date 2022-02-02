package com.client.qqclient.service;

import org.apache.commons.lang3.StringUtils;
import qqcommon.constant.MessageType;
import qqcommon.entity.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author: Mask.m
 * @create: 2022/01/31 11:24
 * @description: 与服务器保持连接
 */
public class ClientConnectServerThread extends Thread {

    // 该线程需要持有socket
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 线程需要后台 持续和服务器 通信
        while (true) {
            try {
                System.out.println("客户端线程等待读取从服务器发送的消息");
                ObjectInputStream oos = new ObjectInputStream(socket.getInputStream());
                Message msg = (Message) oos.readObject();

                // 返回在线用户
                if (StringUtils.equals(msg.getMsgType(), MessageType.MESSAGE_RETURN_ONLINE_FRIEND.getCode())) {
                    String[] split = msg.getContent().split("-");
                    System.out.println("\t =============在线用户列表===============");
                    Arrays.stream(split).forEach(s -> System.out.println("用户：" + s));
                }
                // 接收私聊消息
                if (StringUtils.equals(msg.getMsgType(), MessageType.MESSAGE_COMM_MES.getCode())) {
                    System.out.println(msg.getSender() + " 对" + msg.getReceiver() + " 说：" + msg.getContent());
                    System.out.println("\n" + msg.getSendTime());
                }

                // 接收群发消息
                if(StringUtils.equals(msg.getMsgType(),MessageType.MESSAGE_FORWARD_ALL.getCode())){
                    System.out.println(msg.getSender() + " 对所有在线用户说：" + msg.getContent());
                    System.out.println("\n" + msg.getSendTime());
                }

                // 接收文件
                if (StringUtils.equals(msg.getMsgType(),MessageType.MESSAGE_FILE.getCode())){
                    String dest = msg.getDest();

                    FileOutputStream fos = new FileOutputStream(new File(dest));
                    fos.write(msg.getFileBytes());

                    fos.close();

                    System.out.println(msg.getReceiver() + "收到了文件，路径：" + msg.getDest());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return this.socket;
    }
}
