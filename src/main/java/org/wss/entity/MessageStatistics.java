package org.wss.entity;

import java.util.*;



  
public class MessageStatistics  implements java.io.Serializable,Comparable {





    private String id;
    /**
     *消息id
     */
    private String messageId;
    /**
     *子消息id
     */
    private String messageSubId;
    /**
     *openId
     */
    private String openId;
    /**
     *浏览时间
     */
    private String createTime;
    /**
     *用户ip
     */
    private String ip;
    /**
     *纬度
     */
    private String longi;
    /**
     *经度
     */
    private String lati;
    /**
     *地理位置
     */
    private String location;

    public MessageStatistics() {
    }

    public MessageStatistics(String id) {
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



    private String _getOpenId(String openId) {
        return this.openId;
    }
    public String getOpenId() {
        return _getOpenId(this.openId);
    }
    public void setOpenId(String openId) {
        this.openId = openId;
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



    private String _getIp(String ip) {
        return this.ip;
    }
    public String getIp() {
        return _getIp(this.ip);
    }
    public void setIp(String ip) {
        this.ip = ip;
    }



    private String _getLongi(String longi) {
        return this.longi;
    }
    public String getLongi() {
        return _getLongi(this.longi);
    }
    public void setLongi(String longi) {
        this.longi = longi;
    }



    private String _getLati(String lati) {
        return this.lati;
    }
    public String getLati() {
        return _getLati(this.lati);
    }
    public void setLati(String lati) {
        this.lati = lati;
    }



    private String _getLocation(String location) {
        return this.location;
    }
    public String getLocation() {
        return _getLocation(this.location);
    }
    public void setLocation(String location) {
        this.location = location;
    }



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        MessageStatistics other =(MessageStatistics) obj;
        return this.getId().compareTo(other.getId());
    }
}