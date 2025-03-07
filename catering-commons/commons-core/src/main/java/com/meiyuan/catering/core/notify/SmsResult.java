package com.meiyuan.catering.core.notify;

/**
 * 发送短信的返回结果
 * @author admin
 */
public class SmsResult {
	private boolean successful;
	private Object result;

	/**
	 * 短信是否发送成功
	 *
	 * @return
	 */
	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
