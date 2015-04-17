package co.uk.jpmorgan.lib;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class MailSender
{
final String senderEmailID = "itsarunp4u@gmail.com";
final String senderPassword = "aravindanusha143";
final String emailSMTPserver = "smtp.gmail.com";
final String emailServerPort = "465";

String receiverEmailID = null;
String emailSubject = null;
String emailBody = null;
Calendar c = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy kk:mm");
public MailSender(String receiverEmailID, String emailSubject, String emailBody)
{
this.receiverEmailID=receiverEmailID;
this.emailSubject=emailSubject;
this.emailBody=emailBody;


Properties props = new Properties();
props.put("mail.smtp.user",senderEmailID);
props.put("mail.smtp.host", emailSMTPserver);
props.put("mail.smtp.port", emailServerPort);
props.put("mail.smtp.starttls.enable", "true");
props.put("mail.smtp.auth", "true");
// props.put("mail.smtp.debug", "true");
props.put("mail.smtp.socketFactory.port", emailServerPort);
props.put("mail.smtp.socketFactory.class",
"javax.net.ssl.SSLSocketFactory");
props.put("mail.smtp.socketFactory.fallback", "false");

SecurityManager security = System.getSecurityManager();

try
{
Authenticator auth = new SMTPAuthenticator();
Session session = Session.getInstance(props, auth);
// session.setDebug(true);

MimeMessage msg = new MimeMessage(session);
msg.setText(emailBody);
msg.setSubject(emailSubject);
String filename = "C:\\Users\\jvk\\workspace\\jpmorgan\\target\\qa-logs\\JpMorgan_Test_Results.html";


DataSource source = new FileDataSource(filename);
msg.setDataHandler(new DataHandler(source));
msg.setFrom(new InternetAddress(senderEmailID));
msg.addRecipient(Message.RecipientType.TO,
new InternetAddress(receiverEmailID));
Transport.send(msg);
}
catch (Exception mex)
{
mex.printStackTrace();
}


}
public class SMTPAuthenticator extends javax.mail.Authenticator
{
public PasswordAuthentication getPasswordAuthentication()
{
return new PasswordAuthentication(senderEmailID, senderPassword);
}
}

public static void main(String[] args)
{
MailSender mailSender=new MailSender("itsarunp4u@gmail.com","Selenium Webdriver Execution Report","Hi this is a test mail");
}

}