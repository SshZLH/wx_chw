package org.wss.entity;

import java.util.*;



  
public class Response4Wss  implements java.io.Serializable,Comparable {




    private String id;
    /**
     *回复内容类型: text ,imagetext, supertext
     */
    private String contentType;
    /**
     *xml格式内容(用于微信)
     */
    private String xmlContent;
    /**
     *文本内容
     */
    private String content;
    /**
     *图文模板id
     */
    private String newsTemplateId;
    private String newsTemplateName;

    public Response4Wss() {
    }

    public Response4Wss(String id) {
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



    private String _getContentType(String contentType) {
        return this.contentType;
    }
    public String getContentType() {
        return _getContentType(this.contentType);
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }



    private String _getXmlContent(String xmlContent) {
        return this.xmlContent;
    }
    public String getXmlContent() {
        return _getXmlContent(this.xmlContent);
    }
    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
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



    private String _getNewsTemplateId(String newsTemplateId) {
        return this.newsTemplateId;
    }
    public String getNewsTemplateId() {
        return _getNewsTemplateId(this.newsTemplateId);
    }
    public void setNewsTemplateId(String newsTemplateId) {
        this.newsTemplateId = newsTemplateId;
    }



    private String _getNewsTemplateName(String newsTemplateName) {
        return this.newsTemplateName;
    }
    public String getNewsTemplateName() {
        return _getNewsTemplateName(this.newsTemplateName);
    }
    public void setNewsTemplateName(String newsTemplateName) {
        this.newsTemplateName = newsTemplateName;
    }



    public String toString(){
        if(this.getId()==null)
            return "";
        return this.getId()+"";
    }



    public int compareTo(Object obj){
        Response4Wss other =(Response4Wss) obj;
        return this.getId().compareTo(other.getId());
    }
}