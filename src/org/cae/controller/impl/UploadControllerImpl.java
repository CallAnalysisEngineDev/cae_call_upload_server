package org.cae.controller.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.cae.common.ServiceResult;
import org.cae.controller.IUploadController;
import org.cae.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller("uploadController")
public class UploadControllerImpl implements IUploadController{
	
	@Autowired
	private IUploadService uploadService;
	
	@Override
	@ResponseBody
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public Map<String,Object> uploadCallController(@RequestParam("file") CommonsMultipartFile file){
		try{
			ServiceResult result =uploadService.uploadCallService(file.getInputStream());
			return result.toMap();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void downloadCallController(HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
