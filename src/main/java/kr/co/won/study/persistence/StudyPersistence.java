package kr.co.won.study.persistence;

import kr.co.won.study.domain.StudyDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyPersistence extends JpaRepository<StudyDomain, Long>, StudyPersistenceExtension {

    boolean existsByPath(String path);

    Optional<StudyDomain> findByIdx(Long studyIdx);

    Optional<StudyDomain> findByPath(String path);
}
