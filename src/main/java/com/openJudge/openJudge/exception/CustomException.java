package com.openJudge.openJudge.exception;

/**
 * @Author aries
 * @Data 2021-02-18
 */
public class CustomException extends Exception {
    String getMsg() {
        return super.getMessage();
    }
}
