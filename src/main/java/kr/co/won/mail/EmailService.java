package kr.co.won.mail;


import kr.co.won.mail.form.VerifiedMessage;

public interface EmailService {

    public default void sendEmail() {
        return;
    }

    public default void sendConfirmEmail(VerifiedMessage message) {
        return;
    }
}
