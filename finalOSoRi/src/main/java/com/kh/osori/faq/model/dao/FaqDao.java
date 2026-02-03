package com.kh.osori.faq.model.dao;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.osori.faq.model.vo.Faq;

@Repository
public class FaqDao {

	public List<Faq> questionList(SqlSessionTemplate sqlSession) {
		return sqlSession.selectList("faqMapper.questionList");
	}

}
