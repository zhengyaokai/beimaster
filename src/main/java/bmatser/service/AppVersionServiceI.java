package bmatser.service;

import java.util.Map;

public interface AppVersionServiceI {

	Map getAppInfo(String type, String version, String channel, String build);

	Map getMappAppInfo(String type, String version, String channel, String build);

}
