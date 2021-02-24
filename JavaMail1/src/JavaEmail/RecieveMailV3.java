package JavaEmail;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class RecieveMailV3 {

	public static void AutomatedMail(String username, String password)
	{
		//while(true){
		try{
				Properties properties = new Properties();
				properties.setProperty("mail.store.protocol", "imaps");
			Session emailSession = Session.getDefaultInstance(properties);
			Store emailStore = emailSession.getStore();
				//(host, username, password)
				emailStore.connect("imap.gmail.com",username,password); // change the gmail to the correct email.
				// get inbox folder
				Folder emailFolder = emailStore.getFolder("INBOX");
				emailFolder.open(Folder.READ_WRITE);
				Message[] messages = emailFolder.getMessages();
				
				for(int i = 0;i<messages.length;i++) 
				{
					//Message[] message[i] = messages[i];
					//String content= messages[i].getContent().toString();
					
					if(messages[i].isSet(Flags.Flag.SEEN))
					{
						System.out.println(i);
					}
					
					if(messages[i].getSubject().equals("NEW EMPLOYEE") ||messages[i].getSubject().equals("New Employee"))
					{
						Message msg = emailFolder.getMessage(i+1);
			            Multipart mp = (Multipart) msg.getContent();
			            BodyPart bp = mp.getBodyPart(0);
			            
			            //gets the from email from the <> brackets
			           String s = msg.getFrom()[0].toString();
			           String emailAddress = s.substring(s.indexOf("<")+1, s.indexOf(">"));// prints what is in between the two <>
			           System.out.println(emailAddress);
			            //gets the from email from the <> brackets
			          //String Name = s.substring(0, s.indexOf("<"));
			            String en = (String) bp.getContent();
			            String employeeNumber = en.substring(s.indexOf("EMPLOYEE: ")+1, en.lastIndexOf("Employee: ")+16);
			            final String newEn = stripNonDigits(employeeNumber);
			            System.out.println("Employee Number: " + newEn);
			          
			            
			            requestPdf("Employee_Request_"+ newEn, (String) bp.getContent(),emailAddress);
			           // System.out.println(employeeNumber);
				         
				         //System.out.print(result.toString());			// gets message
				         
						//System.out.println("New Employee: "+ message.getFrom()[0]);			// who the email is from
						//System.out.println("Text: " + result.toString());					// prints the message
						
						
						
						
						
						// SMTP info
				        String host = "smtp.gmail.com";
				        String port = "465";
				        String mailFrom = "zkm7184@gmail.com";				//username
				        String passwords = "ThePassword!";						//password
				 
				        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");// EE is for the day of the week.EEEE is for full day name
				        Calendar today = Calendar.getInstance();
				        today.set(Calendar.HOUR_OF_DAY, 0);
				        String theDate = dateFormat.format(today.getTime()).toString();
				        //System.out.println(theDate);	//used for testing output
				        String newDate = null ;
				        if(theDate.contains("Thursday") || theDate.contains("Wednesday") || theDate.contains("Friday"))
				        {
				        
				        	today.add(Calendar.DAY_OF_YEAR, 5);
				        	newDate = dateFormat.format(today.getTime()).toString();   
					       // System.out.println(newDate); //used for testing output
					        
				        }
				        else if(theDate.contains("Saturday"))
				        {
				        	today.add(Calendar.DAY_OF_YEAR, 4);
				        	newDate = dateFormat.format(today.getTime()).toString();
				        	//System.out.println(newDate);	// used for testing output
				        }
				        
				        else{
				        	today.add(Calendar.DAY_OF_YEAR, 3);
				        	newDate = dateFormat.format(today.getTime()).toString();
				        	//System.out.println(newDate);	// used for testing output
				        }
				       
				        
				        // message info
				        String mailTo = emailAddress;
				        String subject = "New Employee Laptop Request Confirmation Email DO_NOT_REPLY";
				        String Subject2 = "New Employee Laptop Request";
				        String Estimated = ("Your request has been sent to ryan.hong@sjsu.edu. Estimated Delivery Date is: "+ newDate+".");
				        String messagess = Estimated;
				        String messagesss = "" ;
				        String Messages1 = "Employee notified that request has been sent on: " +theDate+". The Employee is also notified that the estimated Delivery date is: "+newDate;
				        String Messages2 = "EMPLOYEE: ";
				        // attachments
				        String[] attachFiles = new String[1];
				        attachFiles[0] = "C:/Users/Ryan Hong/New folder/JavaMail1/"+"Employee_Request_"+ newEn +".pdf"; //location of pdf
				        try{
				        sendEmailWithAttachments(host, port, mailFrom, passwords, mailTo,
				                subject, messagess,messagesss, attachFiles);
				        sendEmailWithAttachments2(host, port, mailFrom, passwords, "ryan.hong@sjsu.edu",
				                Subject2, Messages1,Messages2, attachFiles);
				        }catch(Exception ex)
				        {
				        	ex.printStackTrace();
				        }
				       // message.setFlag(Flags.Flag.DELETED, true); this is used for deleting mail
					} // end of "New Employee" if statement
					// need to find a way to detect a reply not a reagular message. Attempt to search for reply messaged or messages with re: in the front of them.
					if(messages[i].getSubject().equals("Re: NEW EMPLOYEE LAPTOP REQUEST") ||messages[i].getSubject().equals("Re: New Employee Laptop Request"))
							{
						Message msg = emailFolder.getMessage(i+1);
			            Multipart mp = (Multipart) msg.getContent();
			            BodyPart bp = mp.getBodyPart(0);
						
			            String s = msg.getFrom()[0].toString();
			            
			            String en = (String) bp.getContent();
			            String employeeNumber = en.substring(s.indexOf("EMPLOYEE: ")+10, en.indexOf("EMPLOYEE: ")+16);
			        
			            String dd = (String) bp.getContent();
			            String deliveryDate = dd.substring(s.indexOf("DELIVERY DATE: ")+32, dd.indexOf("DELIVERY DATE: ")+27);
			            final String newDelivery = addPeriods(stripNonDigits(deliveryDate));
			           
			            //System.out.println(newDelivery);
			            final String newEn = stripNonDigits(employeeNumber);
			            
			            System.out.println("Asset PDF Name: " + newEn +"-"+newDelivery);
			            String assetPDFname = newEn+"-"+newDelivery;
			            AssetPdf(assetPDFname,(String) bp.getContent(), assetPDFname);
			            // make asset pdf for accounting and send to accounting
			            // asset pdf must be named "employee number + delivery date" i.e. 95131-20170515
			            String host = "smtp.gmail.com";
				        String port = "465";
				        String mailFrom = "zkm7184@gmail.com";				//username
				        String passwords = "ThePassword!";
				        String mailTo = "ryanhhong@outlook.com";
				        String subject = "Employee laptop request asset pdf";
				        String messagess = "Asset PDF for accounting";
				        String[] attachFiles = new String[1];
				        attachFiles[0]= "C:/Users/Ryan Hong/New folder/JavaMail1/"+assetPDFname+".pdf ";
				        sendEmailWithAttachments(host, port, mailFrom, passwords, mailTo, subject,messagess,"",attachFiles);
				       //  message.setFlag(Flags.Flag.DELETED, true); this was used for deleting mail
							}
				}
					//emailFolder.close(true); this was used for deleting mails
					//emailStore.close();
					
				}catch (Exception mex) {
					mex.printStackTrace();
					
				}
				
	}
	//}
	
	public static String addPeriods(String addPeriods)
	{
		final StringBuilder sb = new StringBuilder(addPeriods.length());
		for(int i = 0; i < addPeriods.length(); i++)
		{
			final char c = addPeriods.charAt(i);
			if(i != 2 && i != 5)
			{
				sb.append(c);
			}
			else
			{
				sb.append(c);
				sb.insert(i, ".");
			}
		}
		return sb.toString();
	}
	public static String stripNonDigits(String getDigits)
	{
		final StringBuilder sb = new StringBuilder(getDigits.length());
		for(int i = 0; i < getDigits.length(); i++)
		{
			final char c = getDigits.charAt(i);
			if(c>47 && c<58)
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static void requestPdf(String filename, String body, String email) throws DocumentException, IOException {

		Document request = new Document();
		PdfWriter.getInstance(request, new FileOutputStream(filename + ".pdf"));
		request.open();
		request.add(new Paragraph("NEW EMPLOYEE COMPUTER REQUEST"));
		request.add(new Paragraph("Employee Email: <" + email + ">"));
		request.add(new Paragraph(" "));
		request.add(new Paragraph(body));
		request.add(new Paragraph("Status:"));
		request.add(new Paragraph("Delivery Date:"));
		request.close();
	}
	public static void AssetPdf(String filename, String body, String assetPDF) throws DocumentException, IOException {

		Document request = new Document();
		PdfWriter.getInstance(request, new FileOutputStream(filename + ".pdf"));
		request.open();
		request.add(new Paragraph("NEW EMPLOYEE COMPUTER REQUEST DELIVERED"));
		
		request.add(new Paragraph(" "));
		request.add(new Paragraph(body));
		request.add(new Paragraph("Asset Tag: "+ assetPDF));
		
		request.close();
	}

	public static void sendEmailWithAttachments(String host, String port, final String userName, final String password,
			String toAddress, String subject, String message, String message2, String[] attachFiles)
			throws AddressException, MessagingException {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);
		properties.put("mail.smtp.socketFactory.fallback", "false");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");
		// for gap (styling)
		MimeBodyPart gap = new MimeBodyPart();
		gap.setContent("", "text/html");
		// for gap (styling)
		MimeBodyPart messageBodyPart2 = new MimeBodyPart();
		messageBodyPart2.setContent(message2, "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		multipart.addBodyPart(gap);
		multipart.addBodyPart(messageBodyPart2);

		// adds attachments
		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);

	}
	public static void sendEmailWithAttachments2(String host, String port, final String userName, final String password,
			String toAddress, String subject, String message, String message2, String[] attachFiles)
			throws AddressException, MessagingException {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);
		properties.put("mail.smtp.socketFactory.fallback", "false");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");
		// for gap (styling)
		MimeBodyPart gap = new MimeBodyPart();
		gap.setContent("", "text/html");
		// for gap (styling)
		MimeBodyPart messageBodyPart2 = new MimeBodyPart();
		messageBodyPart2.setContent("EMPLOYEE NAME: ", "text/html");
		MimeBodyPart messageBodyPart3 = new MimeBodyPart();
		messageBodyPart3.setContent("EMPLOYEE: ", "text/html");
		MimeBodyPart messageBodyPart4 = new MimeBodyPart();
		messageBodyPart4.setContent("DELIVERY DATE: ", "text/html");
		MimeBodyPart messageBodyPart5 = new MimeBodyPart();
		messageBodyPart5.setContent("START DATE: ", "text/html");
		MimeBodyPart messageBodyPart6 = new MimeBodyPart();
		messageBodyPart6.setContent("DEPARTMENT: ", "text/html");
		MimeBodyPart messageBodyPart7 = new MimeBodyPart();
		messageBodyPart7.setContent("JOB CLASSIFICATION: ", "text/html");
		MimeBodyPart messageBodyPart8 = new MimeBodyPart();
		messageBodyPart8.setContent("SUPERVISOR: ", "text/html");
		MimeBodyPart messageBodyPart9 = new MimeBodyPart();
		messageBodyPart9.setContent("COMPUTER: ", "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart2);
		multipart.addBodyPart(messageBodyPart3);
		multipart.addBodyPart(messageBodyPart4);
		multipart.addBodyPart(messageBodyPart5);
		multipart.addBodyPart(messageBodyPart6);
		multipart.addBodyPart(messageBodyPart7);
		multipart.addBodyPart(messageBodyPart8);
		multipart.addBodyPart(messageBodyPart9);
		multipart.addBodyPart(gap);
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);

	}

	public static void main(String args[]) {
		AutomatedMail("zkm7184@gmail.com", "ThePassword!");

	}
}



