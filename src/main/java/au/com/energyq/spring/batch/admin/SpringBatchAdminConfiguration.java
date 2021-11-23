package au.com.energyq.spring.batch.admin;

import javax.sql.DataSource;

import org.springframework.batch.admin.service.SimpleJobServiceFactoryBean;
import org.springframework.batch.admin.web.JobController;
import org.springframework.batch.admin.web.JobExecutionController;
import org.springframework.batch.admin.web.StepExecutionController;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Import({ FreeMarkerConfiguration.class, WebMvcConfiguration.class })
public class SpringBatchAdminConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private PlatformTransactionManager transactionManager;

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
    public TaskExecutor batchAdminTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(6);
        executor.setCorePoolSize(6);
        executor.setThreadGroupName("spring-batch-admin");
        executor.setThreadNamePrefix("spring-batch-admin");
        executor.initialize();
        return executor;
    }

    @Bean
    public JobLauncher batchAdminjobLauncher() {
        final SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(batchAdminTaskExecutor());
        return simpleJobLauncher;
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    @Bean
    public SimpleJobServiceFactoryBean jobService() {
        SimpleJobServiceFactoryBean simpleJobServiceFactory = new SimpleJobServiceFactoryBean();
        simpleJobServiceFactory.setJobRepository(jobRepository);
        simpleJobServiceFactory.setJobLauncher(batchAdminjobLauncher());
        simpleJobServiceFactory.setJobLocator(jobRegistry);
        simpleJobServiceFactory.setDataSource(dataSource);
        simpleJobServiceFactory.setJobExplorer(jobExplorer);
        simpleJobServiceFactory.setTransactionManager(transactionManager);
        return simpleJobServiceFactory;
    }
}
