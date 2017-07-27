package org.cae.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.cae.entity.CallRecord;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface IUploadController {

	Map<String, Object> uploadCallController(CommonsMultipartFile file,
			CallRecord callRecord);

	void downloadCallController(HttpServletResponse response);
}
