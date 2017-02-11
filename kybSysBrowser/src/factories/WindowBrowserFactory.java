package factories;

import windows.WindowBrowser;

public enum WindowBrowserFactory {
	INSTANCE;
	WindowBrowser windowBrowser;

	public WindowBrowser getWindow_Browser() {
		if (windowBrowser != null)
			return windowBrowser;

		return windowBrowser = new WindowBrowser();
	}
}
