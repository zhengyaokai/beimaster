package bmatser.util;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;  
import java.security.SecureRandom;  
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
  
import javax.crypto.Cipher;  
import javax.crypto.KeyGenerator;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.DESKeySpec;  
import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.igfay.jfig.JFig;
  
  
/** 
 * DES安全编码组件 
 *  
 * <pre> 
 * 支持 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR) 
 * DES                  key size must be equal to 56 
 * DESede(TripleDES)    key size must be equal to 112 or 168 
 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
 * RC2                  key size must be between 40 and 1024 bits 
 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits 
 * 具体内容 需要关注 JDK Document http://.../docs/technotes/guides/security/SunProviders.html 
 * </pre> 
 *  
 * @version 1.0 
 * @since 1.0 
 */  
public class DESCoder extends Coder {  
    /** 
     * ALGORITHM 算法 <br> 
     * 可替换为以下任意一种算法，同时key值的size相应改变。 
     *  
     * <pre> 
     * DES                  key size must be equal to 56 
     * DESede(TripleDES)    key size must be equal to 112 or 168 
     * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
     * RC2                  key size must be between 40 and 1024 bits 
     * RC4(ARCFOUR)         key size must be between 40 and 1024 bits 
     * </pre> 
     *  
     * 在Key toKey(byte[] key)方法中使用下述代码 
     * <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> 替换 
     * <code> 
     * DESKeySpec dks = new DESKeySpec(key); 
     * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM); 
     * SecretKey secretKey = keyFactory.generateSecret(dks); 
     * </code> 
     */  
	private static final String ALGORITHM = "DES";  
	
	private static String key;
  
    /** 
     * 转换密钥<br> 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */  
    private static Key toKey(byte[] key) throws Exception {  
        DESKeySpec dks = new DESKeySpec(key);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
        SecretKey secretKey = keyFactory.generateSecret(dks);  
  
        // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码  
        // SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);  
  
        return secretKey;  
    }  
  
    /** 
     * 解密 
     *  
     * @param data 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    private static byte[] decrypt(byte[] data, String key) throws Exception {  
        Key k = toKey(decryptBASE64(key));  
  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.DECRYPT_MODE, k);  
  
        return cipher.doFinal(data);  
    }  
  
    /** 
     * 加密 
     *  
     * @param data 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    private static byte[] encrypt(byte[] data, String key) throws Exception {  
        Key k = toKey(decryptBASE64(key));  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        cipher.init(Cipher.ENCRYPT_MODE, k);  
  
        return cipher.doFinal(data);  
    }  
  
    /** 
     * 生成密钥 
     *  
     * @param seed 
     * @return 
     * @throws Exception 
     */  
    private static String initKey(String seed) throws Exception {  
        SecureRandom secureRandom = null;  
  
        if (seed != null) {  
//           secureRandom = new SecureRandom(decryptBASE64(seed));  
        	 secureRandom = SecureRandom.getInstance("SHA1PRNG");  
        	 secureRandom.setSeed(decryptBASE64(seed));  
        } else {  
            secureRandom = new SecureRandom();  
        }
  
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);  
        kg.init(secureRandom);  
  
        SecretKey secretKey = kg.generateKey();  
  
