package com.credentials.emailms.service;

import com.credentials.emailms.model.EmailMessage;
import com.credentials.emailms.model.EmailTemplate;
import com.credentials.emailms.model.TemplateRequest;
import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

@Service("EmailSdk")
public class EmailServiceImpl implements EmailService {
    @Autowired
    RestTemplate template;
    @Value("${aws.us-east-1.endpoint}")
    private String nVirginiaRegionEndpoint;
    @Value("${aws.sendmail.resource}")
    private String emailResourceUrl;

    /**
     * Send Email using AWS SDK -- Deployment in EC2 required
     */
    @Override
    public ResponseEntity<String> sendEmail(String recipient) throws IOException {
        String sender = "";
        String subject = "";
        EmailTemplate emailTemplate = retrieveEmailTemplate("").getBody();
        retrieveDataFromSources();
        Region region = Region.US_EAST_1;
        SesClient client = SesClient.builder().region(region).build();

        // The email body for non-HTML email clients
        String bodyText = "Hello,\r\n" + "See the list of customers. ";

        // The HTML body of the email
        String bodyHTML = "<html>" + "<head></head>" + "<body>" + "<h1>Hello!</h1>"
                + "<p> See the list of customers.</p>" + "</body>" + "</html>";

        try {
            send(client, sender, recipient, subject, bodyText, bodyHTML);
            client.close();
            System.out.println("Done");
        } catch (IOException | MessagingException e) {
            e.getStackTrace();
        }

        return null;
    }

    /**
     * Send Email using SES API
     */
    public ResponseEntity<String> sendEmail_sesApi(String recipient, EmailMessage emailMessage) throws IOException {
        /*SES send email API Call*/
        ResponseEntity<String> emailStatus = template.exchange(nVirginiaRegionEndpoint + emailResourceUrl,
                HttpMethod.POST, buildEmailMessage(emailMessage), String.class);
        return null;
    }

    /*Admin call*/
    @Override
    public void createEmailTemplate(TemplateRequest templateRequest) {
        String response = template.postForObject("/v2/email/templates", getRequestEntity(templateRequest), String.class);
    }

    @Override
    public ResponseEntity<EmailTemplate> retrieveEmailTemplate(String templateName) {
        EmailTemplate emailTemplate = template.getForObject("/v2/email/templates/" + templateName, EmailTemplate.class);
        return new ResponseEntity<EmailTemplate>(emailTemplate, HttpStatus.OK);
    }

    private HttpEntity<?> getRequestEntity(TemplateRequest templateRequest) {
        Headers headers = new Headers();
        return new HttpEntity<TemplateRequest>(templateRequest);

    }

    private HttpEntity<?> buildEmailMessage(EmailMessage emailMessage) {
        return new HttpEntity<>(emailMessage);
    }

    private void retrieveDataFromSources() {
    }

    private void send(SesClient client,
                      String sender,
                      String recipient,
                      String subject,
                      String bodyText,
                      String bodyHTML) throws MessagingException, IOException {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);

        //Add Subject
        message.setSubject(subject, "UTF-8");
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

        // Create a multipart/alternative child container
        MimeMultipart msgBody = new MimeMultipart("alternative");

        // Create a wrapper for the HTML and text parts
        MimeBodyPart wrap = new MimeBodyPart();

        // Define the text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(bodyText, "text/plain; charset=UTF-8");

        //Define HTML part
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(bodyHTML, "text/html; charset=UTF-8");

        // Add the text and HTML parts to the child container
        msgBody.addBodyPart(textPart);
        msgBody.addBodyPart(htmlPart);

        // Add the child container to the wrapper object
        wrap.setContent(msgBody);

        // Create a multipart/mixed parent container
        MimeMultipart msg = new MimeMultipart("mixed");

        // Add the parent container to the message
        message.setContent(msg);

        // Add the multipart/alternative part to the message
        msg.addBodyPart(wrap);

        try {
            System.out.println("Attempting to send an email through Amazon SES " + "using the AWS SDK for Java...");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());

            byte[] arr = new byte[byteBuffer.remaining()];
            byteBuffer.get(arr);

            SdkBytes data = SdkBytes.fromByteArray(arr);
            RawMessage rawMessage = RawMessage.builder().data(data).build();
            SendRawEmailRequest rawEmailRequest = SendRawEmailRequest.builder().rawMessage(rawMessage).build();
            client.sendRawEmail(rawEmailRequest);



        } catch (SesException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

}
