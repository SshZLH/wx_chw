package org.wss.entity;

import java.util.*;



  
public class Menu4Wss  implements java.io.Serializable,Comparable {






    private String id;
    /**
     *parentId
     */
    private String parentId;
    private String parentName;
    /**
     *显示顺序
     */
    private Integer order;
    /**
     *层级
     */
    private Integer level;
    /**
     *菜单名称
     */
    private String name;
    /**
     *回复消息Id
     */
    private String respId;
    /**
     *跳转url
     */
    private String url;
    /**
     *菜单事件类型：click 消息响应, view 页面跳转
     */
    private String type;
    /**
     *链接类型：0 静态链接 1 动态链接
     */
    private String urlType;

    public Menu4Wss() {
    }

    public Menu4Wss(String id) {
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



    private String _getParentId(String parentId) {
        return this.parentId;
    }
    public String getParentId() {
        return _getParentId(this.parentId);
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }



    private String _getParentName(String parentName) {
        return this.parentName;
    }
    public String getParentName() {
        return _getParentName(this.parentName);
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }



    private Integer _getOrder(Integer order) {
        return this.order;
    }
    public Integer getOrder() {
        return _getOrder(this.order);
    }
    public void setOrder(Integer order) {
        this.order = order;
    }



    private Integer _getLevel(Integer level) {
        return this.level;
    }
    public Integer getLevel() {
        return _getLevel(this.level);
    }
    public void setLevel(Integer level) {
        this.level = level;
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



    private String _getRespId(String respId) {
        return this.respId;
    }
    public String getRespId() {
        return _getRespId(this.respId);
    }
    public void setRespId(String respId) {
        this.respId = respId;
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



    private String _getType(String type) {
        return this.type;
    }
    public String getType() {
        return _getType(this.type);
    }
    public void setType(String type) {
        this.type = type;
    }



    private String _getUrlType(String urlType) {
        return this.urlType;
    }
    public String getUrlType() {
        return _getUrlType(this.urlType);
    }
    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        Menu4Wss other =(Menu4Wss) obj;
        return this.getId().compareTo(other.getId());
    }
}