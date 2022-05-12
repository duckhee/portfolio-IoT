package kr.co.won.study.event;

import kr.co.won.study.domain.StudyDomain;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 *
 * File Name        : StudyEvent
 * Author           : Doukhee Won
 * Date             : 2022/05/12
 * Version          : v0.0.1
 *
 * class create and update event Domain
 */
@Getter
public class StudyEvent extends ApplicationEvent {

    private StudyDomain study;

    public StudyEvent(StudyDomain source) {
        super(source);
        this.study = source;
    }

}
