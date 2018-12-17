package ll.com.Parent.controller;

import java.io.Serializable;
import java.util.List;

public class NewsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private News news;
	private List<MonitoringData> dataList;

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public List<MonitoringData> getDataList() {
		return dataList;
	}

	public void setDataList(List<MonitoringData> dataList) {
		this.dataList = dataList;
	}

}
