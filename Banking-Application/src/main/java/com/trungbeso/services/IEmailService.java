package com.trungbeso.services;

import com.trungbeso.dtos.EmailDetails;

public interface IEmailService {
	void sendEmailAlert(EmailDetails emailDetails);
	void sendEmailWithAttachment(EmailDetails emailDetails);
}
