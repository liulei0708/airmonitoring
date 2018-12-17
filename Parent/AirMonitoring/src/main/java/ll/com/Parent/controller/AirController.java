package ll.com.Parent.controller;

import java.io.File;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.thoughtworks.xstream.core.util.Base64Encoder;

@RestController
@RequestMapping(value = "air")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods =
{ RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD }, origins = "*")
public class AirController
{
	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private MonitoringDataRepository monitoringDataRepository;

	@Autowired
	private AboutRepository aboutRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdRepository adRepository;

	@Value("${web.upload-path}")
	private String path;

	@Value("${server.port}")
	private int port;

	private String lastPath = "/imgs/";

	/***
	 * 获取文章列表:ID、title、date、source
	 * 
	 * @return
	 */
	@RequestMapping(value = "news/list")
	public ResultData<PageNewsPO> newsList(@RequestParam(value = "pagenum") int pageNum,
			@RequestParam(value = "pagesize") int pageSize)
	{
		ResultData<PageNewsPO> r = new ResultData<>();
		try
		{
			PageNewsPO pno = new PageNewsPO();
			Pageable p = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
			List<NewsPO> nlist = newsRepository.getNewsList(p);
			Page<News> pageobj = newsRepository.findAll(p);
			pno.setPageNumber(pageobj.getNumber() + 1);
			pno.setTotalPages(pageobj.getTotalPages());
			pno.setPageSize(pageobj.getSize());
			pno.setNewsList(nlist);
			r.setData(pno);
			r.setCode(200);
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}

		return r;
	}

