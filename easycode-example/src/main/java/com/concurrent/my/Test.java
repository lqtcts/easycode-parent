
package  com.concurrent.my;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.utils.DateUtil;


public class Test{
	public static void main(String[] args) throws IOException {
		long a = 1466759033820L;
		Date date = new Date(a);
		System.out.println(DateUtil.dateToStrLong(date));
		
	}
	
}