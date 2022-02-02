package com.server.qqserver.service;

import jdk.nashorn.internal.ir.IfNode;
import org.apache.commons.lang3.StringUtils;
import qqcommon.constant.MessageType;
import qqcommon.entity.Message;
import qqcommon.entity.User;
import sun.applet.resources.MsgAppletViewer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Mask.m
 * @create: 2022/01/31 11:55
 * @description: qq服务端 监听9999端口
 */
public class QQServer {

    private ServerSocket serverSocket;

    //private static HashMap<String,User> validUsers = new HashMap<>();
    private static ConcurrentHashMap<String,User> validUsers = new ConcurrentHashMap<>();

    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("至尊宝",new User("至尊宝","123456"));
        validUsers.put("紫霞仙子",new User("紫霞仙子","123456"));
        validUsers.put("菩提老祖",new User("菩提老祖","123456"));
    }

    public QQServer(){

        try {
            System.out.println("服务端在9999端口监听。。。。。。。。");
            serverSocket = new ServerSocket(9999);

            new Thread(new PushNewsService()).start();
            // 不断去监听
            while (true){
                Socket socket = serverSocket.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) ois.readObject();
                Message message = new Message();

                // 先固定配置账号密码
                if (checkUser(user.getId(),user.getPassword())){
                    System.out.println("用户" + user.getId() + "登录成功");
                    // 登录成功
                    message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCEED.getCode());
                    oos.writeObject(message);

                    // 创建一个线程，与客户端保持通信
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getId());
                    serverConnectClientThread.start();

                    // 把线程放到集合中
                    ManageClientsThread.addClientThread(user.getId(),serverConnectClientThread);

                    // 发送离线消息
                    ConcurrentHashMap<String, ArrayList<Message>> offlineMap = ManageClientsThread.offlineMap;
                    for (Map.Entry<String, ArrayList<Message>> entry : offlineMap.entrySet()) {
                        String key = entry.getKey();
                        ArrayList<Message> value = entry.getValue();
                        if (StringUtils.equals(key,user.getId())){
                            for (Message msg : value) {

                                ServerConnectClientThread thread = ManageClientsThread.getClientThread(key);
                                ObjectOutputStream oos1 = new ObjectOutputStream(thread.getSocket().getOutputStream());
                                oos1.writeObject(msg);

                            }
                            // 所有离线消息发完之后删除掉
                            ManageClientsThread.removeOfflineMsg(key);
                        }

                    }

                }else {
                    System.out.println("用户" + user.getId() + "登录失败");
                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAIL.getCode());
                    oos.writeObject(message);
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private boolean checkUser(String userId,String pwd){
        User user = validUsers.get(userId);
        if (user == null){
            return false;
        }

        if (!StringUtils.equals(user.getPassword(),pwd)){
            return false;
        }
        return true;
    }

}
