package com.openJudge.openJudge.myTools;

import com.openJudge.openJudge.entity.ExecutorResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author aries
 * @Data 2021-02-15
 */
public class ProcessUtil {

    /**
     * 获取程序运行结果或错误信息
     *
     * @param process
     * @return map(" result " - result, " errorMsg " - errorMsg)
     */
    public static void getSystemOut(final Process process,ExecutorResult executorResult) {
        String result = read(process.getInputStream(), System.out).get("msg");
        String errorMsg = read(process.getErrorStream(), System.err).get("msg");
        executorResult.setResult(result);
        executorResult.setErrorMsg(errorMsg);
    }


    /**
     * 读取输入流并打印
     *
     * @param inputStream process.getInputStream()
     * @param out         System.out
     * @return map 返回一个键值对（"msg"-msg），存储运行结果，如果没有结果，msg为""
     */
    public static Map<String, String> read(InputStream inputStream, PrintStream out) {
        Map<String, String> map = new HashMap<String, String>();
        String msg = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("GBK")));
            String line;
            while ((line = reader.readLine()) != null && msg.length() < 200) {
                out.println(line);
                msg += line;
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("msg", msg);
        return map;
    }
}
