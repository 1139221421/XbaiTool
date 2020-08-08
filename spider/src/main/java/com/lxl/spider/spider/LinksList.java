package com.lxl.spider.spider;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 爬虫获取的博客列表
 * @author
 *
 */
public class LinksList {

	// 用户名，对应一个用户列表，如果用户为新用户则put新的列表
	private static Map<String, ConcurrentHashMap<String, BlogLink>> linkMap = new ConcurrentHashMap <String,ConcurrentHashMap<String, BlogLink>>();

	public static void clearList(String token){
		linkMap.remove(token);
	}

	public static void addLinks(String token, List<BlogLink> links) {

		ConcurrentHashMap<String,BlogLink> linkList;

		if(linkMap.containsKey(token)){
			linkList= linkMap.get(token);
		} else{
			linkList = new ConcurrentHashMap<String,BlogLink>();
		}

		//put links 去重复
		for(int i=0; i<links.size(); ++i){
			String key = links.get(i).getLink();

			if(linkList.containsKey(key)){	//重复，不提交
				continue;
			}

			linkList.put(key, links.get(i));
		}

		linkMap.put(token, linkList);
	}

	public static List<BlogLink> getLinkList(String token) {
		ConcurrentHashMap<String, BlogLink> hash;
		if(linkMap.containsKey(token)){
			hash = linkMap.remove(token);
			return new ArrayList<BlogLink>(hash.values());	//hash to list
		}

		return null;
	}

}
