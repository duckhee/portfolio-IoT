package kr.co.won.study.service;

import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service(value = "studyService")
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final ModelMapper modelMapper;

    private final UserPersistence userPersistence;

    private final StudyPersistence studyPersistence;
}
