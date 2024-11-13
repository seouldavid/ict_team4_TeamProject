package com.ict.healim.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailHandler {
	private JavaMailSender javaMailSender;
	private MimeMessage message;
	private MimeMessageHelper messageHelper;
	
	public EmailHandler (JavaMailSender javaMailSender) throws Exception {
		this.javaMailSender = javaMailSender;
		
		message = this.javaMailSender.createMimeMessage();
		messageHelper = new MimeMessageHelper(message, true, "UTF-8");
	}
	
	// ����
	public void setSubject(String subject) throws Exception{
		messageHelper.setSubject(subject);
	}
	// ����
	public void setText(String text) throws Exception{
		messageHelper.setText(text, true);
	}
	// ���� ����� �̸��ϰ� ����
	public void setForm(String email, String name) throws Exception{
		messageHelper.setFrom(email, name);
	}
	// �޴� �̸���
	public void setTO(String email) throws Exception{
		messageHelper.setTo(email);
	}
	// ������
	public void send() {
		javaMailSender.send(message);
	}
}
