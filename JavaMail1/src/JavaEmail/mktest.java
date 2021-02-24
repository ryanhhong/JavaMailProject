package JavaEmail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class mktest {
	public static void main(String[] args) {

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", "zkm7184@gmail.com", "ThePassword!");
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message message[] = inbox.getMessages();
            for(int i = message.length-1; i>= 0;i--)
            {
            	Message msg = message[i];
            	String subject = msg.getSubject();
            	System.out.println((i+1)+") "+subject);
            	
            }
           
            //Multipart mp = (Multipart) msg.getContent();
           // BodyPart bp = mp.getBodyPart(0);
            //createPdf(msg.getSubject(), (String) bp.getContent());
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
	
	 public static void createPdf(String filename, String body)
	            throws DocumentException, IOException {

	        Document document = new Document();
	        PdfWriter.getInstance(document, new FileOutputStream(filename + ".pdf"));
	        document.open();
	        document.add(new Paragraph(body));
	        document.close();
	    }

}
