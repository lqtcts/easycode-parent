package hellohessian.service.bean;

import java.io.Serializable;

public class Test implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9194336585811057801L;
	private String userName;
	private String userPhone;
	
	public Test() {
		 
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public Test(String userName, String userPhone) {
		super();
		this.userName = userName;
		this.userPhone = userPhone;
	}
	
	
}
