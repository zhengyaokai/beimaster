package bmatser.util;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigConstants;
import org.igfay.jfig.JFigException;
import org.igfay.jfig.JFigLocator;

public class AppContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent arg0) {
		String f = arg0.getServletContext().getInitParameter("config-file");
		if (f == null) {
			f = "WEB-INF/classes/application.config.xml";
		}
		f = arg0.getServletContext().getRealPath(f);
		try {
			File file = new File(f);
			if (!file.exists())
				throw new JFigException(String.format(
						"Configuration file %s not found", f));
			int i = f.lastIndexOf('/');
			if (i == -1)
				i = f.lastIndexOf('\\');
			if (i <= 0 || (i == f.length() - 1)) {
				throw new JFigException(String.format(
						"Invalid Configuration file %s", f));
			}
			String config = f.substring(i + 1);
			String configDir = f.substring(0, i);
			configDir += File.separator;
			JFigLocator loc = new JFigLocator(config);
			loc.setConfigDirectory(configDir);
			loc.setConfigLocation(JFigConstants.LOC_FILE);
			JFig.initialize(loc);
		} catch (JFigException e) {
			e.printStackTrace();
		}
	}

}
