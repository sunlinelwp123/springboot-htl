package com.open.boot.test.model;




public class ThymeleafDome {

	    private String title;
	    private String content;
	    private String url;
	    
	    public ThymeleafDome(String title,String content, String url) {
	    	this.title = title;
	    	this.content = content;
	    	this.url = url;
	    	
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
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}

	
}
