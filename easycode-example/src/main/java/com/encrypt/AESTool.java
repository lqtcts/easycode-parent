
package com.encrypt;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

/**
 * AES encryption and decryption tool.
 * 
 * @author ben
 * @creation 2014年3月20日
 */
public class AESTool {

	private static byte[] initVector = { 0x32, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x38, 0x27, 0x36, 0x35, 0x33, 0x23, 0x32, 0x31 };
//	private static byte[] initVector = { 50,55,54,53,52,51,50,49,56,39,54,53,51,35,50,49 };
	
	

	/**
	 * Encrypt the content with a given key using aes algorithm.
	 * 
	 * @param content
	 * @param key
	 *            must contain exactly 32 characters
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content, String key) throws Exception {
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null!");
		}
		String encrypted = null;
		byte[] keyBytes = key.getBytes();
		if (keyBytes.length != 32 && keyBytes.length != 24 && keyBytes.length != 16) {
			throw new IllegalArgumentException("Key length must be 128/192/256 bits!");
		}
		byte[] encryptedBytes = null;
		encryptedBytes = encrypt(content.getBytes(), keyBytes, initVector);
		encrypted = new String(Hex.encode(encryptedBytes));
		return encrypted;
	}

	/**
	 * Decrypt the content with a given key using aes algorithm.
	 * 
	 * @param content
	 * @param key
	 *            must contain exactly 32 characters
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content, String key) throws Exception {
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null!");
		}
		String decrypted = null;
		byte[] encryptedContent = Hex.decode(content);
		byte[] keyBytes = key.getBytes();
		byte[] decryptedBytes = null;
		if (keyBytes.length != 32 && keyBytes.length != 24 && keyBytes.length != 16) {
			throw new IllegalArgumentException("Key length must be 128/192/256 bits!");
		}
		decryptedBytes = decrypt(encryptedContent, keyBytes, initVector);
		decrypted = new String(decryptedBytes);
		return decrypted;
	}

	/**
	 * Encrypt data.
	 * 
	 * @param plain
	 * @param key
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] plain, byte[] key, byte[] iv) throws Exception {
		PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
		CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
		aes.init(true, ivAndKey);
		return cipherData(aes, plain);
	}

	/**
	 * Decrypt data.
	 * 
	 * @param cipher
	 * @param key
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] cipher, byte[] key, byte[] iv) throws Exception {
		PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
		CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
		aes.init(false, ivAndKey);
		return cipherData(aes, cipher);
	}

	/**
	 * Encrypt or decrypt data.
	 * 
	 * @param cipher
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private static byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data) throws Exception {
		int minSize = cipher.getOutputSize(data.length);
		byte[] outBuf = new byte[minSize];
		int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
		int length2 = cipher.doFinal(outBuf, length1);
		int actualLength = length1 + length2;
		byte[] result = new byte[actualLength];
		System.arraycopy(outBuf, 0, result, 0, result.length);
		return result;
	}

	public static void main(String[] args) throws Exception {
		String appid = "canairport001";
		String key = "1111111111111111";
		// // String xml =
		// "<root><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name><name>test</name></root>";
		// String xml="00";
		// //使用aes加密
		// String encrypted = AESTool.encrypt(xml, key);
		// System.out.println("encrypted: \n" + encrypted);
		// System.out.println("encrypted length: \n" + encrypted.length());
		//
		// //解密
		// String decrypted = AESTool.decrypt(encrypted, key);
		// System.out.println("decrypted: \n" + decrypted);
		// System.out.println("decrypted length: \n" + decrypted.length());
		//
		//
		// boolean isSuccessful = StringUtils.equals(decrypted, xml);
		// System.out.println(isSuccessful);
		String a = "fbac6c10aa8e0f946e02e0b57b35c7483b57b485c41147c31511ad1c212e33881f432d9dad"
				+ "ddb6bcd0d3c6dbd1e6ae36881c48c8f7ef72d42cba27a87d86f0e8a0a05c3f9686d06f03c6b"
				+ "79b3d3f5524fcf742cdbddb87df151db0ad5645aad40085ab4aff280f139ba1bb72389918bed"
				+ "3189ffeea1481b2052ca3b6b7b66386999f02a774a3cd056e5a4378d5eeecd9679072ce45bb3a9"
				+ "d124df5736b5cef12e7265e46d4f91c1790136ba278f7fe4c75b9cbe0e470dbf3d4136ffa9954f3"
				+ "83f8b499d3dc097a4e55472d4fb87b8bc40fc81ac9602a4b787372f899ae7e55ab433951a35cff5d0905"
				+ "0fe4ff18edf8583438eb90f3ec3268f110b5b71e28b7f1c4641a2bbd8aaca07546e6c65b7effcee637467e5"
				+ "c74c9594282d31316458802268de20f2d9d6d370b136f11cac376f7b6120bfca8f2aad7cd8bbaf3d31513"
				+ "c5352fbc389285805db085d2a1ca0cde0dfcbbc1190ee44a5059e292b42e065989fa66a38ae43f3003ee0"
				+ "ab4997b3e70ccd7d059b4737849844bfb2ffcde2227c0070f052d34cc1f3f1dcb738730a9b3d57a285572"
				+ "94c0f441d509ee8aa2a4a4f6631f2107d8c78575e47f4d2d14ca29a168428d879171cdb99523050e66f4c"
				+ "426a21eb313c8efdbd995f907cf7e47e7eee49ae9688316a7b88eae3fd1ef790e2a516bd4f0b7162b5779"
				+ "28737dc92b1d7afa91c076c60c1439238320dbd6225bc7598288c436dc500c8519b67a7f7e87fdd304af7"
				+ "a5a857980a69536dd43ea1423541e74b819c234ea4217bc6a33e2413d6e4a3dd4003ed731890bf8450847"
				+ "ee9ba8af9be9d98dfea2ccd8ded41ebf85c0976de26b0fd4a6d17e0dbb63881a94484aec3f20d1136421b"
				+ "35d6f6e221b51866d361e999e707d39d124481f636717bdb7be639eaa863f3c8e4a3bce7b0b8e4dae0a3d"
				+ "c41738575b32d0fd3a8c8efead04a016c375cd15f8e98aedc293886567fa7574f449e6ff87defa1d42eb0"
				+ "87eb8b2c48f8c78b09c80fe9a380ac3aba76f67abbdab7fd982cdafec475960cef095667ca56ad36db377"
				+ "d2540f61d156a70c7be1df7e55ad99528547e164aa975bee7a8fe8a0ed61bb44ec8d24f41234274cc183c"
				+ "e342072333bf4fd1a246775d9c1fa1a4c146ea31f4fb461af48e84e3de40f944a1d5a0dc61a87c8ebca6c"
				+ "c3a09a2f5f21c431734629d7cddacd9bcea18fbcc6ccbd0c1d37f5eebb7351a526a6722697e3542ebe589"
				+ "6202524524d1ad120e3c631c3e204d8b0b1d51de46256cb94229756a3fa31c415d74fe26ffacd5f4b2740"
				+ "a076891d95fc95f632c3cb9a023d6e11d55dc555f9c9ae6b9de18ef55f3e8c308d5e2558d2e9bb94a9220"
				+ "d4f1112e7606278333aa5325f2b380284946158d9ce88a6dd71a3d953615a2e9b8c2305e37748a7d25fef"
				+ "46ab678bc188ae72703ba6e2db89fc17fb345f6e46191fe73829ba8ba35e13a2a50eead2341bcec9a";
	
//		System.out.println(AESTool.decrypt(a, "adjdjfjfjfjdkdkd"));
		String testkey="adjdjfjfjfjdkdkd";
		String b="{\"rescode\":\"200\",\"resdes\":\"操作成功\",\"resvalue\":{\"accountBean\":null,\"address\":\"frefwfrewf\",\"category\":\"7\",\"coefficient\":2,\"company\":\"hhh\",\"contacts\":\"ffrewfrew\",\"contactsphone\":\"13811111111\",\"createtime\":{\"date\":25,\"day\":1,\"hours\":20,\"minutes\":11,\"month\":0,\"seconds\":27,\"time\":1453723887000,\"timezoneOffset\":-480,\"year\":116},\"createuser\":\"pxene\",\"domain\":\"http://www.baidu.com\",\"email\":\"ss@pxene.com\",\"fox\":\"\",\"id\":\"1852090d-fc6d-4e60-aaad-949d10b0b4c6\",\"invoice\":\"\",\"lastlogintime\":null,\"logins\":0,\"name\":\"hhh\",\"operate\":\"\",\"organizationno\":\"1111111\",\"parentid\":\"\",\"parentname\":\"\",\"passwd\":\"2577849f207e243f42f78428f62900e3\",\"phone\":\"2222222\",\"province\":\"\",\"remark\":\"\",\"scanmemberid\":\"\",\"seller\":\"ffff\",\"sitename\":\"fewfew\",\"status\":\"02\",\"statusstr\":\"\",\"town\":\"\",\"type\":\"00\",\"updatetime\":{\"date\":30,\"day\":6,\"hours\":15,\"minutes\":15,\"month\":0,\"seconds\":26,\"time\":1454138126000,\"timezoneOffset\":-480,\"year\":116},\"updateuser\":\"pxene\",\"usertype\":2,\"usertypestr\":\"\",\"vocationid\":\"\",\"zip\":111111}}";
//		System.out.println(AESTool.encrypt(b, "adjdjfjfjfjdkdkd"));
//		String encrypt = AESTool.encrypt(b, "adjdjfjfjfjdkdkd");
//		System.out.println(AESTool.decrypt(encrypt, "adjdjfjfjfjdkdkd"));
		String c ="{\"body\":{\"result\":{\"rescode\":\"200\",\"resdes\":\"操作成功\",\"resvalue\":{\"accountBean\":null,\"address\":\"frefwfrewf\",\"category\":\"7\",\"coefficient\":2,\"company\":\"hhh\",\"contacts\":\"ffrewfrew\",\"contactsphone\":\"13811111111\",\"createtime\":{\"date\":25,\"day\":1,\"hours\":20,\"minutes\":11,\"month\":0,\"seconds\":27,\"time\":1453723887000,\"timezoneOffset\":-480,\"year\":116},\"createuser\":\"pxene\",\"domain\":\"http://www.baidu.com\",\"email\":\"ss@pxene.com\",\"fox\":\"\",\"id\":\"1852090d-fc6d-4e60-aaad-949d10b0b4c6\",\"invoice\":\"\",\"lastlogintime\":null,\"logins\":0,\"name\":\"hhh\",\"operate\":\"\",\"organizationno\":\"1111111\",\"parentid\":\"\",\"parentname\":\"\",\"passwd\":\"2577849f207e243f42f78428f62900e3\",\"phone\":\"2222222\",\"province\":\"\",\"remark\":\"\",\"scanmemberid\":\"\",\"seller\":\"ffff\",\"sitename\":\"fewfew\",\"status\":\"02\",\"statusstr\":\"\",\"town\":\"\",\"type\":\"00\",\"updatetime\":{\"date\":30,\"day\":6,\"hours\":15,\"minutes\":15,\"month\":0,\"seconds\":26,\"time\":1454138126000,\"timezoneOffset\":-480,\"year\":116},\"updateuser\":\"pxene\",\"usertype\":2,\"usertypestr\":\"\",\"vocationid\":\"\",\"zip\":111111}}},\"head\":{\"res_code\":\"0\"}}";
		
		System.out.println(AESTool.encrypt(c, testkey));
		String encrypt = AESTool.encrypt(c, testkey);
		System.out.println(AESTool.decrypt(encrypt, testkey));
		
		String d="fbac6c10aa8e0f946e02e0b57b35c7483b57b485c41147c31511ad1c212e33881f432d9dadddb6bcd0d3c6dbd1e6ae36881c48c8f7ef72d42cba27a87d86f0e8a0a05c3f9686d06f03c6b79b3d3f5524fcf742cdbddb87df151db0ad5645aad40085ab4aff280f139ba1bb72389918bed3189ffeea1481b2052ca3b6b7b66386999f02a774a3cd056e5a4378d5eeecd9679072ce45bb3a9d124df5736b5cef12e7265e46d4f91c1790136ba278f7fe4c75b9cbe0e470dbf3d4136ffa9954f383f8b499d3dc097a4e55472d4fb87b8bc40fc81ac9602a4b787372f899ae7e55ab433951a35cff5d09050fe4ff18edf8583438eb90f3ec3268f110b5b71e28b7f1c4641a2bbd8aaca07546e6c65b7effcee637467e5c74c9594282d31316458802268de20f2d9d6d370b136f11cac376f7b6120bfca8f2aad7cd8bbaf3d31513dc5352fbc389285805db085d2a1ca0cde0dfcbbc1190ee44a5059e292b42e065989fa66a38ae43f3003ee0ab4997b3e70ccd7d059b4737849844bfb2ffcde2227c0070f052d34cc1f3f1dcb738730a9b3d57a28557294c0f441d509ee8aa2a4a4f6631f2107d8c78575e47f4d2d14ca29a168428d879171cdb99523050e66f4c426a21eb313c8efdbd995f907cf7e47e7eee49ae9688316a7b88eae3fd1ef790e2a516bd4f0b7162b577928737dc92b1d7afa91c076c60c1439238320dbd6225bc7598288c436dc500c8519b67a7f7e87fdd304af7a5a857980a69536dd43ea1423541e74b819c234ea4217bc6a33e2413d6e4a3dd4003ed731890bf8450847ee9ba8af9be9d98dfea2ccd8ded41ebf85c0976de26b0fd4a6d17e0dbb63881a94484aec3f20d1136421b35d6f6e221b51866d361e999e707d39d124481f636717bdb7be639eaa863f3c8e4a3bce7b0b8e4dae0a3dc41738575b32d0fd3a8c8efead04a016c375cd15f8e98aedc293886567fa7574f449e6ff87defa1d42eb087eb8b2c48f8c78b09c80fe9a380ac3aba76f67abbdab7fd982cdafec475960cef095667ca56ad36db377d2540f61d156a70c7be1df7e55ad99528547e164aa975bee7a8fe8a0ed61bb44ec8d24f41234274cc183ce342072333bf4fd1a246775d9c1fa1a4c146ea31f4fb461af48e84e3de40f944a1d5a0dc61a87c8ebca6cc3a09a2f5f21c431734629d7cddacd9bcea18fbcc6ccbd0c1d37f5eebb7351a526a6722697e3542ebe5896202524524d1ad120e3c631c3e204d8b0b1d51de46256cb94229756a3fa31c415d74fe26ffacd5f4b2740a076891d95fc95f632c3cb9a023d6e11d55dc555f9c9ae6b9de18ef55f3e8c308d5e2558d2e9bb94a9220d4f1112e7606278333aa5325f2b380284946158d9ce88a6dd71a3d953615a2e9b8c2305e37748a7d25fef46ab678bc188ae72703ba6e2db89fc17fb345f6e46191fe73829ba8ba35e13a2a50eead2341bcec9a";
		
		System.out.println(AESTool.decrypt(d, testkey));
		
		
	}
}