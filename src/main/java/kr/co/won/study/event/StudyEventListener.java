package kr.co.won.study.event;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyEventListener {


    @EventListener(value = {StudyEvent.class})
    public void studyEventListener() {

    }

}
