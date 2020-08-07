package com.lxl.spider.demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class OschinaBlogPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.oschina.net");

    @Override
    public void process(Page page) {
//        List<String> links = page.getHtml().links().regex("http://www\\.oschina\\.net\\d+").all();
//        page.addTargetRequests(links);
//        page.putField("title", page.getHtml().xpath("//div[@class='blog-item']/div/a/text()").all());
//        page.putField("content", page.getHtml().xpath("//div[@class='blog-item']/div/div[@class='description']/p/text()").all());
////        page.putField("user", page.getHtml().$("div.blog-item>div>div.extra>div>div:nth-child(1)>a").all());
//        page.putField("user", page.getHtml().xpath("//div[@class='blog-item']/div/div[@class='extra']/div/div[1]/a/text()").all());

        List<Selectable> list = page.getHtml().xpath("//div[@class='blog-item']").nodes();
        for (Selectable selectable : list) {
            System.out.println("============title:" + selectable.xpath("//div[@class='blog-item']/div/a/text()").toString()
                    + " content:" + selectable.xpath("//div[@class='blog-item']/div/div[@class='description']/p/text()").toString()
                    + " user:" + selectable.xpath("//div[@class='blog-item']/div/div[@class='extra']/div/div[1]/a/text()").toString());
        }
    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new OschinaBlogPageProcessor()).addUrl("https://www.oschina.net/blog?tab=recommend")
                .addPipeline(new ConsolePipeline()).run();
    }
}
