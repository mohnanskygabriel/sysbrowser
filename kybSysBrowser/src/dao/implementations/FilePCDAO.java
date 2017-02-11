package dao.implementations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.eclipse.swt.widgets.TreeItem;
import windows.WindowBrowser;
import dao.interfaces.PCDAO;
import entities.Bookmark;
import entities.PC;
import factories.WindowBrowserFactory;

public class FilePCDAO implements PCDAO {
	private WindowBrowser browser = WindowBrowserFactory.INSTANCE
			.getWindow_Browser();
	private final File fileOfBookmarks = browser.getFileOfBookmarks();
	//TreeItem selectedTreeItem = browser.getCurrentSelection();

	@Override
	public void insertPC(PC pc) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deletePC(PC pc) throws IOException {
		Scanner sc = null;
		Scanner scLine = null;
		FileWriter fw = null;
		File copyOfBookmarks = new File("newFileOfBookmarks.txt");
		try {
			sc = new Scanner(fileOfBookmarks);
			fw = new FileWriter(copyOfBookmarks);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				scLine = new Scanner(line);
				scLine.useDelimiter("\t");
				String pcName = scLine.next();
			/*	if (!pcName.equals(selectedTreeItem.getText())) {
					fw.write(line + "\n");
				} else {
					continue;
				}*/
			}
			if (fw != null)
				fw.close();
			if (sc != null)
				sc.close();
			if (scLine != null)
				scLine.close();
			boolean deleted = false;
			for (int i = 0; i < 20; i++) {
				deleted = fileOfBookmarks.delete();
				if (deleted)
					break;
				System.gc();
				Thread.yield();
			}
			boolean renamed = false;
			if (deleted) {
				for (int i = 0; i < 20; i++) {
					renamed = copyOfBookmarks.renameTo(fileOfBookmarks);
					if (renamed)
						break;
					System.gc();
					Thread.yield();
				}
				if (!renamed) {
					throw new IOException();
				}
			} else {
				throw new IOException();
			}
		} finally {

		}
	}

	@Override
	public void editPC(PC pcOld, PC pcNew) {
		// TODO Auto-generated method stub

	}

	@Override
	public PC getAllPCOfBookmark(Bookmark bookmark) {
		// TODO Auto-generated method stub
		return null;
	}

}
