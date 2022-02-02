package com.client.qqclient.view;

import com.client.qqclient.service.MessageDealService;
import com.client.qqclient.service.UserClientService;
import com.client.qqclient.utils.ScannerUtils;

/**
 * @author: Mask.m
 * @create: 2022/01/30 18:37
 * @description: 界面相关
 */
public class QQView {

    private static boolean loop = true; // 控制循环
    private static String key = "";// 接收用户的键盘输入
    private static UserClientService userClientService = new UserClientService();// 登录注册校验
    private static MessageDealService messageDealService = new MessageDealService();// 登录注册校验

    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu(){

        while (loop){
            System.out.println("================欢迎进入网络通信系统================");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入您的选择：");
            key = ScannerUtils.readString(1);

            switch (key){
                case "1":
                    secondMenu();
                    break;
                case "9":
                    loop = false;
                    System.out.println("退出系统");
                    break;
            }

        }
    }

    private static void secondMenu() {
        System.out.println("登录系统");
        System.out.print("请输入用户名：");
        String userId = ScannerUtils.readString(50);
        System.out.print("请输入密\t码：");
        String pwd = ScannerUtils.readString(50);

        if (userClientService.checkUser(userId,pwd)){
            System.out.println("===========（用户"+ userId +"）=============");
            // 进入二级菜单
            while (loop){
                System.out.println("\n==========网络通信系统二级菜单（用户"+ userId +"）====================");
                System.out.println("\t\t 1 显示在线用户列表");
                System.out.println("\t\t 2 群发消息");
                System.out.println("\t\t 3 私聊消息");
                System.out.println("\t\t 4 发送文件");
                System.out.println("\t\t 9 退出系统");
                System.out.println("请输入你的选择：");

                key = ScannerUtils.readString(1);
                switch (key){
                    case "1":
                        System.out.println("显示在线用户列表");
                        userClientService.getOnlineFriends();
                        break;
                    case "2":
                        System.out.println("群发消息");
                        System.out.println("请输入要发送的内容：");
                        String AllContent = ScannerUtils.readString(100);
                        messageDealService.sendMessageToMany(userId,AllContent);
                        break;
                    case "3":
                        System.out.println("私聊消息");
                        System.out.println("请输入接收消息的朋友（须在线）：");
                        String receiver = ScannerUtils.readString(50);
                        System.out.println("请输入要发送的内容：");
                        String OneContent = ScannerUtils.readString(100);
                        messageDealService.sendMessageToOne(userId,receiver,OneContent);
                        break;
                    case "4":
                        System.out.println("发送文件");
                        System.out.println("请输入接收文件的朋友（须在线）：");
                        String fileReceiver = ScannerUtils.readString(50);
                        System.out.println("请输入发送的文件路径：");
                        String FileSrc = ScannerUtils.readString(100);
                        System.out.println("请输入接收的文件路径：");
                        String FileDest = ScannerUtils.readString(100);
                        messageDealService.sendFileToOne(userId,fileReceiver,FileSrc,FileDest);
                        break;
                    case "9":
                        System.out.println("退出系统");
                        userClientService.logout();
                        loop = false;
                        break;
                }
            }
        }else {
            System.out.println("========登录失败========");
        }
    }
}
