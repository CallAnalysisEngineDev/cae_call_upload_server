package org.cae.service;

import java.io.InputStream;

import org.cae.common.ServiceResult;

public interface IUploadService {

	ServiceResult uploadCallService(InputStream input);
	
	InputStream downloadCallService();
}
