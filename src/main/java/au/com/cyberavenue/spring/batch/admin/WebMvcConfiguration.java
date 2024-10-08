package au.com.cyberavenue.spring.batch.admin;

import java.util.Arrays;

import org.springframework.batch.admin.web.JobController;
import org.springframework.batch.admin.web.JobExecutionController;
import org.springframework.batch.admin.web.StepExecutionController;
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
    public JobController jobController() {
        return new JobController();
    }

    @Bean
    public JobExecutionController jobExecutionController() {
        return new JobExecutionController();
    }

    @Bean
    public StepExecutionController stepExecutionController() {
        return new StepExecutionController();
    }

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
