package kr.co.won.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import java.net.URI;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginFailedDto {

    private String msg;

    private URI loginUri;
}
