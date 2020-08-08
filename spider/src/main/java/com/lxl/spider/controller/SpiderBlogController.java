package com.lxl.spider.controller;

import com.alibaba.fastjson.JSON;
import com.lxl.spider.spider.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 博客搬家action
 *
 * @author
 */
@Controller
@RequestMapping("/spider")
public class SpiderBlogController {

    /**
     * http://localhost:8088/spider/blog?url=https%3A%2F%2Fblog.csdn.net%2Fdevcloud
     *
     * @param request
     * @throws IOException
     */
    @ResponseBody
    @GetMapping("/blog")
    protected String spiderBlog(HttpServletRequest request) {
        String link = request.getParameter("url");
        String userCatalog = request.getParameter("user_catalog");
        String sysCatalog = request.getParameter("sys_catalog");
        String type = request.getParameter("reprint");
        String privacy = request.getParameter("priva");
        return spiderBlog(link, userCatalog, sysCatalog, type, privacy);
    }

    public static void main(String[] args) {
        // csdn
        String result = "";
        result = spiderBlog("https://blog.csdn.net/devcloud", null, null, null, null);
        System.out.println(result);
    }

    /**
     * @param link        链接
     * @param userCatalog 博客分类
     * @param sysCatalog  系统博客分类 默认：其他类型
     * @param type        原创：1、转载：4	1
     * @param privacy     公开：0、私有：1	0
     * @return
     */
    private static String spiderBlog(String link, String userCatalog, String sysCatalog, String type, String privacy) {
        if (StringUtils.isBlank(link)) {//id获取失败
            return "link获取失败";
        }

        if (StringUtils.isBlank(userCatalog)) {
            userCatalog = "0";
        }

        if (StringUtils.isBlank(sysCatalog)) {
            sysCatalog = "430381";
        }

        if (StringUtils.isBlank(type)) {
            type = "1";
        }

        if (StringUtils.isBlank(privacy)) {
            privacy = "0";
        }

        LinksList.clearList("normal-user");
        PageProcessor pageProcessor = null;
        try {
            pageProcessor = new BlogPageProcessor(link);
        } catch (Exception e) {
            e.printStackTrace();
            return "暂不支持该博客网站!";
        }
        Spider.create(pageProcessor)
                .addUrl(link)
                .addPipeline(new BlogPipeline()).run();
        List<BlogLink> linkList = LinksList.getLinkList("normal-user");
        System.out.println(JSON.toJSONString(linkList));
        return JSON.toJSONString(linkList);


//        Blog blog = BlogList.getBlog(link); //已存在，不用抓取
//        if (null == blog) {
//            PageProcessor pageProcessor = null;
//            try {
//                pageProcessor = new BlogPageProcessor(link);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "暂不支持该博客网站!";
//            }
//            //爬取博客，结果存放在BLogList中
//            Spider.create(pageProcessor)
//                    .addUrl(link)
//                    .addPipeline(new BlogPipeline()).run();
//            blog = BlogList.getBlog(link);
//        }
//
//        if (blog == null) {
//            return "抓取失败，你懂的，稍后再试！";
//        }
//
//        /**
//         * 可设置blog非必要参数：
//         *	save_as_draft=0;	//	false		保存到草稿 是：1 否：0	0
//         *	catalog;			//	false		博客分类
//         *	abstracts="";		//	false		博客摘要
//         *	tags="";			//	false		博客标签，用逗号隔开
//         *	type=1;				//	false		原创：1、转载：4	1
//         *	origin_url="";		//	false		转载的原文链接
//         *	privacy=0;			//	false		公开：0、私有：1	0
//         *	deny_comment=0;		//	false		允许评论：0、禁止评论：1	0
//         *	auto_content=0;		//	false		自动生成目录：0、不自动生成目录：1	0
//         *	as_top=0;			//	false		非置顶：0、置顶：1	0
//         */
//        blog.setCatalog(userCatalog);
//        blog.setClassification(sysCatalog);
//        blog.setType(type);
//        blog.setPrivacy(privacy);
//
//        if (!"1".equals(type)) {
//            blog.setOrigin_url(link);
//        }
//
//        //todo 博客操作
//        System.out.println(JSON.toJSONString(blog));
//        return JSON.toJSONString(blog);
    }

}
