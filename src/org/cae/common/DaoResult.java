package org.cae.common;

import java.util.List;

public class DaoResult<T> {

	private boolean successed;
	private List<T> result;
	private List<T> failList;
	private String errInfo;
	
	public DaoResult(boolean successed,List<T> result){
		this.successed=successed;
		this.result=result;
	}
	public DaoResult(boolean successed,List<T> result,List<T> failList){
		this.successed=successed;
		this.result=result;
		this.failList=failList;
	}
	public boolean isSuccessed() {
		return successed;
	}
	public void setSuccessed(boolean successed) {
		this.successed = successed;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public List<T> getFailList() {
		return failList;
	}
	public void setFailList(List<T> failList) {
		this.failList = failList;
	}
	public String getErrInfo() {
		return errInfo;
	}
	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}
	
}
