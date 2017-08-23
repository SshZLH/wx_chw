package org.wss.entity;

import java.util.*;



  
public class AttentionResponse4Wss  implements java.io.Serializable,Comparable {




    private String id;
    /**
     *消息Id
     */
    private String respId;
    /**
     *关键字，用于扩展应用，如：可以根据关注用户信息回复不同的消息
     */
    private String keyword;

    public AttentionResponse4Wss() {
    }

    public AttentionResponse4Wss(String id) {
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



    private String _getRespId(String respId) {
        return this.respId;
    }
    public String getRespId() {
        return _getRespId(this.respId);
    }
    public void setRespId(String respId) {
        this.respId = respId;
    }



    private String _getKeyword(String keyword) {
        return this.keyword;
    }
    public String getKeyword() {
        return _getKeyword(this.keyword);
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        AttentionResponse4Wss other =(AttentionResponse4Wss) obj;
        return this.getId().compareTo(other.getId());
    }
}