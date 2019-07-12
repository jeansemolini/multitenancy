package com.concept.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.concept.tenant.TenancyInterceptor;

//@SuppressWarnings("deprecation")
@Component
public class WebTenantConfig extends WebMvcConfigurationSupport{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TenancyInterceptor())
				.excludePathPatterns("/master/*");
	}
}
