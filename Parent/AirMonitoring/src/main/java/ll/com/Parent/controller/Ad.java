package ll.com.Parent.controller;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ad")
public class Ad implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "link")
	private String link;

	@Column(name = "img")
	private String img;

	/**
	 * 长度为1 0是启用 1是禁用
	 */
	@Column(name = "isuse")
	private int isuse;

	@Transient
	private Boolean use;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public int getIsuse()
	{
		return isuse;
	}

	public void setIsuse(int isuse)
	{
		this.isuse = isuse;
	}

	public Boolean getUse()
	{
		return this.isuse == 0;
	}

	public void setUse(Boolean use)
	{
		this.use = use;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
