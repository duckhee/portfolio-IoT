package kr.co.won.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
// async config setting default is make new thread and deal
@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        /** 자바에 쓰레드풀 익스큐터를 활용해서 비동기 처리하는 것 */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        /** 기본 값 설정 하는것 - 프로세서의 갯수를 가져온다. */
        int processors = Runtime.getRuntime().availableProcessors();
        /** logging */
        log.info("processor count {}", processors);

        /** CPU와 하는 작업에 따라 결정하는 게 좋다. */
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors * 2);
        /** 메모리에 따라서 적절히 설정 하기 */
        executor.setQueueCapacity(50);
        /** 쓰레드를 더 만든 것을 정리하기 위한 시간을 설정을 해주는 것 */
        executor.setKeepAliveSeconds(60);
        /** 쓰레드 생성되는 이름 설정 */
        executor.setThreadNamePrefix("AsyncExecutor-");
        /** 쓰레드 풀 초기화 진행 해주는 것 */
        executor.initialize();
        return executor;
    }

    /**
     * 비동기에서 생성된 스레트에서 예외 처리하기 위한 핸들러 등록하는 것
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
