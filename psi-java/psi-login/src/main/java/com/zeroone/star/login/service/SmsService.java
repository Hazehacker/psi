package com.zeroone.star.login.service;

import com.zeroone.star.project.components.sms.aliyun.SmsResult;

public interface SmsService {

    public SmsResult sendMessage(String phone);
}
