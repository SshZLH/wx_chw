package com.whc.wx.web.util.model;

import com.wanhuchina.common.util.weixin.cgi.BaseDO;

/**
 * Created by shenshanghua
 * Email shenshanghua@wanhuchina.com
 * Date：2017/7/17
 * Time：16:07
 * 问题反馈的json接收类
 */
public class userQuestionParm extends BaseDO{

    private String quest_type;
    private String quest_comment;
    private String quest_order_id;
    private String quest_source_url;
    private String quest_access_ip;
    private String quest_access_addr;
    private String quest_browse_message;

    public String getQuest_type() {
        return quest_type;
    }

    public void setQuest_type(String quest_type) {
        this.quest_type = quest_type;
    }

    public String getQuest_comment() {
        return quest_comment;
    }

    public void setQuest_comment(String quest_comment) {
        this.quest_comment = quest_comment;
    }

    public String getQuest_order_id() {
        return quest_order_id;
    }

    public void setQuest_order_id(String quest_order_id) {
        this.quest_order_id = quest_order_id;
    }

    public String getQuest_source_url() {
        return quest_source_url;
    }

    public void setQuest_source_url(String quest_source_url) {
        this.quest_source_url = quest_source_url;
    }

    public String getQuest_access_ip() {
        return quest_access_ip;
    }

    public void setQuest_access_ip(String quest_access_ip) {
        this.quest_access_ip = quest_access_ip;
    }

    public String getQuest_access_addr() {
        return quest_access_addr;
    }

    public void setQuest_access_addr(String quest_access_addr) {
        this.quest_access_addr = quest_access_addr;
    }

    public String getQuest_browse_message() {
        return quest_browse_message;
    }

    public void setQuest_browse_message(String quest_browse_message) {
        this.quest_browse_message = quest_browse_message;
    }
}
