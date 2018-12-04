package com.example.demo.service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

public interface ShortMessageService {

	SendSmsResponse sendSms();
	
    QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException;

}
