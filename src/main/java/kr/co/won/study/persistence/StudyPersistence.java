package kr.co.won.study.persistence;

import kr.co.won.study.domain.StudyDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyPersistence extends JpaRepository<StudyDomain, Long> {
}
