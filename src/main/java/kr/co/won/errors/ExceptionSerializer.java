package kr.co.won.errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.MessageSource;

import java.io.IOException;

@Slf4j
@JsonComponent
@RequiredArgsConstructor
public class ExceptionSerializer extends JsonSerializer<Exception> {
    /**
     * Message Source Get Locale Code
     */
    private final MessageSource messageSource;

    @Override
    public void serialize(Exception value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        /** error start */
        String errorName = value.getClass().getName();
        String errorMsg = value.getLocalizedMessage();
        gen.writeStringField("msg", errorMsg);
        log.error("error class ::: {}, error msg ::: {}", errorName, errorMsg);
    }
}
