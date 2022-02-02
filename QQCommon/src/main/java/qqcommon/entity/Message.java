package qqcommon.entity;

import java.io.Serializable;

/**
 * @author: Mask.m
 * @create: 2022/01/30 18:03
 * @description: 消息对象
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sender;
    private String receiver;

    private String content;
    private String sendTime;
    private String msgType;// 消息类型 图片 文字 语音等


    private byte[] fileBytes;
    private int fileLen;
    private String dest;// 接收路径
    private String src;//发送路径



    public Message(String sender, String receiver, String content, String sendTime, String msgType, byte[] fileBytes, int fileLen, String dest, String src) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sendTime = sendTime;
        this.msgType = msgType;
        this.fileBytes = fileBytes;
        this.fileLen = fileLen;
        this.dest = dest;
        this.src = src;
    }

    public Message(String sender, String receiver, String content, String sendTime, String msgType) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sendTime = sendTime;
        this.msgType = msgType;
    }

    public Message() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
