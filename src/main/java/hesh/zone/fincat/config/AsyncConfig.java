package hesh.zone.fincat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")

    /**
     * taskExecutor
     * Spring already manages the threading for incoming http requests using the
     * servlet container's thread pool (e.g., Tomcat). Each request will be processed
     * by a separate thread, but if you want to control processing in your service,
     * you can still use @Async to execute a method asynchronously allowing it to
     * return immediately without blocking the main thread.
     */
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}
