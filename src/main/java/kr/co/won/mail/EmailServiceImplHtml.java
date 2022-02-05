package kr.co.won.mail;

import kr.co.won.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Profile(value = {"product"})
@Service(value = "htmlEmailService")
@RequiredArgsConstructor
public class EmailServiceImplHtml implements EmailService {

    private final JavaMailSender javaMailSender;

    private final AppProperties appProperties;
}
