package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@RestController
public class MailController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${mail.fromMail.sender}")
    private String sender;

    @Value("${mail.fromMail.receiver}")
    private String receiver;

    @Autowired
    private JavaMailSender javaMailSender;

    /* *
     * @Description  http://localhost:8888/sendMail
     * @author haha
     * @email ********@qq.com
     * @method 发送文本邮件
     * @date
     * @param
     * @return
     */
    @RequestMapping("/sendMail")
    public String sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender); 	//发件人
        message.setTo(receiver);	//收件人	
        message.setSubject("大老杨");	//标题
        message.setText("你好你好你好！");	//邮件内容
        try {
            javaMailSender.send(message);
            logger.info("简单邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常！", e);
        }
        return "success";
    }

    /* *
     * @Description  http://localhost:8888/sendHtmlMail
     * @author haha
     * @email ********@qq.com
     * @method 发送html邮件
     * @date
     * @param
     * @return
     */
    @RequestMapping("/sendHtmlMail")
    public String testHtmlMail() {
        String content="<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封Html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject("html mail");
            helper.setText(content, true);

            javaMailSender.send(message);
            logger.info("html邮件发送成功");
        } catch (MessagingException e) {
            logger.error("发送html邮件时发生异常！", e);
        }
        return "success";
    }

    /* *
     * @Description http://localhost:8888/sendFilesMail
     * @author haha
     * @email ******@qq.com
     * @method 发送附件邮件
     * @date
     * @param
     * @return
     */
    @RequestMapping("/sendFilesMail")
    public String sendFilesMail() {
        String filePath="F:\\hell.groovy";
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject("附件邮件");
            helper.setText("这是一封带附件的邮件", true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);

            javaMailSender.send(message);
            logger.info("带附件的邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送带附件的邮件时发生异常！", e);
        }
        return "success";
    }

    /* *
     * @Description http://localhost:8888/sendInlineResourceMail
     * @author haha
     * @email ********@qq.com
     * @method 发送图片邮件
     * @date
     * @param
     * @return
     */
    @RequestMapping("/sendInlineResourceMail")
    public String sendInlineResourceMail() {
        String Id = "haha12138";
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + Id + "\' ></body></html>";
        String imgPath = "F:\\fengjing1.jpg";
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject("这是有图片的邮件");
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(imgPath));
            helper.addInline(Id, res);

            javaMailSender.send(message);
            logger.info("嵌入静态资源的邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送嵌入静态资源的邮件时发生异常！", e);
        }
        return "success";
    }
}
