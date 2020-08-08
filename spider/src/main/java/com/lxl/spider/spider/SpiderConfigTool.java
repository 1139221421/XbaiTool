package com.lxl.spider.spider;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

public class SpiderConfigTool {

    private String configPath = "/Spider.xml";
    private File file;
    private SAXReader reader;
    private Document doc;
    private Node spiderNode;

    public SpiderConfigTool(String spiderName) throws DocumentException {
        String path = this.getClass().getResource("").getPath();
        path = path.substring(0, path.indexOf("classes") + 7) + configPath;

        file = new File(path);
        if (!file.exists()) {
            return;
        }
        reader = new SAXReader();
        doc = reader.read(file);
        spiderNode = getSpider(spiderName);

    }

    public Node getSpiderNode() {
        return spiderNode;
    }

    @SuppressWarnings("unchecked")
    private Node getSpider(String spiderName) {
        List<Node> list = doc.selectNodes("config/spider-cofig");

        for (Node i : list) {
            Node domain = i.selectSingleNode("domain");
            if (domain != null && domain.getText().equals(spiderName)) {
                return i;
            }
        }

        return null;
    }

    public Document getDoc() {
        return doc;
    }

}
