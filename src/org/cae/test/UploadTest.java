package org.cae.test;

import static org.junit.Assert.*;

import org.cae.service.IUploadService;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UploadTest {

	private IUploadService service;

	@SuppressWarnings("resource")
	@Before
	public void init() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		service = (IUploadService) ctx.getBean("uploadService");
	}
}
