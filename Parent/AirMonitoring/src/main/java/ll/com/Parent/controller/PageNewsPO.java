package ll.com.Parent.controller;

import java.util.List;

public class PageNewsPO {

	/**
	 * 每页条数
	 */
	private int pageSize;

	/**
	 * 总页数
	 */
	private int totalPages;

	/**
	 * 当前页
	 */
	private int pageNumber;

	/**
	 * 新闻列表对象集合
	 */
	private List<NewsPO> NewsList;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<NewsPO> getNewsList() {
		return NewsList;
	}

	public void setNewsList(List<NewsPO> newsList) {
		NewsList = newsList;
	}
	
	

}
