package kr.co.won.blog.mapper.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional
@RequiredArgsConstructor
public class BlogDaoImpl implements BlogDao {

    private final SqlSession sqlSession;
}
