package kybsysbrowser.run;

import kybsysbrowser.factory.WindowFactory;

public class Runner {

	public static void main(String[] args) {
		WindowFactory.INSTANCE.getWindow_Browser().initialize();

	}
}
