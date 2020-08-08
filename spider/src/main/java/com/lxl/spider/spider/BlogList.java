package com.lxl.spider.spider;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 爬虫获取的博客列表
 *
 * @author
 */
public class BlogList {

    //用户名，对应一个用户列表，如果用户为新用户则put新的列表
    private static Map<String, Blog> blogMap = new ConcurrentHashMap<String, Blog>();

    public static void addBlog(Blog blog) {

        if (blogMap.containsKey(blog.getLink())) {
            //已存在博客，有异常，没处理
            blogMap.put(blog.getLink(), blog);
        } else {
            blogMap.put(blog.getLink(), blog);
        }
    }

    public static Blog getBlog(String link) {
        if (blogMap.containsKey(link)) {
            return blogMap.remove(link);
        }
        return null;
    }

}
