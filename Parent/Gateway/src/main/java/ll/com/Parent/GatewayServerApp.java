package ll.com.Parent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayServerApp
{
	public static void main(String[] args)
	{
		System.out.println("Hello GatewayServerApp!");
		SpringApplication.run(GatewayServerApp.class, args);
	}

}
