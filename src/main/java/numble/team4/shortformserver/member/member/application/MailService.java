package numble.team4.shortformserver.member.member.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.member.exception.FailMailAuthNumberIssuanceException;
import numble.team4.shortformserver.member.member.ui.dto.EmailAuthResponse;
import numble.team4.shortformserver.member.member.ui.dto.MemberEmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String host;

    private final String title = "Kicks 숏폼 컨텐츠 이메일 인증";
    private final String content = "\nKicks 숏폼 컨텐츠 이메일 확인을 위한 인증번호입니다." +
            "\n아래의 6자리 숫자를 인증 번호 확인란에 입력해주세요.\n";

    public EmailAuthResponse sendAuthMail(MemberEmailRequest request) {

        String authNum = issueMailAuthNumber();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true, "UTF-8");
            messageHelper.setFrom(host);
            messageHelper.setTo(request.getEmail());
            messageHelper.setSubject(title);
            messageHelper.setText(content + authNum);

            mailSender.send(message);

            return EmailAuthResponse.from(Integer.parseInt(authNum));

        } catch (Exception e) {
            throw new FailMailAuthNumberIssuanceException();
        }
    }

    private String issueMailAuthNumber() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
