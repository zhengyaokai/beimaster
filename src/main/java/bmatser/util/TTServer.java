package bmatser.util;

import java.io.IOException;

import org.igfay.jfig.JFig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.tools.tree.ThisExpression;

import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class TTServer {
	
	private final static Logger logger = LoggerFactory.getLogger(TTServer.class);
	static MemcachedClient client;
	static String ttServer = JFig.getInstance().getValue("system_options", "tt_server", "192.168.1.211:1978");
	static String ttServerId = JFig.getInstance().getValue("system_options", "tt_server_id", "");
	
	static{
		
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                AddrUtil.getAddresses(ttServer));
		try {
			client = builder.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据key获取递增数
	 * @param key
	 * @return
	 */
	public static String getIncreasedId(String key){
		try {
			if(client == null){
				MemcachedClientBuilder builder = new XMemcachedClientBuilder(
		                AddrUtil.getAddresses(ttServer));
				client = builder.build();
			}
			Counter counter=client.getCounter(key, 0);
		
			return String.valueOf(counter.incrementAndGet())+ttServerId;
		} catch (Exception e) {
			logger.error("getIncreasedId fail:{}", e.getMessage());
		} 
		return null;
	}
	
	public static void main(String[] args){
		System.out.print(getIncreasedId("20151224"));
	}
}
