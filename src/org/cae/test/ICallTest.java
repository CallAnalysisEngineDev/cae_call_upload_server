package org.cae.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cae.common.DaoResult;
import org.cae.dao.ICallDao;
import org.cae.dao.ISongDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class ICallTest {
	private ISongDao songDao;
	private ICallDao callDao;
	JdbcTemplate template;
	@SuppressWarnings("resource")
	@Before
	public void init(){
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		songDao=(ISongDao)ctx.getBean("songDao");
		callDao=(ICallDao)ctx.getBean("callDao");
		template=(JdbcTemplate)ctx.getBean("jdbcTemplate");

	}
	public void test2(){
	}
	@Test
	public void test3(){
		List<String> songName=new ArrayList<>();
		songName.add("トリコリコPLEASE!!");
		songName.add("LONELY TUNING");
		songName.add("夜空はなんでも知ってるの");
		songName.add("Aqours☆HEROES");
		DaoResult daoResult =callDao.updateCallVersionDao(songName);
	}

}
