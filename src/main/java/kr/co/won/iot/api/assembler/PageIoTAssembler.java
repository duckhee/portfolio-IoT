package kr.co.won.iot.api.assembler;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageIoTAssembler {
    private final ModelMapper modelMapper;
}
