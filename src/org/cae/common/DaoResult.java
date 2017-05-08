package org.cae.common;

public class DaoResult {

	private boolean result;
	private String errInfo;
	public DaoResult(boolean result,String errInfo){
		this.result=result;
		this.errInfo=errInfo;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getErrInfo() {
		return errInfo;
	}
	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}
	
}
