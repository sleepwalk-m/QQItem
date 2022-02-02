package com.server.qqserver.service;

import org.apache.commons.lang3.StringUtils;
import qqcommon.constant.MessageType;
import qqcommon.entity.Message;
import qqcommon.util.ScannerUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author: Mask.m
 * @create: 2022/02/02 13:23
 * @description: 服务端推送新闻
 */
public class PushNewsService implements Runnable {


    @Override
    public void run() {
        while (true) {
            System.out.println("请输入推送的内容（输入exit停止推送）：");
            String key = ScannerUtils.readString(100);

            if (StringUtils.equals(key, "exit")) {
                break;
            }

            Message message = new Message();
            message.setSender("服务器");
            message.setContent(key);
            message.setMsgType(MessageType.MESSAGE_FORWARD_ALL.getCode());
            message.setSendTime(LocalDateTime.now().toString());

            HashMap<String, ServerConnectClientThread> map = ManageClientsThread.map;
            Collection<ServerConnectClientThread> values = map.values();


            // 遍历转发消息
            values.stream().forEach(serverConnectClientThread -> {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }
}
