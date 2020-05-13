package com.leyou.listener;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.config.SmsProperties;
import com.leyou.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author lizichen
 * @create 2020-04-24 1:12
 */
@Component
public class SmsListener {
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private SmsProperties smsProperties;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.sms.queue", durable = "true"),
            exchange = @Exchange(value = "leyou.sms.exchange",
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    /**
     * 监听短信MQ
     * @param msg
     * @throws ClientException
     */
   public void sendSms(Map<String ,String > msg) throws ClientException {
       if (CollectionUtils.isEmpty(msg)){
           return ;
       }
       String phone=msg.get("phone");
       String code=msg.get("code");
       if (StringUtils.isNoneBlank(phone)&&StringUtils.isNoneBlank(code)){
           this.smsUtils.sendSms(phone,code,this.smsProperties.getSignName() ,this.smsProperties.getVerifyCodeTemplate());
       }
   }
}
