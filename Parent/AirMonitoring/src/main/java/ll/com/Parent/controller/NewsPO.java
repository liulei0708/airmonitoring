package ll.com.Parent.controller;

import java.util.Date;

public class NewsPO {

	private long id;

	private String title;

	private Date date;

	private String source;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public NewsPO(long pid, String ptitle, Date pdate, String psource) {
		super();
		this.id = pid;
		this.title = ptitle;
		this.date = pdate;
		this.source = psource;
	}
}
