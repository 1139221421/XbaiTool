package com.lxl.spider.spider;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * 将爬取的博客代码部分转换为oschina博客代码类型
 * @author
 *
 */
public class OscBlogReplacer {

	//csdn, osc	不一致的css属性的对应关系
	private Hashtable<String, String> hashtable = null;

	public OscBlogReplacer(Hashtable<String, String> hashtable) {
		this.hashtable = hashtable;
	}

	public void setHashtable(Hashtable<String, String> hash){
		hashtable = hash;
	}

	@SuppressWarnings("rawtypes")
	public String replace(List<String> rexBegin, List<String> rexEnd, String oldString) {
		String osc=oldString;

		//标签头
		for(int i=0; i<rexBegin.size(); ++i){
			osc=osc.replaceAll(rexBegin.get(i),"<pre class=\"brush:$1;toolbar:true\">");
		}

		//标签尾
		for(int i=0; i<rexEnd.size(); ++i){
			osc=osc.replaceAll(rexEnd.get(i),"</pre>");
		}


		//1.不一致的css属性替换
		for(Iterator itr = hashtable.keySet().iterator(); itr.hasNext();){
			String key = (String) itr.next();
			String value = (String) hashtable.get(key);
			osc=osc.replaceAll("<pre class=\"brush:"+key+";toolbar:true\">", "<pre class=\"brush:"+value+";toolbar:true\">");
		}

		return osc;
	}
}
