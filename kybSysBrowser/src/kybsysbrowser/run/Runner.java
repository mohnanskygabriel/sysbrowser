package kybsysbrowser.run;

import kybsysbrowser.factory.WindowBrowserFactory;

public class Runner {

	public static void main(String[] args) {
		WindowBrowserFactory.INSTANCE.getWindow_Browser().initialize();
	}
}
