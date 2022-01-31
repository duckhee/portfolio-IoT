package kr.co.won.mail;

import kr.co.won.mail.form.VerifiedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Profile(value = {"local", "dev", "docker", "test"})
@Service(value = "consoleEmailService")
@RequiredArgsConstructor
public class EmailServiceConsoleImpl implements EmailService {

    @Override
    public void sendConfirmEmail(VerifiedMessage message) {
        log.info("message host ::: {}, received user address ::: {}, join uri ::: {}, subject ::: {}, message ::: {}",
                message.getHost(), message.getUserEmail(), message.getConfirmUri().toString(), message.getSubject(), message.getMessage());
    }
}
