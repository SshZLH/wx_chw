package org.wss.entity;

import java.util.*;



  
public class WeixinGroup  implements java.io.Serializable,Comparable {




    private String id;
    /**
     *对应微信端的分组id
     */
    private Integer code;
    /**
     *分组名称
     */
    private String name;
    /**
     *二维码参数
     */
    private String sceneStr;
    /**
     *二维码ticket
     */
    private String ticket;
    /**
     *二维码url
     */
    private String url;

    public WeixinGroup() {
    }

    public WeixinGroup(String id) {
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



    private Integer _getCode(Integer code) {
        return this.code;
    }
    public Integer getCode() {
        return _getCode(this.code);
    }
    public void setCode(Integer code) {
        this.code = code;
    }



    private String _getName(String name) {
        return this.name;
    }
    public String getName() {
        return _getName(this.name);
    }
    public void setName(String name) {
        this.name = name;
    }



    private String _getSceneStr(String sceneStr) {
        return this.sceneStr;
    }
    public String getSceneStr() {
        return _getSceneStr(this.sceneStr);
    }
    public void setSceneStr(String sceneStr) {
        this.sceneStr = sceneStr;
    }



    private String _getTicket(String ticket) {
        return this.ticket;
    }
    public String getTicket() {
        return _getTicket(this.ticket);
    }
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }



    private String _getUrl(String url) {
        return this.url;
    }
    public String getUrl() {
        return _getUrl(this.url);
    }
    public void setUrl(String url) {
        this.url = url;
    }



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        WeixinGroup other =(WeixinGroup) obj;
        return this.getId().compareTo(other.getId());
    }
}