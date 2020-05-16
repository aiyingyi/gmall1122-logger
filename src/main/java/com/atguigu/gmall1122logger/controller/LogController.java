package com.atguigu.gmall1122logger.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    @RestController:相当于@Controller + @ResponseBody，不会将请求方法的返回值解析成页面返回
    @ResponseBody：将请求方法的返回值当做字符串返回
 */
@RestController
@Slf4j
public class LogController {
    @Autowired
    KafkaTemplate  kafkaTemplate;
    // @RequestBody:从请求体中获取参数
    @RequestMapping("/applog")
    public String applog(@RequestBody String logString){
        // 对日志进行输出
        log.info(logString);
        // json解析，判断日志类型，放入kafka
        JSONObject jsonObject = JSON.parseObject(logString);
        if(jsonObject.getString("start")!=null && jsonObject.getString("start").length()>0 ){

            kafkaTemplate.send("GMALL_START",logString);
        }else{
            kafkaTemplate.send("GMALL_EVENT",logString);
        }
        return "success";
    }
}
