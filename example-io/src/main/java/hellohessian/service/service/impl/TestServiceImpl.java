package hellohessian.service.service.impl;

import hellohessian.service.bean.Test;
import hellohessian.service.service.ITestServiec;

public class TestServiceImpl implements ITestServiec{

	public Test getTest() {
		return new Test("我叫测试", "测试号码");
	}

}
