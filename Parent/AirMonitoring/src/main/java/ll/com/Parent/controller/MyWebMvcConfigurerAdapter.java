package ll.com.Parent.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurationSupport
{
	@Value("${web.upload-path}")
	private String path;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		System.out.println("==========================================" + path);
		// 指向外部目录
		registry.addResourceHandler("imgs//**").addResourceLocations("file:" + path);
		super.addResourceHandlers(registry);
	}
}
