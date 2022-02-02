package qqcommon.constant;

/**
 * @author: Mask.m
 * @create: 2022/01/30 18:11
 * @description: 消息类型枚举
 */
public enum MessageType {

    MESSAGE_LOGIN_SUCCEED("1","登录成功"),
    MESSAGE_LOGIN_FAIL("2","登录成功"),
    MESSAGE_COMM_MES("3","普通信息"),
    MESSAGE_GET_ONLINE_FRIEND("4","获取在线用户列表"),
    MESSAGE_RETURN_ONLINE_FRIEND("5","返回在线用户列表"),
    MESSAGE_CLIENT_EXIT("6","客户端请求退出"),
    MESSAGE_FORWARD_ALL("7","群发消息"),
    MESSAGE_FILE("8","文件消息");


    private String code;
    private String msg;

    MessageType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
