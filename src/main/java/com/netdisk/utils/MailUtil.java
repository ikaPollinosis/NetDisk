package com.netdisk.utils;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @ClassName: MailUtil
 * @Description: 邮件发送类
 * @Date: 2022/4/27 22:58
 */
public class MailUtil {

    // 设置邮件发送器
    private JavaMailSenderImpl mailSender;

    // 设置日志
    Logger logger = LoggerFactory.getLogger(MailUtil.class);

    public MailUtil(JavaMailSenderImpl mailSender){
        this.mailSender = mailSender;
    }

    public void sendComplexMail(String subject,String text,String recevier){
        logger.info("Start sending complex mail...");
        logger.info("mailSender: "+mailSender);
        MimeMessage mimemessage=mailSender.createMimeMessage();
        MimeMessageHelper mimehelper=new MimeMessageHelper(mimemessage);
        try{
            mimehelper.setSubject(subject); // 设置主题
            mimehelper.setText(text,true);   // 设置正文
            mimehelper.setFrom("287720499@qq.com"); // 设置发送人
            mimehelper.setTo(recevier);
        }catch(MessagingException e){
            e.printStackTrace();
        }
        logger.info("mimeMessage: "+mimemessage);
        mailSender.send(mimemessage);
    }

    public String sendCaptcha(String email,String userName,String password){
        int captcha = (int) ((Math.random() * 9 + 1) * 100000); // 生成六位随机数作为验证码
        logger.info("Start sending complex mail...");
        logger.info("mailSender: "+mailSender);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("邮箱验证");
            helper.setText("<h3>邮箱验证<h3/>" +
                    "用户名: "+userName+
                    "您的验证码为: <span style='color : red'>"+captcha+"</span><br>" +
                    "<hr>",true);
            helper.setFrom("287720499@qq.com");
            helper.setTo(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        logger.info("mimeMessage: "+mimeMessage);
        mailSender.send(mimeMessage);
        return String.valueOf(captcha);
    }


}
