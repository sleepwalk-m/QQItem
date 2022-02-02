package com.client.qqclient.service;

import org.apache.commons.lang3.StringUtils;
import qqcommon.constant.MessageType;
import qqcommon.entity.Message;
import qqcommon.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author: Mask.m
 * @create: 2022/01/30 22:19
 * @description: 客户端 验证登录
 */
public class UserClientService {

    private User user = new User();
    private Socket socket;

    /**
     * 到服务器验证该用户是否合法
     *
     * @param userId
     * @param pwd
     * @return
     */
    public boolean checkUser(String userId,String pwd){
        boolean flag = false;
        // 设置对象
        user.setId(userId);
        user.setPassword(pwd);

        // 连接服务器 发送user对象
        try {
            socket = new Socket(InetAddress.getLocalHost(),9999);
            // 传递user对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            // 从服务器获取返回的msg对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            // 登录成功
            if (StringUtils.equals(message.getMsgType(), MessageType.MESSAGE_LOGIN_SUCCEED.getCode())){

                // 创建一个线程和服务器保持通信
                flag = true;
                ClientConnectServerThread thread = new ClientConnectServerThread(socket);
                thread.start();// 启动客户端线程 无限获取服务器回复消息

                // 但是client如果有多个 最好是用一个集合来管理多个线程
                ManageClientConnectServerThread.addClientConnectServerThread(userId,thread);

                //

            }else {
                // 登录失败 不能启动和服务端通信的线程，但是socket已经创建，需要关闭
                socket.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 拉取在线用户
     */
    public void getOnlineFriends(){

        // 构建消息对象
        Message message = new Message();
        message.setSender(user.getId());
        message.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND.getCode());// 设置类型为获取在线用户

        try {
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(user.getId());

            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 无异常退出
     *  1. 退出客户端线程
     *  2. 通知服务端该客户端退出
     */
    public void logout(){
        Message message = new Message();
        message.setSender(user.getId());
        message.setMsgType(MessageType.MESSAGE_CLIENT_EXIT.getCode());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getId() + " 退出系统。。");
            System.exit(0); // 退出主线程
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
