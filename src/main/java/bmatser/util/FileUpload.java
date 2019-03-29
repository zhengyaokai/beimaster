package bmatser.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class FileUpload {
	
	/**
	 * 上传文件到自定义路径
	 * @param request
	 * @param filePathpath
	 * @return 文件路径
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String getUpload(HttpServletRequest request,StringBuffer filePath) throws IllegalStateException, IOException{
		String path = null;
    	/**创建一个通用的多部分解析器*/  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        /**判断 request 是否有文件上传,即多部分请求 */ 
        if(multipartResolver.isMultipart(request)){  
            /**转换成多部分request*/
        	MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            /**取得request中的所有文件名*/  
            Iterator<String> iter = multiRequest.getFileNames();  
            while(iter.hasNext()){  
                /**记录上传过程起始时的时间，用来计算上传时间*/  
                int pre = (int) System.currentTimeMillis();  
                /**取得上传文件*/
                MultipartFile file = multiRequest.getFile(iter.next());  
                if(file != null){  
                    /**取得当前上传文件的文件名称*/
                    String myFileName = file.getOriginalFilename();  
                    /**如果名称不为“”,说明该文件存在，否则说明该文件不存在*/
                    if( !"".equals(myFileName.trim())){  
                    	myFileName = myFileName.substring(myFileName.lastIndexOf("."));
                        /**重命名上传后的文件名*/
                        String fileName = PaymentIDProduce.getUUID();
                        /**定义上传路径*/
                        path = fileName+myFileName;
                        filePath = filePath.append(path);
                        File localFile = new File(filePath.toString());  
                        if(!localFile.exists()){
                        	localFile.mkdirs();
                        }
						file.transferTo(localFile);
                    }  
                }  
                /**记录上传该文件后的时间*/
                int finaltime = (int) System.currentTimeMillis();  
                System.out.println(finaltime - pre);
            }  
        }
		return path;
	}
	/**
	 * 上传文件到自定义路径
	 * @param file
	 * @param filePath
	 * @return 文件路径
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String getUpload(MultipartFile file,StringBuffer toPath) throws Exception{
		StringBuffer filePath = new StringBuffer(toPath.toString());
		String path = null;
		String myFileName = file.getOriginalFilename();
		if(myFileName.lastIndexOf(".")>=0 && myFileName !=null){
			myFileName = myFileName.substring(myFileName.lastIndexOf("."));
		}else{
			myFileName=".jpg";
		}
    			/**重命名上传后的文件名*/
    			String fileName = PaymentIDProduce.getUUID();
    			/**定义上传路径*/
    			path = "/"+fileName+myFileName;
    			filePath = filePath.append(path);
    			File myFile = new File(filePath.toString());
    			if(!myFile.exists()){
    				myFile.mkdirs();
    			}
    			try {
    	            BufferedImage image = ImageIO.read(file.getInputStream());
    	            if (image != null) {
    	            	file.transferTo(myFile);
    	            	return path;
    	            }else{
    	            	throw new RuntimeException("图片不存在");
    	            }
    	        } catch (IOException e) {
    	        	e.printStackTrace();
    	        	throw new RuntimeException("图片保存失败");
    	        }
	}

    /**
     * 上传文件到自定义路径
     * @param file
     * @param filePath
     * @return 文件路径
     * @throws IllegalStateException
     * @throws IOException
     */
    public static String getUploadForIos(MultipartFile file,StringBuffer toPath) throws Exception{
        StringBuffer filePath = new StringBuffer(toPath.toString());
        String path = null;
        String myFileName = file.getOriginalFilename();
        if(myFileName.lastIndexOf(".")>=0 && myFileName !=null){
            myFileName = myFileName.substring(myFileName.lastIndexOf("."));
        }else{
            myFileName=".jpg";
        }
        /**重命名上传后的文件名*/
        String fileName = PaymentIDProduce.getUUID();
        /**定义上传路径*/
        path = "/"+fileName+myFileName;
        filePath = filePath.append(path);
        File myFile = new File(filePath.toString());
//        if(!myFile.exists()){
//            myFile.mkdirs();
//        }
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image != null) {
                file.transferTo(myFile);
                return path;
            }else{
                throw new RuntimeException("图片不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片保存失败");
        }
    }
}
