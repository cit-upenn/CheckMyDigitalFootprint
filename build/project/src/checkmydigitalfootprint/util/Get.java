package checkmydigitalfootprint.util;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

// ...

public class Get {

  // ...


  /**
   * Get Message with given ID.
   *
   * @param service Authorized Gmail API instance.
   * @param userId User's email address. The special value "me"
   * can be used to indicate the authenticated user.
   * @param messageId ID of Message to retrieve.
   * @return Message Retrieved Message.
   * @throws IOException
   */
  public static Message getMessage(Gmail service, String userId, String messageId)
      throws IOException {
    Message message = service.users().messages().get(userId, messageId).execute();

    System.out.println("Message snippet: " + message.getSnippet());

    return message;
  }

  /**
   * Get a Message and use it to create a MimeMessage.
   *
   * @param service Authorized Gmail API instance.
   * @param userId User's email address. The special value "me"
   * can be used to indicate the authenticated user.
   * @param messageId ID of Message to retrieve.
   * @return MimeMessage MimeMessage populated from retrieved Message.
   * @throws IOException
   * @throws MessagingException
   */
  public static MimeMessage getMimeMessage(Gmail service, String userId, String messageId)
      throws IOException, MessagingException {
    Message message = service.users().messages().get(userId, messageId).setFormat("raw").execute();

    Base64 base64Url = new Base64(true);
    byte[] emailBytes = base64Url.decodeBase64(message.getRaw());

    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

    return email;
  }

  // ...

}