package com.server.qqserver.service;


import org.apache.commons.lang3.StringUtils;
import qqcommon.constant.MessageType;
import qqcommon.entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: Mask.m
 * @create: 2022/01/31 12:50
 * @description: 该类和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread{

    private Socket socket;
    private String userId; // 连接到服务端的 用户id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        // 可以发送或者接收消息
        while (true){
            try {
                System.out.println("服务端和客户端保持通信，读取数据。。。");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                // 1. 返回在线用户
                if (StringUtils.equals(message.getMsgType(), MessageType.MESSAGE_GET_ONLINE_FRIEND.getCode())){
                    System.out.println(userId + " 获取在线用户列表 ");
                    // 设置消息返回
                    Message returnMsg = new Message();
                    returnMsg.setMsgType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND.getCode());
                    returnMsg.setSender(message.getSender());
                    returnMsg.setContent(getOnlineUser());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(returnMsg);
                }

                // 2. 无异常退出
                if (StringUtils.equals(message.getMsgType(),MessageType.MESSAGE_CLIENT_EXIT.getCode())){

                    System.out.println(message.getSender() + "退出系统");
                    // 1. 移除线程
                    ManageClientsThread.removeClientThread(userId);
                    // 2. 关闭socket连接
                    socket.close();
                    // 3. break掉循环
                    break;
                }

                // 3. 私聊消息
                if (StringUtils.equals(message.getMsgType(),MessageType.MESSAGE_COMM_MES.getCode())){

                    // 获取接收方线程的outputstream
                    ServerConnectClientThread clientThread = ManageClientsThread.getClientThread(message.getReceiver());
                    // 在线消息
                    if (clientThread != null){
                        ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
                    }else {
                        // 此时发送离线消息
                        ManageClientsThread.addOfflineMsg(message.getReceiver(),message);
                        System.out.println( message.getReceiver() + "暂未上线，将会离线发送消息");
                    }

                }

                // 4. 群发消息
                if (StringUtils.equals(message.getMsgType(),MessageType.MESSAGE_FORWARD_ALL.getCode())){

                    // 获取接收方线程的outputstream
                    HashMap<String, ServerConnectClientThread> map = ManageClientsThread.map;
                    Collection<ServerConnectClientThread> values = map.values();
                    // 遍历转发消息
                    values.stream().forEach(serverConnectClientThread -> {
                        try {
                            // 把发送人排除掉 发给其他人
                            if (!StringUtils.equals(serverConnectClientThread.getUserId(),message.getSender())){
                                ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                                oos.writeObject(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                // 5. 发送文件
                if (StringUtils.equals(message.getMsgType(),MessageType.MESSAGE_FILE.getCode())){
                    // 获取接收方线程的outputstream
                    ServerConnectClientThread clientThread = ManageClientsThread.getClientThread(message.getReceiver());

                    // 在线文件
                    if (clientThread != null){
                        ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
                    }else {
                        // 此时发送离线文件
                        ManageClientsThread.addOfflineMsg(message.getReceiver(),message);
                        System.out.println( message.getReceiver() + "暂未上线，将会离线发送文件");
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * 服务端获取在线用户列表
     * @return
     */
    private String getOnlineUser() {
        HashMap<String, ServerConnectClientThread> map = ManageClientsThread.map;
        Set<String> set = map.keySet();
        String collect = set.stream().collect(Collectors.joining("-"));
        return collect;
    }


    public Socket getSocket() {
        return this.socket;
    }

    public String getUserId() {
        return userId;
    }
}
