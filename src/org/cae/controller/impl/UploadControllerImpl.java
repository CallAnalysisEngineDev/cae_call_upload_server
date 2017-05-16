package org.cae.controller.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.cae.common.ServiceResult;
import org.cae.controller.IUploadController;
import org.cae.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
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
	@ResponseBody
	@RequestMapping(value="/download",method=RequestMethod.GET)
	public void downloadCallController(HttpServletResponse response) {
	try {
		File file =uploadService.downloadCallService();
		String contentType=URLConnection.guessContentTypeFromName(file.getName());
		response.setContentType(contentType);
		response.setContentLength((int)file.length());
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
		InputStream inputStream=new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
		new File(IUploadService.DOWNLOAD_ZIP_PATH).delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
