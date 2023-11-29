package com.alataf.microservices.apigateway;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGateWayConfiguration {
	
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		
		return builder.routes()
				.route(p -> p.path("/get")
					.filters(f -> f
							.addRequestHeader("MyHeader", "MyURI")
							.addRequestParameter("Param", "MyValue"))
						.uri("http://httpbin.org:80"))
				//Customize the URL of API's
				.route(p -> p.path("/currency-exchange/**")
						.uri("lb://currency-exchange"))
				.route(p -> p.path("/currency-conversion/**")
						.uri("lb://currency-conversion"))
				.route(p -> p.path("/currency-conversion-feign/**")
						.uri("lb://currency-conversion"))
				.route(p -> p.path("/currency-conversion-new/**")
							.filters(f -> f.rewritePath(
									"currency-conversion-new/(?<segment>.*)", //kisko replace krna hai
									"currency-conversion-feign/${segment}")) //kisase replace krna hai
						.uri("lb://currency-conversion"))
				.build();
		}	
}