	@RequestMapping(value = "news/test")
	public ResultData<List<NewsPO>> test()
	{
		ResultData<List<NewsPO>> r = new ResultData<>();
		try
		{
			Pageable p = PageRequest.of(0, 2, Sort.Direction.DESC, "id");
			List<NewsPO> nlist = newsRepository.getNewsList(p);
			Page<News> pages = newsRepository.findAll(p);
			System.out.println("============总 页  数：" + pages.getTotalPages());
			System.out.println("============每页条数：" + pages.getSize());
			System.out.println("============当 前  页：" + pages.getNumber());
			r.setData(nlist);
			r.setCode(200);
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 添加新闻
	 * 
	 * @param title
	 * @param content
	 * @param date
	 * @return newsId
	 */
	@RequestMapping(value = "news/add", method = RequestMethod.POST)
	public ResultData<News> newsadd(@RequestParam(value = "title") String title,
			@RequestParam(value = "content") String content, @RequestParam(value = "source") String source,
			@RequestParam(value = "date") String date)
	{
		News n = new News();
		n.setTitle(title);
		n.setContent(content);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = new Date();
		try
		{
			dt = sf.parse(date);
		} catch (ParseException e)
		{
			// dt = new Date();
			e.printStackTrace();
		}
		n.setDate(dt);
		n.setSource(source);
		newsRepository.save(n);
		ResultData<News> r = new ResultData<>();
		r.setCode(200);
		r.setData(n);

		return r;
	}

	/**
	 * 编辑新闻
	 * 
	 * @param title
	 * @param content
	 * @param source
	 * @param date
	 * @return
	 */
	@RequestMapping(value = "news/update", method = RequestMethod.POST)
	public ResultData<News> newsupdate(@RequestParam(value = "id") long id, @RequestParam(value = "title") String title,
			@RequestParam(value = "content") String content, @RequestParam(value = "source") String source,
			@RequestParam(value = "date") String date)
	{
		ResultData<News> r = new ResultData<>();
		r.setCode(201);
		Optional<News> optional = newsRepository.findById(id);
		if (optional.isPresent())
		{

			News n = optional.get();
			n.setTitle(title);
			n.setContent(content);

			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = new Date();
			try
			{
				dt = sf.parse(date);
			} catch (ParseException e)
			{
				e.printStackTrace();
			}
			n.setDate(dt);
			n.setSource(source);
			newsRepository.save(n);
			r.setCode(200);
		} else
		{
		}

		return r;
	}

	/**
	 * 删除一条新闻
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping(value = "news/del")
	public ResultData<Long> newsdel(@RequestParam(value = "id") long id)
	{
		ResultData<Long> r = new ResultData<>();
		try
		{
			newsRepository.deleteById(id);
			monitoringDataRepository.deleteByNewsId(id);
			r.setCode(200);
			r.setData(id);
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	/***
	 * 添加一条监测数据
	 * 
	 * @param newsid
	 * @param pm25
	 * @param co2
	 * @param hchoch2
	 * @param vocs
	 * @return
	 */
	@PostMapping("data/add")
	public long dataadd(@RequestParam(value = "newsid") long newsid, @RequestParam(value = "pm25") String pm25,
			@RequestParam(value = "co2") String co2, @RequestParam(value = "hchoch2") String hchoch2,
			@RequestParam(value = "vocs") String vocs)
	{
		MonitoringData md = new MonitoringData();
		md.setNewsid(newsid);
		md.setPm25(pm25);
		md.setCo2(co2);
		md.setHchoch2(hchoch2);
		md.setVocs(vocs);
		monitoringDataRepository.save(md);
		return md.getId();
	}

	/***
	 * 添加 监测数据集合
	 * 
	 * @param list
	 * @return 0表示成功 1表示失败
	 */
	@PostMapping("monitordata/addlist")
	public ResultData<Long> dataadd(@RequestBody List<MonitoringData> list)
	{
		ResultData<Long> r = new ResultData<Long>();
		try
		{
			monitoringDataRepository.saveAll(list);
			r.setCode(200);
			r.setData(0l);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 修改监测数据集合
	 * 
	 * @param list
	 * @return
	 */
	@PostMapping("monitordata/updatelist")
	public ResultData<Long> dataupdate(@RequestBody List<MonitoringData> list)
	{
		ResultData<Long> r = new ResultData<Long>();
		try
		{
			monitoringDataRepository.saveAll(list);
			r.setCode(200);
			r.setData(0l);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 根据NewsID删除监测数据
	 * 
	 * @param newsid
	 * @return
	 */
	@PostMapping(value = "monitordata/dellist")
	public ResultData<Long> datadel(@RequestParam(value = "newsid") long newsid)
	{
		ResultData<Long> r = new ResultData<>();
		try
		{
			monitoringDataRepository.deleteByNewsId(newsid);
			r.setCode(200);
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 根据NewsID查询有没有监测数据
	 * 
	 * @param newsid
	 * @return
	 */
	@GetMapping(value = "monitordata/hasdata")
	public ResultData<Boolean> hasData(@RequestParam(value = "newsid") long newsid)
	{
		ResultData<Boolean> r = new ResultData<>();
		try
		{
			Boolean b = monitoringDataRepository.existsBynewsid(newsid);
			r.setData(b);
			r.setCode(200);
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 根据NewsID查询所属的监测数据
	 * 
	 * @param newsid
	 * @return
	 */
	@GetMapping(value = "monitordata/data")
	public ResultData<List<MonitoringData>> getDataByNewId(@RequestParam(value = "newsid") long newsid)
	{
		ResultData<List<MonitoringData>> r = new ResultData<>();
		try
		{
			List<MonitoringData> list = monitoringDataRepository.getBynewsid(newsid);
			r.setCode(200);
			r.setData(list);
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 根据NewId查询 News 和关联的 MonitoringData 集合
	 * 
	 * @param newsid
	 * @return
	 */
	@GetMapping(value = "/news/get")
	public ResultData<NewsData> getNewsById(@RequestParam(value = "newsid") long newsid)
	{
		ResultData<NewsData> r = new ResultData<>();
		try
		{
			News n = newsRepository.getNewsById(newsid);
			List<MonitoringData> datas = monitoringDataRepository.getBynewsid(newsid);
			NewsData nd = new NewsData();
			nd.setNews(n);
			nd.setDataList(datas);
			r.setCode(200);
			r.setData(nd);
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 查询关于，最多只有一条数据
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "about/get")
	public ResultData<About> getAboutById()
	{
		ResultData<About> r = new ResultData<>();
		try
		{
			List<About> list = aboutRepository.findAll();
			if (list.size() > 0)
			{
				r.setData(list.get(0));
			} else
			{
				// r.setData(new About());
			}
			r.setCode(200);
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 保存/修改 关于
	 * 
	 * @param id
	 * @param addr
	 * @param tel
	 * @param desc
	 * @return
	 */
	@PostMapping(value = "about/save")
	public ResultData<About> saveAbout(@RequestParam(value = "id") long id, @RequestParam(value = "addr") String addr,
			@RequestParam(value = "tel") String tel, @RequestParam(value = "description") String description)
	{
		ResultData<About> r = new ResultData<>();
		r.setCode(201);
		try
		{
			if (id > 0)
			{
				Optional<About> optional = aboutRepository.findById(id);
				if (optional.isPresent())
				{
					About about = optional.get();
					about.setAddr(addr);
					about.setTel(tel);
					about.setDescription(description);
					about = aboutRepository.save(about);
					r.setData(about);
					r.setCode(200);
				}
			}
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	@PostMapping(value = "login")
	public ResultData<User> Longin(@RequestParam(value = "username") String userName,
			@RequestParam(value = "pwd") String pwd)
	{
		ResultData<User> r = new ResultData<>();
		try
		{
			User u = userRepository.findByUserNameAndPassWord(userName, pwd);
			if (u != null)
			{
				u.setPassWord(makeToken(userName + pwd));
				r.setData(u);
				r.setCode(200);
			} else
			{
				r.setCode(201);
			}
		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	public String makeToken(String token)
	{
		// String token = (System.currentTimeMillis() + new Random().nextInt(999999999))
		// + "";
		try
		{
			MessageDigest md = MessageDigest.getInstance("md5");
			byte md5[] = md.digest(token.getBytes());
			Base64Encoder encoder = new Base64Encoder();
			return encoder.encode(md5);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取所有ad
	 * 
	 * @return
	 */
	@GetMapping(value = "ad/list")
	public ResultData<List<Ad>> getAdList()
	{
		ResultData<List<Ad>> r = new ResultData<>();
		try
		{
			List<Ad> adlist = adRepository.findAll();

			r.setCode(200);
			r.setData(adlist);
		} catch (Exception e)
		{
			r.setCode(201);
		}
		return r;
	}

	/**
	 * 修改广告：标题 链接、图片地址
	 * 
	 * @param id
	 * @param title
	 * @param link
	 * @param img
	 * @return
	 */
	@PostMapping(value = "ad/save")
	public ResultData<String> saveAd(@RequestParam(value = "id") long id, @RequestParam(value = "title") String title,
			@RequestParam(value = "link") String link, @RequestParam(value = "img") String img)
	{
		ResultData<String> r = new ResultData<>();
		try
		{
			Optional<Ad> optional = adRepository.findById(id);
			if (optional.isPresent())
			{
				Ad ad = optional.get();
				ad.setTitle(title);
				ad.setLink(link);
				ad.setImg(img);
				adRepository.save(ad);
				r.setCode(HttpStatus.SC_OK);
			} else
			{
				r.setCode(201);
			}

		} catch (Exception e)
		{
			r.setCode(201);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 设置广告是否可用；
	 * 
	 * @param id
	 * @param isuse
	 * @return
	 */
	@PostMapping(value = "ad/setuse")
	public ResultData<String> updateAdUse(@RequestParam(value = "id") long id,
			@RequestParam(value = "isuse") Boolean isuse)
	{
		ResultData<String> r = new ResultData<>();
		try
		{
			Optional<Ad> optional = adRepository.findById(id);
			if (optional.isPresent())
			{
				Ad ad = optional.get();
				ad.setIsuse(isuse ? 0 : 1);
				adRepository.save(ad);
				r.setCode(HttpStatus.SC_OK);
			} else
			{
				r.setCode(201);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			r.setCode(201);
		}
		return r;
	}

	/**
	 * 上传图片
	 * 
	 * @param img
	 * @return
	 */
	@PostMapping(value = "news/upload")
	public ResultData<String> upload(@RequestParam(value = "upfile") MultipartFile img)
	{
		ResultData<String> r = new ResultData<>();
		String url = "";
		if (img != null)
		{
			System.out.println("【*** UploadRest ***】文件名称：" + img.getOriginalFilename() + "、文件大小：" + img.getSize());

			try
			{
				long sizeM = img.getSize() / 1024 / 1024;
				if (sizeM > 2)
				{
					r.setCode(201);
					r.setMsg("图片不能超2MB！");
					return r;
				}
				// 获取本机地址
				InetAddress addr = InetAddress.getLocalHost();
				String ip = addr.getHostAddress();
				// url = "http://" + ip + ":" + port + lastPath + img.getOriginalFilename();
				url = "http://www.shiliuyuan.club/" + img.getOriginalFilename();
				System.out.println("服务器地址： " + url);
				System.out.println("上传地址: " + path);

				byte[] imgbytes = img.getBytes();
				System.out.println(path);
				File saveFile = new File(path + img.getOriginalFilename());

				if (!saveFile.getParentFile().exists())
				{
					System.out.println("=====准备创建目录");
					saveFile.getParentFile().mkdirs();
				}

				System.out.println(saveFile.getAbsolutePath());
				FileCopyUtils.copy(imgbytes, saveFile);
				r.setCode(200);
				r.setData(url);

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} else
		{
			System.out.println("img 为空!!!");
		}

		return r;
	}

	@PostMapping(value = "upfiletest")
	public void uploadImg(HttpServletRequest request)
	{
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("upfile");
		MultipartFile mfile = files.get(0);
		String fileName = mfile.getOriginalFilename();
		System.out.println("接收到上传数据，图片名称为 ：" + fileName);

		File savefile = new File(fileName);
		try
		{
			byte[] imgbytes = mfile.getBytes();
			FileCopyUtils.copy(imgbytes, savefile);
			System.out.println("上传路径：" + savefile.getAbsolutePath());
			;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("上传成功");
	}

}
