package ll.com.Parent.controller;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "monitoringdata")
public class MonitoringData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "newsid")
	private long newsid;
	
	@Column(name= "area")
	private String area;

	@Column(name = "pm25")
	private String pm25;

	@Column(name = "co2")
	private String co2;

	@Column(name = "hchoch2")
	private String hchoch2;

	@Column(name = "vocs")
	private String vocs;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getNewsid()
	{
		return newsid;
	}

	public void setNewsid(long newsid)
	{
		this.newsid = newsid;
	}

	public String getPm25()
	{
		return pm25;
	}

	public void setPm25(String pm25)
	{
		this.pm25 = pm25;
	}

	public String getCo2()
	{
		return co2;
	}

	public void setCo2(String co2)
	{
		this.co2 = co2;
	}

	public String getHchoch2()
	{
		return hchoch2;
	}

	public void setHchoch2(String hchoch2)
	{
		this.hchoch2 = hchoch2;
	}

	public String getVocs()
	{
		return vocs;
	}

	public void setVocs(String vocs)
	{
		this.vocs = vocs;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

}