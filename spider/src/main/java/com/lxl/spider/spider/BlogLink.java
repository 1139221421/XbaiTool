package com.lxl.spider.spider;


import java.util.Date;

/**
 * 博客链接 beans
 *
 * @author
 */
public class BlogLink {

    //private String access_token="";	//	true	oauth2_token获取的access_token

    private String title = "";            //	true		博客标题
    private String link = "";                //	原博客链接
    private String content = "";                //	内容
    private Date time;
    private long id;                    //

    public BlogLink(Blog blog) {
        title = blog.getTitle();
        link = blog.getLink();
    }

    public BlogLink() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
