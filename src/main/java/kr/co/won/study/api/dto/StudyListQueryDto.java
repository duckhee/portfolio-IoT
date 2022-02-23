package kr.co.won.study.api.dto;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.inject.BindingAnnotation;

@Data
@NoArgsConstructor
public class StudyListQueryDto {

    private Long idx;
    private String name;
    private String path;


}
