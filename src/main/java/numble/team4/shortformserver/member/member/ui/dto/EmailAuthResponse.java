package numble.team4.shortformserver.member.member.ui.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmailAuthResponse {

    private int authNumber;

    public static EmailAuthResponse from(int authNumber) {
        return new EmailAuthResponse(authNumber);
    }
}
