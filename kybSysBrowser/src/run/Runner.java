package run;

import factories.WindowBrowserFactory;

public class Runner {

	public static void main(String[] args) {
		WindowBrowserFactory.INSTANCE.getWindow_Browser().initialize();
	}
}
