package kr.co.won.mail.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifiedMessage extends EmailMessage {

    private String confirmUri;

    public VerifiedMessage(String host, String userEmail, String subject, String message, String confirmUri) {
        super(host, userEmail, subject, message);
        this.confirmUri = confirmUri;
    }
}
