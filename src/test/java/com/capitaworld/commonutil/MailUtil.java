package com.capitaworld.commonutil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sahil Patel
 */
public class MailUtil {

	private static final int DEFAULT_RETRY_COUNT = 3;
	private static final String MAIL_SENDER = "oplone@onlinepsbloans.com";
	private static final String MAIL_PASSWORD = "Welcome@opl";
	private static final String MAIL_HOST = "outlook.office365.com";	//"smtp.gmail.com";	//smtp-mail.outlook.com
	private static final String MAIL_PORT = "587";
	

	private static final Logger LOG = Logger.getLogger(MailUtil.class.getSimpleName());

	public static boolean sendMail(Email email, int retryCount) {
		if (retryCount <= 0) {
			retryCount = DEFAULT_RETRY_COUNT;
		}

		for (int i = 1; i <= retryCount; i++) {
			LOG.log(Level.INFO, "sending email...retryCount[{0}]", i);
			if (sendGenericMail(email)) {
				LOG.log(Level.INFO, "Mail has been sent successfully to {0}",
						email.getReceiver());
				return true;
			}
		}
		LOG.log(Level.INFO, "Mail sending failed to {0}.", email.getReceiver());
		return false;
	}

	public static boolean sendMail(Email email) {
		return sendMail(email, DEFAULT_RETRY_COUNT);
	}

	private static boolean sendGenericMail(final Email email) {
		try {
			Properties properties = getEmailProperty();

			Session session;

			if ("true".equals(properties.getProperty("mail.smtp.auth"))) {
				session = Session.getInstance(properties,
						new javax.mail.Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(MAIL_SENDER,
										MAIL_PASSWORD);
							}
						});
			} else {
				session = Session.getInstance(properties);
			}

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(MAIL_SENDER));
			message.setSubject(email.getSubject());

			for (String sReceiver : email.getReceiver()) {
				message.addRecipients(Message.RecipientType.TO,
						InternetAddress.parse(sReceiver));
			}

			if (email.getCc() != null && !email.getCc().isEmpty()) {
				for (String sCC : email.getCc()) {
					message.addRecipients(Message.RecipientType.CC,
							InternetAddress.parse(sCC));
				}
			}

			if (email.getBcc() != null && !email.getBcc().isEmpty()) {
				for (String sBCC : email.getBcc()) {
					message.addRecipients(Message.RecipientType.BCC,
							InternetAddress.parse(sBCC));
				}
			}

			Multipart multipart = new MimeMultipart();

			StringBuilder strContent = new StringBuilder(email.getContent());

			if (email.getFilePaths() != null
					&& !email.getFilePaths().isEmpty()) {
				for (String filePath : email.getFilePaths()) {
					MimeBodyPart messageAttachment = new MimeBodyPart();
					try {
				        DataSource source = new FileDataSource(filePath);
						messageAttachment.setDataHandler(new DataHandler(source));
						messageAttachment.setFileName(getFileNameFromPath(filePath));
						multipart.addBodyPart(messageAttachment);
					} catch (Exception fEx) {
						LOG.log(Level.SEVERE, "Error while attaching file : "+fEx.getMessage());
					}
				}
			}

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(strContent.toString(), "text/html");
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			Transport.send(message);
			return true;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Exception in MailUtil at :", e);
			return false;
		}
	}

	private static Properties getEmailProperty() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		//properties.put("mail.transport.protocol", "smtps");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", MAIL_HOST);
		properties.put("mail.smtp.port", MAIL_PORT);
		return properties;
	}
	
	private static String getFileNameFromPath(String path) {
	       try {
	           return path.substring(path.lastIndexOf("/") + 1, path.length());
	       } catch (Exception e) {
	           return "";
	       }
	   }

}
