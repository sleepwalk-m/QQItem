package com.server.qqserver.service;

import qqcommon.entity.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Mask.m
 * @create: 2022/01/31 13:00
 * @description: 管理客户端线程
 */
public class ManageClientsThread {

    public static HashMap<String,ServerConnectClientThread> map = new HashMap<>();
    public static ConcurrentHashMap<String, ArrayList<Message>> offlineMap = new ConcurrentHashMap<>();


    public static void addClientThread(String userId,ServerConnectClientThread clientConnectServerThread){
        map.put(userId,clientConnectServerThread);
    }

    public static ServerConnectClientThread getClientThread(String userId){
        return map.get(userId);
    }


    public static ServerConnectClientThread removeClientThread(String userId){
        return map.remove(userId);
    }


    public static ArrayList<Message> getOfflineMsg(String userId){
        return offlineMap.get(userId);
    }


    public static void removeOfflineMsg(String userId){
        offlineMap.remove(userId);
    }

    public static void addOfflineMsg(String userId,Message message){

        ArrayList<Message> msg = getOfflineMsg(userId);
        if ( msg != null &&!msg.isEmpty() ){
            msg.add(message);
        }else {
            msg = new ArrayList<>();
            msg.add(message);
        }

        offlineMap.put(userId,msg);
    }
}
