package factories;

import windows.WindowBrowser;

public enum WindowBrowserFactory {
	INSTANCE;
	WindowBrowser windowBrowser;

	public WindowBrowser getWindow_Browser() {
		if (windowBrowser == null)
			windowBrowser = new WindowBrowser();
		return windowBrowser;
	}
}
