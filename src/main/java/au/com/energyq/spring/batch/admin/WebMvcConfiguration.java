package au.com.energyq.spring.batch.admin;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Bean
	public FilterRegistrationBean hiddenHttpMethodFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean(new HiddenHttpMethodFilter());
		filterRegBean.setUrlPatterns(Arrays.asList("/*"));
		return filterRegBean;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/jobs/executions");
	}

}
