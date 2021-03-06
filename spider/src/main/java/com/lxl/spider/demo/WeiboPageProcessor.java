package com.lxl.spider.demo;

import java.util.ArrayList;
import java.util.List;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class WeiboPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static void main(String[] args) {
        Spider.create(new WeiboPageProcessor())
                .addUrl("https://m.weibo.cn/api/container/getIndex?containerid=102803_ctg1_8999_-_ctg1_8999_home")
                .run();
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        List<String> urls = new ArrayList<String>();
//        for (int i = 2; i < 30; i++) {
//            urls.add("https://m.weibo.cn/api/container/getIndex?containerid=102803_ctg1_8999_-_ctg1_8999_home&page="
//                    + i);
//        }
        page.addTargetRequests(urls);
        String pagestring = page.getRawText();
        if (pagestring.length() > 100) {
            List<String> list = new JsonPathSelector("$.data.cards[*].mblog").selectList(pagestring);
            List<JSONObject> result = new ArrayList<>();
            for (String s : list) {
                result.add(JSON.parseObject(s));
            }
            System.out.println(JSON.toJSONString(result));
        }
    }

}
