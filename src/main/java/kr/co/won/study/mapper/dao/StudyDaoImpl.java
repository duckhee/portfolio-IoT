package kr.co.won.study.mapper.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional
@RequiredArgsConstructor
public class StudyDaoImpl implements StudyDao {

    private final SqlSession sqlSession;


}
