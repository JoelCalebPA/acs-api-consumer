package pe.com.domain.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.RollingFileAppender;

public class CustomFileAppender extends RollingFileAppender {

	@Override
	public void setFile(String fileName) {
		if (fileName.indexOf("%timestamp") >= 0) {
			Date d = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			fileName = fileName.replaceAll("%timestamp", format.format(d));
		}
		super.setFile(fileName);
	}
}