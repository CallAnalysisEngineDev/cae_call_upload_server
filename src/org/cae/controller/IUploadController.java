package org.cae.controller;

import java.util.Map;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface IUploadController {

	Map<String,Object> uploadCallController(CommonsMultipartFile file);
}
