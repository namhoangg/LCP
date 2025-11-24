package com.lcp.util;

import com.lcp.common.dto.EmailBaseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@AllArgsConstructor
public class SmtpEmailService implements EmailService {

    @Override
    public void send(EmailBaseDto dto) {
        Map<String, String> emailConfig = new HashMap<>();
        emailConfig.put("host", "smtp.gmail.com");
        emailConfig.put("port", "465");
        emailConfig.put("username", "noreply.freightflex@gmail.com");
        emailConfig.put("password", "utck wxru gmxm tzyi");
        // send email
        MimeMessagePreparator preparator = mimeMessage -> {
            // set email properties
            mimeMessage.setFrom(new InternetAddress(emailConfig.get("username")));
            mimeMessage.setSubject(dto.getSubject());
            // set email body
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setText(dto.getBody(), true);
            if (CollectionUtils.isNotEmpty(dto.getToEmails())) {
                dto.getToEmails().forEach(toEmail -> {
                    try {
                        helper.addTo(toEmail, toEmail);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
                log.info("Send to - " + dto.getToEmails());
            }

            if (CollectionUtils.isNotEmpty(dto.getBccEmails())) {
                dto.getBccEmails().forEach(bccEmail -> {
                    try {
                        helper.addBcc(bccEmail, bccEmail);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
                log.info("Send to - " + dto.getBccEmails());
            }

            if (CollectionUtils.isNotEmpty(dto.getAttachments())) {
                dto.getAttachments().forEach(attachment -> {
                    try {
                        InputStream inputStream = new ByteArrayInputStream(attachment.getData());
                        ByteArrayDataSource dataSource = new ByteArrayDataSource(inputStream, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                        helper.addAttachment(attachment.getFileName(), dataSource);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
            }
        };

        createMailSender(emailConfig).send(preparator);
    }

    protected JavaMailSenderImpl createMailSender(Map<String, String> emailConfig) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.get("host"));
        mailSender.setPort(parsePort(emailConfig.get("port")));
        mailSender.setUsername(emailConfig.get("username"));
        mailSender.setPassword(emailConfig.get("password"));
        mailSender.setJavaMailProperties(createJavaMailProperties(emailConfig));
        return mailSender;
    }

    private Properties createJavaMailProperties(Map<String, String> emailConfig) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.checkserveridentity", "true");
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.socketFactory.port", emailConfig.get("port"));
        return properties;
    }

    private int parsePort(String strPort) {
        try {
            return Integer.valueOf(strPort);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return 587;
    }
}
