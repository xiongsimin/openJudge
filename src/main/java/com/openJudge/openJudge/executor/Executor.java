package com.openJudge.openJudge.executor;

import com.openJudge.openJudge.entity.ExecutorResult;

/**
 * @Author aries
 * @Data 2021-02-15
 */
public interface Executor {
    /**
     * 进程执行器执行
     *
     * @return
     */
    ExecutorResult service();

    /**
     * 返回执行结果
     *
     * @return
     */
    ExecutorResult getExecutorResult();
}
