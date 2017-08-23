package org.wss.entity;

import java.util.*;



  
public class MessageDiscuss  implements java.io.Serializable,Comparable {


    private String id;
    /**
     *openId
     */
    private String openId;
    /**
     *消息id
     */
    private String messageId;
    /**
     *子消息id
     */
    private String messageSubId;
    /**
     *评论时间
     */
    private String createTime;
    /**
     *评论内容
     */
    private String content;

    public MessageDiscuss() {
    }

    public MessageDiscuss(String id) {
        this.id = id;
    }





    private String _getId(String id) {
        return this.id;
    }
    public String getId() {
        return _getId(this.id);
    }
    public void setId(String id) {
        this.id = id;
    }



    private String _getOpenId(String openId) {
        return this.openId;
    }
    public String getOpenId() {
        return _getOpenId(this.openId);
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }



    private String _getMessageId(String messageId) {
        return this.messageId;
    }
    public String getMessageId() {
        return _getMessageId(this.messageId);
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }



    private String _getMessageSubId(String messageSubId) {
        return this.messageSubId;
    }
    public String getMessageSubId() {
        return _getMessageSubId(this.messageSubId);
    }
    public void setMessageSubId(String messageSubId) {
        this.messageSubId = messageSubId;
    }



    private String _getCreateTime(String createTime) {
        return this.createTime;
    }
    public String getCreateTime() {
        return _getCreateTime(this.createTime);
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



    private String _getContent(String content) {
        return this.content;
    }
    public String getContent() {
        return _getContent(this.content);
    }
    public void setContent(String content) {
        this.content = content;
    }



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        MessageDiscuss other =(MessageDiscuss) obj;
        return this.getId().compareTo(other.getId());
    }
}