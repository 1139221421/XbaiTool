package com.lxl.spider.spider;

import cn.hutool.core.date.DateUtil;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.*;


/**
 * 成功blog并保存至BlogList
 *
 * @author
 */
public class BlogPipeline implements Pipeline {

    private Map<String, Object> fields = new HashMap<String, Object>();

    private String token;

    public BlogPipeline(String token) {
        this.token = token;
    }

    public BlogPipeline() {
        this.token = "normal-user";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(ResultItems resultItems, Task task) {

        fields = resultItems.getAll();

        if ((boolean) fields.get("getlinks")) {
            List<String> titles = (ArrayList<String>) fields.get("titles");
            List<String> links = (ArrayList<String>) fields.get("links");
            List<String> contents = (ArrayList<String>) fields.get("contents");
            List<String> times = (ArrayList<String>) fields.get("times");

            if (null == titles || null == links) {
                return;
            }

            List<BlogLink> linklist = new ArrayList<BlogLink>();

            for (int i = 0; i < titles.size(); ++i) {
                BlogLink blogLink = new BlogLink();
                blogLink.setTitle(titles.get(i));
                blogLink.setLink(links.get(i));
                blogLink.setContent(contents == null ? null : contents.get(i));
                blogLink.setTime(times == null ? null : DateUtil.parseDateTime(times.get(i)));
                linklist.add(blogLink);
            }

            LinksList.addLinks(token, linklist);

        } else {

            Blog oscBlog = null;
            try {
                oscBlog = new Blog(fields);
                oscBlog.setLink(resultItems.getRequest().getUrl());
            } catch (Exception e) {
                //e.printStackTrace();
                return;
            }

            BlogList.addBlog(oscBlog);
            List<BlogLink> links = new ArrayList<BlogLink>();
            BlogLink blogLink = new BlogLink();
            blogLink.setLink(oscBlog.getLink());
            blogLink.setTitle(oscBlog.getTitle());
            links.add(blogLink);
            LinksList.addLinks(token, links);
        }

    }
}
