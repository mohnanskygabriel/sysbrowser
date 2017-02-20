package kybsysbrowser.factory;

import kybsysbrowser.frame.MainFrame;

public enum WindowFactory {
	INSTANCE;
	MainFrame windowBrowser;

	public MainFrame getWindow_Browser() {
		if (windowBrowser == null)
			windowBrowser = new MainFrame();
		return windowBrowser;
	}
}
