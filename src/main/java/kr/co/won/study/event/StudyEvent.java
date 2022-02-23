package kr.co.won.study.event;

import kr.co.won.study.domain.StudyDomain;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
public class StudyEvent extends ApplicationEvent {

    private StudyDomain study;

    public StudyEvent(StudyDomain source) {
        super(source);
        this.study = source;
    }

}
