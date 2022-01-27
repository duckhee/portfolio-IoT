package kr.co.won.errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;

import java.io.IOException;

@Slf4j
@JsonComponent
@RequiredArgsConstructor
public class ApiValidationError extends JsonSerializer<Errors> {

    private final MessageSource messageSource;

    /**
     * validation error change json serialize
     */
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        /** error의 시작을 알려주는 것 버전 업데이트로 인해서 Jackson 라이브러리가 Arrary를 먼저 만드는 것을 허용하지 않기 때문에 */
        /** Error가 여려개이니깐 Errors를 답아주기 위해서 배열의 시작을 알려주는 것 */
        jsonGenerator.writeFieldName("errors");
        jsonGenerator.writeStartArray();

        errors.getFieldErrors().forEach(error -> {
            try {
                /** error object start */
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("field", error.getField());
                jsonGenerator.writeStringField("objectName", error.getObjectName());
                jsonGenerator.writeStringField("code", error.getCode());
                jsonGenerator.writeStringField("message", messageSource.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), LocaleContextHolder.getLocale()));
                Object rejectedValue = error.getRejectedValue(); // reject된 value를 가져오는 것
                if (rejectedValue != null) {
                    jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
                }
                /** 배열의 경우 Object의 끝을 표시해줘야한다. */
                jsonGenerator.writeEndObject();
                /** error를 json object로 만들어주는 것 */
            } catch (IOException exception) {
                log.error("json validation make failed :::: {}", exception.toString());
            }
        });

        errors.getGlobalErrors().forEach(error -> {
            try {
                /** error를 json object로 만들어주는 것 */
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", error.getObjectName());
                jsonGenerator.writeStringField("code", error.getCode());
                jsonGenerator.writeStringField("message", messageSource.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), LocaleContextHolder.getLocale()));
                /** error를 json object로 만들어주는 것 */
                jsonGenerator.writeEndObject();

            } catch (IOException exception) {
                log.error("validator failed error logging :::: {}", exception.toString());

            }

        });

        /** Error가 여려개이니깐 Errors를 답아주기 위해서 배열의 끝을 알려주는 것 */
        jsonGenerator.writeEndArray();
    }
}
