package org.wss.entity;

import java.util.*;



  
public class WeixinMember  implements java.io.Serializable,Comparable {



    private String id;
    /**
     *会员名字
     */
    private String name;
    /**
     *会员的微信名字
     */
    private String nickname;
    /**
     *手机
     */
    private String mobile;
    /**
     *会员性别：1：男性 2：女性 0:未知
     */
    private String gender;
    /**
     *会员邮箱
     */
    private String email;
    /**
     *会员微信头像
     */
    private String avatar;
    /**
     *关注状态: 1=已关注，2=已冻结，4=未关注
     */
    private String status;
    /**
     *关注日期
     */
    private String regdate;
    /**
     *地址
     */
    private String addr;
    /**
     *二维码参数
     */
    private String sceneStr;
    /**
     *open_id
     */
    private String openId;
    /**
     *会员备注
     */
    private String memo;
    /**
     *用户所在的分组ID
     */
    private String groupId;
    /**
     *会员状态：1：停止 0:启用
     */
    private String isdel;

    public WeixinMember() {
    }

    public WeixinMember(String id) {
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



    private String _getName(String name) {
        return this.name;
    }
    public String getName() {
        return _getName(this.name);
    }
    public void setName(String name) {
        this.name = name;
    }



    private String _getNickname(String nickname) {
        return this.nickname;
    }
    public String getNickname() {
        return _getNickname(this.nickname);
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



    private String _getMobile(String mobile) {
        return this.mobile;
    }
    public String getMobile() {
        return _getMobile(this.mobile);
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    private String _getGender(String gender) {
        return this.gender;
    }
    public String getGender() {
        return _getGender(this.gender);
    }
    public void setGender(String gender) {
        this.gender = gender;
    }



    private String _getEmail(String email) {
        return this.email;
    }
    public String getEmail() {
        return _getEmail(this.email);
    }
    public void setEmail(String email) {
        this.email = email;
    }



    private String _getAvatar(String avatar) {
        return this.avatar;
    }
    public String getAvatar() {
        return _getAvatar(this.avatar);
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
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



    private String _getRegdate(String regdate) {
        return this.regdate;
    }
    public String getRegdate() {
        return _getRegdate(this.regdate);
    }
    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }



    private String _getAddr(String addr) {
        return this.addr;
    }
    public String getAddr() {
        return _getAddr(this.addr);
    }
    public void setAddr(String addr) {
        this.addr = addr;
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



    private String _getOpenId(String openId) {
        return this.openId;
    }
    public String getOpenId() {
        return _getOpenId(this.openId);
    }
    public void setOpenId(String openId) {
        this.openId = openId;
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



    private String _getGroupId(String groupId) {
        return this.groupId;
    }
    public String getGroupId() {
        return _getGroupId(this.groupId);
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }



    private String _getIsdel(String isdel) {
        return this.isdel;
    }
    public String getIsdel() {
        return _getIsdel(this.isdel);
    }
    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        WeixinMember other =(WeixinMember) obj;
        return this.getId().compareTo(other.getId());
    }
}