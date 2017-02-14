package dao.implementations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import windows.WindowBrowser;
import dao.interfaces.PCDAO;
import entities.Bookmark;
import entities.PC;
import factories.WindowBrowserFactory;

public class FilePCDAO implements PCDAO {
	private WindowBrowser browser = WindowBrowserFactory.INSTANCE
			.getWindow_Browser();
	private final File fileOfBookmarks = browser.getFileOfBookmarks();

	@Override
	public void insertPC(PC pc) {

	}

	@Override
	public void deletePC(PC pc) throws IOException {
		
	}

	@Override
	public void editPC(PC pcOld, PC pcNew) {
		
	}

	@Override
	public PC getAllPCOfBookmark(Bookmark bookmark) {
		
		return null;
	}

}
