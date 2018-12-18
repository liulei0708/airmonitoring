package ll.com.Parent;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Component
public class SecurityFilter implements GlobalFilter, Ordered
{

	@Override
	public int getOrder()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
	{
		// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
		try
		{
			String path = exchange.getRequest().getPath().value();

			String login = path.substring(path.length() - 5, path.length());

			// System.out.println("path=====================" + path);
			// System.out.println("login====================" + login);

			if (login.equals("login"))
			{
				return chain.filter(exchange);
			}

			String token = exchange.getRequest().getHeaders().getFirst("spatial");
			// System.out.println("token=====================" + token);

			if (token == null || token.isEmpty() || token.length() != 24)
			{
				// System.out.println("token不合法");
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

				byte[] bytes = "{\"code\": 401,  \"data\": {}}".getBytes(StandardCharsets.UTF_8);
				DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
				return exchange.getResponse().writeWith(Flux.just(buffer));
			} else
			{
				// String[] split = path.split("/");
				// if (split.length > 0)
				// {
				// String last = split[split.length - 1];
				// System.out.println("last>>>>>>>>>>>>>>>>>>>" + last);
				// byte[] lastbytes = last.getBytes();
				// Base64Encoder base64 = new Base64Encoder();
				// String s = base64.encode(lastbytes);
				// if (s.equals(token))
				// {
				return chain.filter(exchange);
				// }
				// }
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			// System.out.println("进入了异常");
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
		// System.out.println("马上结束");
		// exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		// return exchange.getResponse().setComplete();
	}

	@Bean
	public WebFilter corsFilter()
	{
		return (ServerWebExchange ctx, WebFilterChain chain) ->
		{
			ServerHttpRequest request = ctx.getRequest();
			if (!CorsUtils.isCorsRequest(request))
			{
				return chain.filter(ctx);
			}

			HttpHeaders requestHeaders = request.getHeaders();
			ServerHttpResponse response = ctx.getResponse();
			HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
			HttpHeaders headers = response.getHeaders();
			headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
			headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
			if (requestMethod != null)
			{
				headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
			}
			headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
			headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "18000L");
			if (request.getMethod() == HttpMethod.OPTIONS)
			{
				response.setStatusCode(HttpStatus.OK);
				return Mono.empty();
			}
			// System.out.println("跨域处理结束!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return chain.filter(ctx);
		};
	}
}
