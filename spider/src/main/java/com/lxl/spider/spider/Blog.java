package com.lxl.spider.spider;

import org.dom4j.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 博客 beans
 * @author oscfox
 *
 */
public class Blog {

	//private String access_token="";	//	true	oauth2_token获取的access_token

	private String 	title="";			//	true		博客标题
	private String 	content="";			//	true		博客内容
	private String 	classification="430381";//	true		系统博客分类 默认：其他类型
	private String 	save_as_draft="0";	//	false		保存到草稿 是：1 否：0	0
	private String 	catalog;			//	false		博客分类
	private String 	abstracts="";		//	false		博客摘要
	private String 	tags="";			//	false		博客标签，用逗号隔开
	private String 	type="1";			//	false		原创：1、转载：4	1
	private String 	origin_url="";		//	false		转载的原文链接
	private String 	privacy="0";		//	false		公开：0、私有：1	0
	private String 	deny_comment="0";	//	false		允许评论：0、禁止评论：1	0
	private String 	auto_content="0";	//	false		自动生成目录：0、不自动生成目录：1	0
	private String 	as_top="0";			//	false		非置顶：0、置顶：1	0

	private String link="";				//	原博客链接
	private long id;					//

	public Blog() {

	}

	@SuppressWarnings("unchecked")
	public Blog(Node itemNode) {
		title = itemNode.selectSingleNode("title").getText();
        link = itemNode.selectSingleNode("link").getText();
        content = itemNode.selectSingleNode("content:encoded").getText();

        List<String> tagslist = new ArrayList<String>();
        List<Node> nodes=itemNode.selectNodes("category[@domain='post_tag']");

        for(Node n : nodes){
        	tagslist.add(n.getText());
        }

        tags = tagslist.toString();
        if(tags != null)
        	tags = tags.substring(1, tags.length()-1);
	}

	public Blog(Map<String, Object> blogMap) throws Exception {

		Object title=blogMap.get("title");						//	true		博客标题
		Object content=blogMap.get("content");					//	true		博客内容
		Object classification = blogMap.get("classification");	//	true		系统博客分类
		Object save_as_draft = blogMap.get("save_as_draft");	//	false		保存到草稿 是：1 否：0	0
		Object catalog = blogMap.get("catalog");				//	false		博客分类
		Object abstracts = blogMap.get("abstracts");			//	false		博客摘要
		Object tags = blogMap.get("tags");						//	false		博客标签，用逗号隔开
		Object type = blogMap.get("type");						//	false		原创：1、转载：4	1
		Object origin_url = blogMap.get("origin_url");			//	false		转载的原文链接
		Object privacy = blogMap.get("privacy");				//	false		公开：0、私有：1	0
		Object deny_comment = blogMap.get("deny_comment");		//	false		允许评论：0、禁止评论：1	0
		Object auto_content = blogMap.get("auto_content");		//	false		自动生成目录：0、不自动生成目录：1	0
		Object as_top = blogMap.get("as_top");					//	false		非置顶：0、置顶：1	0
		Object link = blogMap.get("link");						//	原博客链接
		Object id = blogMap.get("id");							//

		if(null == content || null == title){
			throw new Exception("blog缺乏必要参数");
		}

		this.setContent(content.toString());
		this.setTitle(title.toString());

		if(null != classification){
			this.setClassification(classification.toString());
		}

		if(null != save_as_draft){
			this.setSave_as_draft(save_as_draft.toString());
		}

		if(null != catalog){
			this.setCatalog(catalog.toString());
		}

		if(null != abstracts){
			this.setAbstracts(abstracts.toString());
		}

		if(null != tags){
			this.setTags(tags.toString());
		}

		if(null != type){
			this.setType(type.toString());
		}

		if(null != origin_url){
			this.setOrigin_url(origin_url.toString());
		}

		if(null != privacy){
			this.setPrivacy(privacy.toString());
		}

		if(null != deny_comment){
			this.setDeny_comment(deny_comment.toString());
		}

		if(null != auto_content){
			this.setAuto_content(auto_content.toString());
		}

		if(null != as_top){
			this.setAs_top(as_top.toString());
		}

		if(null != id){
			this.setId((Long)id);
		}

		if(null != link){
			this.setLink(link.toString());
		}
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTags(){
		return tags;
	}

	public String getTagString() {
		String string = tags.toString();
		return string.substring(1, string.lastIndexOf(']'));

	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return the save_as_draft
	 */
	public String getSave_as_draft() {
		return save_as_draft;
	}

	/**
	 * @param save_as_draft the save_as_draft to set
	 */
	public void setSave_as_draft(String save_as_draft) {
		this.save_as_draft = save_as_draft;
	}

	/**
	 * @return the catalog
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * @param catalog the catalog to set
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	/**
	 * @return the abstracts
	 */
	public String getAbstracts() {
		return abstracts;
	}

	/**
	 * @param abstracts the abstracts to set
	 */
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the origin_url
	 */
	public String getOrigin_url() {
		return origin_url;
	}

	/**
	 * @param origin_url the origin_url to set
	 */
	public void setOrigin_url(String origin_url) {
		this.origin_url = origin_url;
	}

	/**
	 * @return the privacy
	 */
	public String getPrivacy() {
		return privacy;
	}

	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	/**
	 * @return the deny_comment
	 */
	public String getDeny_comment() {
		return deny_comment;
	}

	/**
	 * @param deny_comment the deny_comment to set
	 */
	public void setDeny_comment(String deny_comment) {
		this.deny_comment = deny_comment;
	}

	/**
	 * @return the auto_content
	 */
	public String getAuto_content() {
		return auto_content;
	}

	/**
	 * @param auto_content the auto_content to set
	 */
	public void setAuto_content(String auto_content) {
		this.auto_content = auto_content;
	}

	/**
	 * @return the as_top
	 */
	public String getAs_top() {
		return as_top;
	}

	/**
	 * @param as_top the as_top to set
	 */
	public void setAs_top(String as_top) {
		this.as_top = as_top;
	}
}
