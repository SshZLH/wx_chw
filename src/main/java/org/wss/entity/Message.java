package org.wss.entity;

import java.util.*;



  
public class Message  implements java.io.Serializable,Comparable {




    private String id;
    /**
     *标题
     */
    private String title;
    /**
     *摘要
     */
    private String memo;
    /**
     *封面图
     */
    private String image;
    /**
     *文本消息内容
     */
    private String text;
    /**
     *图文模板id
     */
    private String wcmsId;
    /**
     * 消息模式：1 模板消息 2 群发消息
     */
    private String mode;
    /**
     * 消息类型：1 单图文 2 多图文
     */
    private String type;
    /**
     *创建的时间
     */
    private String createTime;
    /**
     *编辑的时间
     */
    private String updateTime;
    /**
     *发布的时间
     */
    private String publishTime;
    /**
     *发布状态：0：未发布 1：已发布
     */
    private String status;
    /**
     *发送人id
     */
    private String fromUserId;
    /**
     *是否记录读者的地理位置
     */
    private String hasLocation;
    /**
     *是否加载该公众号已读用户列表
     */
    private String hasUserlist;
    /**
     *是否加载回复
     */
    private String hasDiscuss;
    /**
     *是否发送给所有人
     */
    private String isToAll;
    /**
     *按分组选择的分组的id,以逗号隔开
     */
    private String messGroupIds;
    /**
     *按分组选择的分组的名称,以逗号隔开
     */
    private String messGroupNames;

    public Message() {
    }

    public Message(String id) {
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



    private String _getTitle(String title) {
        return this.title;
    }
    public String getTitle() {
        return _getTitle(this.title);
    }
    public void setTitle(String title) {
        this.title = title;
    }



    private String _getMemo(String memo) {
        return this.memo;
    }
    public String getMemo() {
        return _getMemo(this.memo);
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }



    private String _getImage(String image) {
        return this.image;
    }
    public String getImage() {
        return _getImage(this.image);
    }
    public void setImage(String image) {
        this.image = image;
    }



    private String _getText(String text) {
        return this.text;
    }
    public String getText() {
        return _getText(this.text);
    }
    public void setText(String text) {
        this.text = text;
    }



    private String _getWcmsId(String wcmsId) {
        return this.wcmsId;
    }
    public String getWcmsId() {
        return _getWcmsId(this.wcmsId);
    }
    public void setWcmsId(String wcmsId) {
        this.wcmsId = wcmsId;
    }



    private String _getMode(String mode) {
        return this.mode;
    }
    public String getMode() {
        return _getMode(this.mode);
    }
    public void setMode(String mode) {
        this.mode = mode;
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



    private String _getCreateTime(String createTime) {
        return this.createTime;
    }
    public String getCreateTime() {
        return _getCreateTime(this.createTime);
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



    private String _getUpdateTime(String updateTime) {
        return this.updateTime;
    }
    public String getUpdateTime() {
        return _getUpdateTime(this.updateTime);
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }



    private String _getPublishTime(String publishTime) {
        return this.publishTime;
    }
    public String getPublishTime() {
        return _getPublishTime(this.publishTime);
    }
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }



    private String _getStatus(String status) {
        return this.status;
    }
    public String getStatus() {
        return _getStatus(this.status);
    }
    public void setStatus(String status) {
        this.status = status;
    }



    private String _getFromUserId(String fromUserId) {
        return this.fromUserId;
    }
    public String getFromUserId() {
        return _getFromUserId(this.fromUserId);
    }
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }



    private String _getHasLocation(String hasLocation) {
        return this.hasLocation;
    }
    public String getHasLocation() {
        return _getHasLocation(this.hasLocation);
    }
    public void setHasLocation(String hasLocation) {
        this.hasLocation = hasLocation;
    }



    private String _getHasUserlist(String hasUserlist) {
        return this.hasUserlist;
    }
    public String getHasUserlist() {
        return _getHasUserlist(this.hasUserlist);
    }
    public void setHasUserlist(String hasUserlist) {
        this.hasUserlist = hasUserlist;
    }



    private String _getHasDiscuss(String hasDiscuss) {
        return this.hasDiscuss;
    }
    public String getHasDiscuss() {
        return _getHasDiscuss(this.hasDiscuss);
    }
    public void setHasDiscuss(String hasDiscuss) {
        this.hasDiscuss = hasDiscuss;
    }



    private String _getIsToAll(String isToAll) {
        return this.isToAll;
    }
    public String getIsToAll() {
        return _getIsToAll(this.isToAll);
    }
    public void setIsToAll(String isToAll) {
        this.isToAll = isToAll;
    }



    private String _getMessGroupIds(String messGroupIds) {
        return this.messGroupIds;
    }
    public String getMessGroupIds() {
        return _getMessGroupIds(this.messGroupIds);
    }
    public void setMessGroupIds(String messGroupIds) {
        this.messGroupIds = messGroupIds;
    }



    private String _getMessGroupNames(String messGroupNames) {
        return this.messGroupNames;
    }
    public String getMessGroupNames() {
        return _getMessGroupNames(this.messGroupNames);
    }
    public void setMessGroupNames(String messGroupNames) {
        this.messGroupNames = messGroupNames;
    }



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        Message other =(Message) obj;
        return this.getId().compareTo(other.getId());
    }
}