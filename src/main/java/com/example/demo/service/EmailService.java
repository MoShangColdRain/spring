package com.example.demo.service;

public interface EmailService {

	void sendSimpleEmail(String to, String subject, String content);
	
    void sendHtmlEmail(String to, String subject, String content);
    
	void sendInlineResourceEmail(String to, String subject, String content, String rscPath, String rscId);


}
