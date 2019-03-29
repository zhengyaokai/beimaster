package bmatser.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

public class FileTypeUtil {

	public static boolean isImage(InputStream in) {
		ImageInputStream image;
		try {
			image = ImageIO.createImageInputStream(in);
			Iterator<?> iter = ImageIO.getImageReaders(image);
			if (iter.hasNext()) {//文件不是图片 
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}
