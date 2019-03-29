package bmatser.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.web.multipart.MultipartFile;

public class CachedImage {
	
	 private static byte data[];
	 
	 public static void insertImage(MemcachedClient memcachedClient, MultipartFile file, String key) throws Exception{
		  InputStream input=file.getInputStream();
		  data=new byte[1024];
		  List<byte[]> list =new ArrayList<byte[]>();
		  int length=-1;
		  while((length=input.read(data))!=-1){
		   if(length<1024){
		    byte data2[]=new byte[length];
		    for(int i=0;i<length;i++){
		     data2[i]=data[i];
		    }
		    data=data2;
		   }
		   byte data3[]=new byte[length];
		   for(int i=0;i<data.length;i++){
		    data3[i]=data[i];
		   }
		   list.add(data3);
		  }

		  memcachedClient.delete(key);
		  memcachedClient.add(key, 60, list);

		 }
	 	public static void selectImage(HttpServletRequest request, HttpServletResponse response,MemcachedClient memcachedClient,String key)
			   throws Exception {
			  response.setContentType("image/*");
			   OutputStream out=response.getOutputStream();
			  List<byte[]>list=(List<byte[]>) memcachedClient.get(key);
			  
			  for(int i=0;i<list.size();i++){
			   byte data[]=list.get(i);
			   out.write(data, 0, data.length);
			  }
			  out.flush();
			  out.close();
		}
}
