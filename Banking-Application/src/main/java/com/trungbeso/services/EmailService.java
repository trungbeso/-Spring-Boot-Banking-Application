package com.trungbeso.services;

import com.trungbeso.dtos.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailService{

	JavaMailSender mailSender;

	@NonFinal
	@Value("${spring.mail.username}")
	private String senderEmail;

	@Override
	public void sendEmailAlert(EmailDetails emailDetails) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			mailMessage.setSubject(emailDetails.getSubject());
			mailMessage.setTo(emailDetails.getRecipient());
			mailMessage.setText(emailDetails.getMessageBody());

			mailSender.send(mailMessage);
			System.out.println("Mail sent successfully");
		} catch (MailException e) {
			throw new RuntimeException(e);
		}
	}

	//send with pdf file
	@Override
	public void sendEmailWithAttachment(EmailDetails emailDetails) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(senderEmail);
			helper.setTo(emailDetails.getRecipient());
			helper.setText(emailDetails.getMessageBody());
			helper.setSubject(emailDetails.getSubject());

			FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
			helper.addAttachment(file.getFilename(), file);
			mailSender.send(message);
			log.info("{} has been sent to user which have email {}", file.getFilename(), emailDetails.getRecipient());
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}


}
