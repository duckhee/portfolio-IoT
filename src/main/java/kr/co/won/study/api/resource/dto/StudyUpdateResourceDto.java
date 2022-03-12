package kr.co.won.study.api.resource.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class StudyUpdateResourceDto extends RepresentationModel<StudyUpdateResourceDto> {

    private Long idx;

    private String name;

    private String path;

    private LocalDateTime updatedAt;
}
