package com.kh.osori.faq.model.service;
import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.osori.faq.model.dao.FaqDao;
import com.kh.osori.faq.model.vo.Faq;

@Service
public class FaqServiceImpl implements FaqService{
	@Autowired
	private FaqDao dao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Faq> questionList() {
		return dao.questionList(sqlSession);
	}

}