        return encryptBASE64(secretKey.getEncoded());
    }  
    
    
    /** 
     * 生成密钥 
     *  
     * @return 
     * @throws Exception 
     */  
    public static void instance() throws Exception { 
    	String keys = JFig.getInstance().getValue("system_options", "DES_KEYS","UU8hICx$W:t:!#as,1QR$d?BFAG+4)q>@,<!OP|5qxY_HH}J@V,IRT9L/WR15v!=|L],j)mz-u+ca=D34Ku;,b<H)8(L<h@D?hll6,C#:(hASMrdaHP5Dl,?2gA[^JpS}b(sG$<,VX-29o$raPl9c|sz,k3jz;BZKzCvEV;5k,TO_+E>WKK6~WnM64,&B&VzEE]QNKSFpEX,cX?a(Y68ZEiXpFu8,H3=k<NHoI0HZLW5x,0_2(rt])I(?pdHd8,X!a}+?Tt>R@=)KP0,$!boC3AF/]e!ZTfW,T)g$FXIIqhRbr}s3,72:DkkUeq*v@Z9pH,61-Jqfem9#_]CCf2,kD@H#/Mc2M!C;:ir,!U~Gf?4Yax~gdt0:,0xoH:df]@?!fOS%9,W3R5Vc;$Px:leCOX,qCy<v$y~kyos20F-,!IHr=^hKtl{1@e|r,PZiKH%*AjP7eDqgc,[tU$-wlwG|-/I;y},]j@7&lEk6ba8AZ0b,!=2^Jsr$&2?<1=(|,bGie~[W5sID78p@Q,bGie~[W5sIrt8p@Q");
    	String[] keyArr = keys.split(",");
    	String day = getDateString(new Date(), "d");
        key = initKey(keyArr[Integer.parseInt(day)]);
    }
    
    /** 
     * 初始化 
     *  
     * @return 
     * @throws Exception 
     */  
    public static void instanceMobile() throws Exception { 
        key = "lI96USqnT4o";
    }
    
    
    public static String getDateString(Date date, String pattern)
    {
      SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
      return sdf.format(date);
    }
    
    /**
     * 加密数据
     * @param data
     * @return
     * @throws Exception
     */
    public static String encoder(String data) throws Exception{
    	if(StringUtils.isBlank(data)){
    		return "";
    	}else{
    		byte[] inputData = data.getBytes("UTF-8");
            inputData = DESCoder.encrypt(inputData, key);
            return DESCoder.encryptBASE64(inputData);
    	}
    }
    
    /**
     * 解密数据
     * @param data
     * @return
     * @throws Exception
     */
    public static String decoder(String data) throws Exception{
    	if(StringUtils.isBlank(data)){
    		return "";
    	}else{
    		byte[] outputData = DESCoder.decrypt(DESCoder.decryptBASE64(data), key);
    		return new String(outputData, "utf-8");
    	}
		 
    }
    
    /**
     * 将手机号中间四位变为*号
     * @param tel
     * @return
     */
    public static String getNewTel(String tel){
    	if(StringUtils.isNotBlank(tel)){
    		if(tel.contains("-") || tel.length() != 11 || tel.startsWith("0")){
    			if(! tel.contains("/")){
    				tel = tel.substring(0, tel.length()-4)+"****";
        		}else {
        			tel = tel.substring(0, tel.indexOf("/")-4)+"****"+tel.substring(tel.indexOf("/"), tel.length()-4)+"****";
    			}
    		}else {
    			tel = tel.substring(0,3)+"****"+tel.substring(7);
			}
    	}
    	return tel;
    }
    
    public static String getEncoderStr(String model) throws IOException{
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        int width = 180;  
        int height = 20;  
        String s = model;  
        Font font = new Font("Serif", Font.BOLD, 10);  
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        Graphics2D g2 = (Graphics2D)bi.getGraphics();  
        g2.setBackground(Color.WHITE);  
        g2.clearRect(0, 0, width, height);  
        g2.setPaint(Color.RED);  
                                                                                                                                                                       
        FontRenderContext context = g2.getFontRenderContext();  
        Rectangle2D bounds = font.getStringBounds(s, context);  
        double x = (width - bounds.getWidth()) / 2;  
        double y = (height - bounds.getHeight()) / 2;  
        double ascent = -bounds.getY();  
        double baseY = y + ascent;  
                                                                                                                                                                       
        g2.drawString(s, (int)x, (int)baseY);  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", bos);
        byte[] b = bos.toByteArray();
        String encoderStr = "data:image/jpg;base64,"+encoder.encodeBuffer(b);
        return encoderStr;
	}
    public static String getEncoderLeftStr(String model) throws IOException{
    	sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    	int width = 170;  
    	int height = 20;  
    	String s = model;  
//    	Font font = new Font("Serif", Font.BOLD, 10);
    	Font font = new Font("宋体", Font.PLAIN, 10);
    	BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
    	Graphics2D g2 = (Graphics2D)bi.getGraphics();  
    	g2.setBackground(Color.WHITE);  
    	g2.clearRect(0, 0, width, height);  
    	g2.setPaint(Color.RED);  
    	g2.setFont(new Font("宋体", Font.PLAIN, 12)); 
    	
    	FontRenderContext context = g2.getFontRenderContext();  
    	Rectangle2D bounds = font.getStringBounds(s, context);
    	double y = (height - bounds.getHeight()) / 2;  
    	double ascent = -bounds.getY();  
    	double baseY = y + ascent;  
    	
    	
    	g2.drawString(s, 0, (int)baseY);  
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	ImageIO.write(bi, "jpg", bos);
    	byte[] b = bos.toByteArray();
    	String encoderStr = "data:image/jpg;base64,"+encoder.encodeBuffer(b);
    	return encoderStr;
    }
    
    public static void main(String[] args) throws Exception{
    	DESCoder.instanceMobile();
    	System.out.println(decoder("+dMYbi8RW3q6lgHV52mJCQ=="));

    }
}  