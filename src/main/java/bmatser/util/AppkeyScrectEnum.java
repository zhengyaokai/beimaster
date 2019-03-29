package bmatser.util;

public enum AppkeyScrectEnum {
	HEJI("heji-6f46faa8-6661-4027-864e-3f1fef071788","5a0079fb-d6a2-4ef6-a972-89b559296c3a"),
	GDB("hiy11231","123asef");
	private String appKey;
    private String appScrect;
	private AppkeyScrectEnum(String appKey,String appScrect){
		this.appKey=appKey;
		this.appScrect=appScrect;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppScrect() {
		return appScrect;
	}
	public void setAppScrect(String appScrect) {
		this.appScrect = appScrect;
	}
	
	
	
}
