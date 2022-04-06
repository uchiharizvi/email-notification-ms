package com.credentials.emailms.model;

import lombok.Data;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.MessageTag;

import java.util.List;
@Data
public class EmailMessage {
    private String configurationSetName;
    private Destination destination;//mandatory
    private Message message;//mandatory
    private List<String> replyToAddressesMember;
    private String returnPath;
    private String returnPathArn;
    private String source;//mandatory
    private String sourceArn;
    private List<MessageTag> tagsMember;
}
