package com.encrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.util.encoders.Base64;

public class ParamClass {

	// 在这里写请求数据
	public static String getContent(String contentName)  {
		String content = new String("utf-8");
		String Startcontent = getPublicParam(contentName);
		
		if (contentName.equals("getAdvertiser")) {

			content = "{\"advertiserid\": \"c5633bcf-63eb-4112-862b-fdf7b38ad540\", \"currentpage\": \"0\", \"pagecount\": \"10\" } ";

		} else if (contentName.equals("createAdvertiser")) {

			content = "{\"name\":\"OpenAdver-1\",\"passwd\":\"qqqqqq\",\"company\":\"111\",\"contacts\":\"11\",\"contactsphone\":\"13111111111\",\"coefficient\":\"111\",\"phone\":\"222222\",\"email\":\"1111111@qq.com\",\"address\":\"222\",\"category\":\"2\",\"seller\":\"22\",\"usertype\":\"2\",\"zip\":\"222222\",\"domain\":\"2222\",\"sitename\":\"22\",\"organizationno\":\"22\",\"parentadvertiserid\":\"c5633bcf-63eb-4112-862b-fdf7b38ad540\"}";

		} else if (contentName.equals("editAdvertiser")) {

			content = "{\"id\":\"c5633bcf-63eb-4112-862b-fdf7b38ad540\", \"company\":\"1\", \"contacts\":\"1\", \"contactsphone\":\"1\", \"coefficient\":\"1\", \"phone\":\"1\", \"email\":\"1\", \"address\":\"1\", \"category\":\"3\", \"seller\":\"1\", \"usertype\":\"1\", \"zip\":\"1\", \"domain\":\"1\", \"sitename\":\"1\", \"organizationno\":\"1\", \"scanmemberid\":\"1\" }";

		} else if (contentName.equals("delAdvertiser")) {

			content = "{\"advertiserid\":\"c5633bcf-63eb-4112-862b-fdf7b38ad540\"}";

		} else if (contentName.equals("addAdvertiserAptitudes")) {//上传扫描件
			String[] typeString = { "aptitudes", "scanmember" };
			String filePath="C:\\Users\\wuchengbin\\Desktop\\source";
			String filenameString=filePath+"\\ddd.png";
			String imgToBase64 = imgToBase64(filenameString);
			content = "{\"advertiserid\":\"9ddec381-f0e9-4b47-a87b-655ee63e5522\",\"type\":\"" + typeString[0] + "\",\"imgtype\":\"png\",\"imgbase\":\""+imgToBase64+"\",}";

		} else if (contentName.equals("advRecharge")) {

			content = "{\"advertiserid\":\"9ddec381-f0e9-4b47-a87b-655ee63e5522\",\"money\":\"10000\"}";

		} else if (contentName.equals("getAdvertiserBalance")) {

			content = "{\"advertiserid\":\"9ddec381-f0e9-4b47-a87b-655ee63e5522\"}";

		} else if (contentName.equals("createCampaign")) {

			content = "{\"name\":\"testb\",\"startdate\":\"2016-04-14\",\"enddate\":\"2016-05-14\",\"budget\":\"100\",\"specialfunds\":\"1000\",\"advertiserid\":\"9ddec381-f0e9-4b47-a87b-655ee63e5522\"}";

		} else if (contentName.equals("delCampaignByid")) {

			content = "{\"campaignid\":\"deab9b4e-3f3c-4cb4-8325-246cc8151ffe\"}";

		} else if (contentName.equals("editCampaign")) {

			content = "{\"campaignid\":\"deab9b4e-3f3c-4cb4-8325-246cc8151ffe\",\"name\":\"test1\",\"startdate\":\"2016-04-15\",\"enddate\":\"2016-05-15\",\"budget\":\"110\",\"specialfunds\":\"1100\",\"status\":\"\"}";

		} else if (contentName.equals("getCampaignById")) {

			content = "{\"campaignid\":\"deab9b4e-3f3c-4cb4-8325-246cc8151ffe\" } ";

		} else if (contentName.equals("getCampaignList")) {

			content = "{\"advertiserid\":\"c5633bcf-63eb-4112-862b-fdf7b38ad540\",\"currentpage\":\"0\",\"pagecount\":\"10\"}";

		} else if (contentName.equals("addSource")) {
			String[] suorceType={"image","video"};
			String groupidString="f06e1c92-15c1-4958-be40-cc12bc907187";
			if ("image".equals(suorceType[1])) {
				String filenameString="C:\\Users\\wuchengbin\\Desktop\\source\\6.png";
				String imgToBase64 = imgToBase64(filenameString);
				content = "{\"groupid\":\""+groupidString+"\",\"type\":\""+suorceType[0]+"\",\"sourcetype\":\"png\",\"sourcebase\":\""+imgToBase64+"\",\"sourcefilename\":\" car\",\"filesize\":\"1024\",\"sourcesize\":\"320x50\"}";
			}else {
				String filenameString="C:\\Users\\wuchengbin\\Desktop\\source\\Clip_480_5sec_6mbps_h264.mp4";
				String imgToBase64 = imgToBase64(filenameString);
				content = "{\"groupid\":\""+groupidString+"\",\"type\":\""+suorceType[1]+"\",\"sourcetype\":\"mp4\",\"sourcebase\":\""+imgToBase64+"\",\"sourcefilename\":\"Clip_480_5sec_6mbps_h264\",\"filesize\":\"3059\",\"sourcesize\":\"720x480\"}";
			}

		} else if (contentName.equals("delSource")) {

			content = "{\"sourceid\": \"28941f32-5c1f-4af5-80bf-bd7f057e3324\" } ";

		} else if (contentName.equals("createCreative")) {

			content = "{\"groupid\":\"f06e1c92-15c1-4958-be40-cc12bc907187\",\"name\":\"c1\",\"imolinks\":\"23131321231,1321321231321321,111\",\"cmolinks\":\"131,1312313,11321321,1313\",\"adtype\":\"02\",\"typevalue\":\"2\",\"adimagesize\":\"320x50\",\"videosize\":\"\",\"timelength\":\"\",\"txtfirst\":\"测试创意接口\",\"materialid\":\"28941f32-5c1f-4af5-80bf-bd7f057e3324\"}";

		} else if (contentName.equals("editCreative")) {

			content = "{\"mapid\":\"94368073-af7a-4308-9ea1-8f0184f292cd\",\"groupid\":\"f06e1c92-15c1-4958-be40-cc12bc907187\",\"name\":\"c2\",\"imolinks\":\"00000;11111\",\"cmolinks\":\"222222;33333\",\"adtype\":\"02\",\"adimagesize\":\"320x50\"}";

		} else if (contentName.equals("delCreative")) {

			content = "{\"mapid\":\"b5ca6e20-b4fe-4988-ac33-d29b343e51e7\"}";

		}else if (contentName.equals("getCreative")) {

			content = "{\"groupid\":\"f06e1c92-15c1-4958-be40-cc12bc907187\",    \"mapid\": \"\", \"currentpage\": \"0\", \"pagecount\": \"10\", } ";

		}

		return Startcontent + content + "}";
	}

	public static String imgToBase64(String path) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
			// 对字节数组Base64编码
			byte[] encode = Base64.encode(data);
			String resultString;
			
			resultString = new String(encode,"UTF-8");
			
			return resultString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String  getPublicParam(String contentName) {
		String[] servicenae={
				"advertiserServiceCall",
				"campaignServiceCall",
				"sourceServiceCall",
				"creativeServiceCall"
		};

		int a =0;
		if (contentName.toLowerCase().contains("advertiser")) {
			a=0;
		}else if (contentName.toLowerCase().contains("campaign")) {
			a=1;
		}else if (contentName.toLowerCase().contains("source")) {
			a=2;
		}else if (contentName.toLowerCase().contains("creative")) {
			a=3;
		}
		
		String startcontent = "{\"servicename\": \""+servicenae[a]+"\",\"funcname\": \"" + contentName + "\",\"methodparam\":";
		return startcontent;
	}
	
	public static void main(String[] args) {
		String imgToBase64 = imgToBase64("C:\\Users\\wuchengbin\\Desktop\\source\\ddd.png");
		
		
	}
}
