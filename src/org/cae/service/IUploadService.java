package org.cae.service;

import java.io.File;
import java.io.InputStream;

import org.cae.common.ServiceResult;

public interface IUploadService {

	final static String DOWNLOAD_ZIP_PATH = "result.zip";
	
	final static String DOWNLOAD_HTML_PATH = "D:\\nginx-1.12.0\\html\\aqours";
	
	ServiceResult uploadCallService(InputStream input);
	
	File downloadCallService();
}
