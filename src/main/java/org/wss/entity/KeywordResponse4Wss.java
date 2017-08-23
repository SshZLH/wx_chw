package org.wss.entity;

import java.util.*;



  
public class KeywordResponse4Wss  implements java.io.Serializable,Comparable {





    private String id;
    /**
     *关键词
     */
    private String keyword;
    /**
     *消息Id
     */
    private String respId;

    public KeywordResponse4Wss() {
    }

    public KeywordResponse4Wss(String id) {
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



    private String _getKeyword(String keyword) {
        return this.keyword;
    }
    public String getKeyword() {
        return _getKeyword(this.keyword);
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
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



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        KeywordResponse4Wss other =(KeywordResponse4Wss) obj;
        return this.getId().compareTo(other.getId());
    }
}