package kr.co.won.websocket.stomp.form;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMsgForm {

    @NotBlank(message = "need to device serial number.")
    private String serialNumber;

    // TODO check api key
    @NotBlank(message = "need to api key.")
    private String apiKey;

    private String topic;

    private String data;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
