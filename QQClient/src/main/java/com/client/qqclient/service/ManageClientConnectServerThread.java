package com.client.qqclient.service;

import java.util.HashMap;

/**
 * @author: Mask.m
 * @create: 2022/01/31 11:32
 * @description: 管理线程 客户端连接服务端线程
 */
public class ManageClientConnectServerThread {
    // 多个线程放到map中 key -》 用户id
    private static HashMap<String,ClientConnectServerThread> map = new HashMap<>();

    public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        map.put(userId,clientConnectServerThread);
    }

    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return map.get(userId);
    }


}
