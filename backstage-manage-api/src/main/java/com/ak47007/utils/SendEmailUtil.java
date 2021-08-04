package com.ak47007.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 发送邮箱
 *
 * @author AK47007
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "email")
public class SendEmailUtil {

    /**
     * 发送邮箱
     */
    private String formEmail;

    /**
     * 发送人用户名
     */
    private String sendUserName;

    /**
     * 邮箱授权密码
     */
    private String password;

    /**
     * smpt服务
     */
    private String smtp;


    /**
     * 发送邮件
     *
     * @param toMail      接收人邮箱（多人，英文逗号分隔）
     * @param ccMail      抄送人邮箱（多人，英文逗号分隔）
     * @param mailTitle   邮件标题
     * @param mailContent 邮件内容
     * @throws Exception 异常
     */
    public void sendMail(String toMail, String ccMail, String mailTitle, String mailContent) throws Exception {
        try {
            // 使用smtp：简单邮件传输协议
            // 可以加载一个配置文件
            Properties props = new Properties();
            // 存储发送邮件服务器的信息
            props.put("mail.smtp.host",smtp);
            // 同时通过验证
            props.put("mail.smtp.auth", "true");
            // 根据属性新建一个邮件会话
            Session session = Session.getInstance(props);
            // 由邮件会话新建一个消息对象
            MimeMessage message = new MimeMessage(session);
            // 设置发件人的地址
            message.setFrom(new InternetAddress(formEmail));
            // 设置收件人,并设置其接收类型为TO
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
            // 设置抄送人，并设置其接收类型为CC
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccMail));
            // 设置标题
            message.setSubject(mailTitle);
            // 设置信件内容，发送HTML邮件，内容样式比较丰富
            message.setContent(mailContent, "text/html;charset=gbk");
            // 设置发信时间
            message.setSentDate(new Date());
            // 存储邮件信息
            message.saveChanges();

            // 发送邮件
            Transport transport = session.getTransport();
            transport.connect(sendUserName, password);
            // 发送邮件,其中第二个参数是所有已设好的收件人地址
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception ex) {
            log.error("邮件发送异常：{}", ex);
        }
    }
}
