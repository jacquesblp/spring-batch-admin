package au.com.cyberavenue.spring.batch.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.batch.admin.web.ExecutionsMenu;
import org.springframework.batch.admin.web.JobsMenu;
import org.springframework.batch.admin.web.resources.DefaultResourceService;
import org.springframework.batch.admin.web.resources.MenuManager;
import org.springframework.batch.admin.web.resources.ResourceService;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

@Configuration
public class FreeMarkerConfiguration {

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer(FreeMarkerProperties properties) {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPaths(properties.getTemplateLoaderPath());
		configurer.setPreferFileSystemAccess(false);
		configurer.setDefaultEncoding(properties.getCharsetName());
		Map<String, Object> freeMarkerVariables = new HashMap<>();
		freeMarkerVariables.put("menuManager", menuManager());
		configurer.setFreemarkerVariables(freeMarkerVariables);
		Properties settings = new Properties();
		settings.put("recognize_standard_file_extensions", "true");
		settings.put("default_encoding", "UTF-8");
		settings.putAll(properties.getSettings());
		configurer.setFreemarkerSettings(settings);
		return configurer;
	}

	@Bean
	public ViewResolver freeMarkerViewResolver() {
		BeanNameViewResolver vr = new BeanNameViewResolver();
		vr.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return vr;
	}

	@Bean
	public MenuManager menuManager() {
		return new MenuManager();
	}

	@Bean
	public ResourceService resourceService() {
		return new DefaultResourceService();
	}

	@Bean
	public JobsMenu jobsMenu() {
		JobsMenu jobsMenu = new JobsMenu();
		jobsMenu.setPrefix(resourceService().getServletPath());
		return jobsMenu;
	}

	@Bean
	public ExecutionsMenu executionsMenu() {
		ExecutionsMenu executionsMenu = new ExecutionsMenu();
		executionsMenu.setPrefix(resourceService().getServletPath());
		return executionsMenu;
	}

	private FreeMarkerView parentFreeMarkerView() {
		FreeMarkerView fmv = new FreeMarkerView();
		fmv.setAllowRequestOverride(false);
		Properties p = new Properties();
		p.put("titleCode", "home.title");
		p.put("titleText", "Spring Batch Admin");
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("titleCode", "home.title");
		attributes.put("titleText", "Spring Batch Admin");
		return fmv;
	}

	private FreeMarkerView standard() {
		FreeMarkerView fmv = parentFreeMarkerView();
		fmv.setUrl("/layouts/html/standard.ftl");
		fmv.setContentType("text/html;charset=UTF-8");
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/layouts/html/home.ftl");
		attributes.put("servletPath", resourceService().getServletPath());
		return fmv;
	}

	@Bean("jobs")
	public FreeMarkerView jobsView() {
		FreeMarkerView fmv = standard();
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/manager/jobs/html/jobs.ftl");
		attributes.put("titleCode", "jobs.title");
		attributes.put("titleText", "Spring Batch Admin: Jobs");
		return fmv;
	}

	@Bean("jobs/job")
	public FreeMarkerView jobDetailView() {
		FreeMarkerView fmv = standard();
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/manager/jobs/html/job.ftl");
		attributes.put("titleCode", "jobs.title");
		attributes.put("titleText", "Spring Batch Admin: Job Summary");
		return fmv;
	}

	@Bean("jobs/executions")
	public FreeMarkerView jobExecutionsView() {
		FreeMarkerView fmv = standard();
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/manager/jobs/html/executions.ftl");
		attributes.put("feedPath", "/jobs/executions.rss");
		attributes.put("titleCode", "job.executions.title");
		attributes.put("titleText", "Spring Batch Admin: Job Executions");
		return fmv;
	}

	@Bean("jobs/execution")
	public FreeMarkerView jobExecutionView() {
		FreeMarkerView fmv = standard();
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/manager/jobs/html/execution.ftl");
		attributes.put("titleCode", "job.execution.title");
		attributes.put("titleText", "Spring Batch Admin: Job Execution");
		return fmv;
	}

	@Bean("jobs/executions/steps")
	public FreeMarkerView jobExecutionsStepsView() {
		FreeMarkerView fmv = standard();
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/manager/steps/html/executions.ftl");
		attributes.put("titleCode", "step.executions.title");
		attributes.put("titleText", "Spring Batch Admin: Step Executions");
		return fmv;
	}

	@Bean("jobs/executions/step")
	public FreeMarkerView jobExecutionsStepView() {
		FreeMarkerView fmv = standard();
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/manager/steps/html/execution.ftl");
		attributes.put("titleCode", "step.execution.title");
		attributes.put("titleText", "Spring Batch Admin: Step Execution Summary");
		return fmv;
	}

	@Bean("jobs/executions/step/history")
	public FreeMarkerView jobExecutionsStepHistoryView() {
		FreeMarkerView fmv = standard();
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/manager/steps/html/history.ftl");
		attributes.put("titleCode", "step.history.title");
		attributes.put("titleText", "Spring Batch Admin: Step Execution History");
		return fmv;
	}

	@Bean("jobs/executions/step/progress")
	public FreeMarkerView jobExecutionsStepProgressView() {
		FreeMarkerView fmv = standard();
		Map<String, Object> attributes = fmv.getAttributesMap();
		attributes.put("body", "/manager/steps/html/progress.ftl");
		attributes.put("history", "/manager/steps/html/history.ftl");
		attributes.put("execution", "/manager/steps/html/execution.ftl");
		attributes.put("titleCode", "step.progress.title");
		attributes.put("titleText", "Spring Batch Admin: Step Execution Progress");
		return fmv;
	}
}
