import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender
{
	private static final String SMTP_DEFAULT = "smtp.cs.umbc.edu";

	public static void send(EMail email)
	{
		send(email, SMTP_DEFAULT);
	}

	public static void send(EMail email, String smtpHost)
	{
		// create some properties and get the default Session
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.transfer.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.from", "milindp1@cs.umbc.edu");
		// props.put("mail.smtp.ssl.enable", "true");
		// props.put("mail.smtp.starttls.enable", "true");
		/*
		 * mail.transfer.protocol=smtp mail.smtp.host=mail.sjc2.webaroo.com mail.smtp.auth=false mail.smtp.from=payments@smsgupshup.com mail.mime.charset=UTF-8
		 * 
		 * 
		 * props.put("mail.from", "myemail@example.com"); props.put("mail.smtp.starttls.enable", "true"); // I tried this by itself and also together with ssl.enable)
		 * props.put("mail.smtp.ssl.enable", "true");
		 */

		/**
		 * 
		 */
		Session session = Session.getInstance(props, new javax.mail.Authenticator()
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication("milindp1", "Pyrqxgl12");
			}
		});
		session.setDebug(false);

		try
		{
			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(email.getFrom()));

			// set To
			InternetAddress[] address = new InternetAddress[email.getTo().length];
			for (int i = 0; i < email.getTo().length; ++i)
			{
				address[i] = new InternetAddress(email.getTo()[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, address);

			// set Cc
			address = new InternetAddress[email.getCc().length];
			for (int i = 0; i < email.getCc().length; ++i)
			{
				address[i] = new InternetAddress(email.getCc()[i]);
			}
			if (address.length > 0)
				msg.setRecipients(Message.RecipientType.CC, address);

			// set Bcc
			address = new InternetAddress[email.getBcc().length];
			for (int i = 0; i < email.getBcc().length; ++i)
			{
				address[i] = new InternetAddress(email.getBcc()[i]);
			}
			if (address.length > 0)
				msg.setRecipients(Message.RecipientType.BCC, address);

			msg.setSubject(email.getSubject());

			// create and fill the first message part
			MimeBodyPart mbpText = new MimeBodyPart();
			mbpText.setText(email.getMessage());

			// create the second message part
			List<MimeBodyPart> mbpAttachment = new ArrayList<MimeBodyPart>();
			if (email.getAttachment() != null)
			{
				for (EMailAttachment attachment : email.getAttachment())
				{
					MimeBodyPart mbp = new MimeBodyPart();
					mbp.attachFile(attachment.getFile());
					mbp.setFileName(attachment.getFilename());
					mbpAttachment.add(mbp);
				}
			}

			// create the Multipart and add its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbpText);
			for (MimeBodyPart mbp : mbpAttachment)
			{
				mp.addBodyPart(mbp);
			}

			// add the Multipart to the message
			msg.setContent(mp);

			// set the Date: header
			msg.setSentDate(new Date());

			// send the message
			Transport.send(msg);
		}
		catch (MessagingException mex)
		{
			System.out.println("EmailSender: Could not send mail: ");
			mex.printStackTrace();
			Exception ex = null;
			if ((ex = mex.getNextException()) != null)
			{
				System.out.println("EmailSender: Could not send mail nextExp: ");
				ex.printStackTrace();
			}
		}
		catch (Exception e)
		{
			System.out.println("EmailSender: Could not send mail: ");
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{

		String subject = "sending email via smtp,no auth";
		String message = "some message";
		EMail email = new EMail("milindp1@cs.umbc.edu", subject, message);
		email.setTo("milindp1@umbc.edu", "mailmaldi@gmail.com");
		EmailSender.send(email);
		System.out.println("Email Sent");
	}

	/**
	 * The server's address is smtp.cs.umbc.edu
	 * 
	 * You must set it up to use SSL and Password-based authentication. Your username and password are the same ones you use to log onto linuxserver1.cs.umbc.edu.
	 */
}

class EMail
{
	private String from;

	private String[] to;

	private String[] cc;

	private String[] bcc;

	private String subject;

	private String message;

	private EMailAttachment[] attachment;

	/**
	 * @param from
	 * @param subject
	 * @param message
	 */
	public EMail(String from, String subject, String message)
	{
		super();
		this.from = from;
		this.subject = subject;
		this.message = message;
	}

	/**
	 * @return the from
	 */
	public String getFrom()
	{
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from)
	{
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String[] getTo()
	{
		return to;
	}

	/**
	 * Enables chaining...
	 * 
	 * @param to
	 *            the to to set
	 */
	public EMail setTo(String... to)
	{
		this.to = to;
		return this;
	}

	/**
	 * @return the cc
	 */
	public String[] getCc()
	{
		return (cc == null) ? new String[0] : cc;
	}

	/**
	 * Enables chaining...
	 * 
	 * @param cc
	 *            the cc to set
	 */
	public EMail setCc(String... cc)
	{
		this.cc = cc;
		return this;
	}

	/**
	 * @return the bcc
	 */
	public String[] getBcc()
	{
		return (bcc == null) ? new String[0] : bcc;
	}

	/**
	 * Enables chaining...
	 * 
	 * @param bcc
	 *            the bcc to set
	 */
	public EMail setBcc(String... bcc)
	{
		this.bcc = bcc;
		return this;
	}

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @return the attachment
	 */
	public EMailAttachment[] getAttachment()
	{
		return attachment;
	}

	/**
	 * Enables chaining...
	 * 
	 * @param attachment
	 *            the attachment to set
	 */
	public EMail setAttachment(EMailAttachment... attachment)
	{
		this.attachment = attachment;
		return this;
	}
}

interface EMailAttachment
{
	public File getFile() throws IOException;

	public String getFilename();
}
