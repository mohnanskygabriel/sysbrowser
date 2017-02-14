package windows;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JRootPane;
import javax.swing.JTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import popups.PopUpFileNotFound;
import popups.PopUpInfo;
import entities.Bookmark;
import factories.DAOFactory;
import factories.ModelFactory;

public class WindowBrowser {

	/*
	 * TODO Tree prerobit na JTree a dat TreeModel pouzit JSON na ukladanie
	 * entit bookmark a pc Vykomentovan� in�tan�n� premenn� zru�i� a robi� iba s
	 * tree v ostatn�ch triedach getTree.getItems napr�klad pou��va�, miesto
	 * currentselection tie� p�ta� tree selectedItem ak mo�no tak aj mapu
	 * stringov na bookmarky uplne zru�i� a nahradi� modelom pre tree pre
	 * premenne ciest suborov vytvori� entitu settings a
	 */
	private File fileOfBookmarks = new File("src/bookmarks.txt");
	private File fileOfVNCPath = new File("src/vncPath.txt");
	private String vncPath = "";
	private List<Bookmark> bookmarks = new LinkedList<Bookmark>();

	private Shell shell = null;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void initialize() {
		Display display = Display.getDefault();
		this.shell = new Shell(display);
		GridLayout gl_shell = new GridLayout();
		gl_shell.numColumns = 2;
		shell.setLayout(gl_shell);
		shell.setSize(800, 600);
		Rectangle parentBounds = display.getBounds();
		shell.setLocation(parentBounds.width / 2 - shell.getBounds().width / 2,
				parentBounds.height / 2 - shell.getBounds().height / 2);
		shell.setText("KybSys browser");

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		final MenuItem addBookmarkButton = new MenuItem(menu, SWT.PUSH);
		addBookmarkButton.setText("Prida� syst�m");

		final MenuItem addPCButton = new MenuItem(menu, SWT.PUSH);
		addPCButton.setText("Prida� PC");

		MenuItem editItemButton = new MenuItem(menu, SWT.PUSH);
		editItemButton.setText("Upravi� polo�ku");

		final MenuItem deleteItemButton = new MenuItem(menu, SWT.PUSH);
		deleteItemButton.setText("Odobra� polo�ku");

		final MenuItem pingButton = new MenuItem(menu, SWT.PUSH);
		pingButton.setText("PING");

		final MenuItem remoteDesktopButton = new MenuItem(menu, SWT.PUSH);
		remoteDesktopButton.setText("Remote Desktop");

		final MenuItem vncButton = new MenuItem(menu, SWT.PUSH);
		vncButton.setText("VNC");

		MenuItem settingButton = new MenuItem(menu, SWT.NONE);
		settingButton.setText("Nastavenia programu");

		Composite composite = new Composite(shell, SWT.EMBEDDED);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1);
		gd_composite.widthHint = 150;
		composite.setLayoutData(gd_composite);
		Frame frame = SWT_AWT.new_Frame(composite);
		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);

		JTree tree = new JTree(ModelFactory.INSTANCE.getBookmarkTreeModel());
		tree.setRootVisible(false);
		rootPane.getContentPane().add(tree, BorderLayout.CENTER);
		try {
			DAOFactory.INSTANCE.getBookmarkDao().deleteBookmark(
					DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll()
							.get(0));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		final Browser browser = new Browser(shell, SWT.NORMAL);
		GridData gd_browser = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_browser.widthHint = 600;
		gd_browser.heightHint = 540;
		browser.setLayoutData(gd_browser);

		addBookmarkButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				WindowAddBookmark formAddBookmark = new WindowAddBookmark(
						shell, SWT.DIALOG_TRIM, false);
				formAddBookmark.open();
				shell.setEnabled(true);
				bookmarks = DAOFactory.INSTANCE.getBookmarkDao()
						.getBookmarkAll();
				for (Bookmark bookmark : bookmarks) {
					System.out.println(bookmark.getName());
					System.out.println(bookmark.getUrl());
				}
			}
		});

		// addPCButton.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// shell.setEnabled(false);
		// WindowAddPC addPCDialog = new WindowAddPC(addPCButton
		// .getParent().getShell(), SWT.DIALOG_TRIM,
		// currentSelection, false, null);
		// addPCDialog.open();
		// shell.setEnabled(true);
		// // refreshBookmarks();
		// }
		// });
		//
		// editItemButton.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent event) {
		// shell.setEnabled(false);
		// if (pcFromTreeItem.containsKey(currentSelection)) {
		// WindowAddPC addPCDialog = new WindowAddPC(addPCButton
		// .getParent().getShell(), SWT.DIALOG_TRIM,
		// currentSelection.getParentItem(), true,
		// pcFromTreeItem.get(currentSelection));
		// addPCDialog.open();
		// } else {
		// WindowAddBookmark formAddBookmark = new WindowAddBookmark(
		// shell, SWT.DIALOG_TRIM, true);
		// formAddBookmark.open();
		// }
		// shell.setEnabled(true);
		// // refreshBookmarks();
		// }
		// });
		//
		// deleteItemButton.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// shell.setEnabled(false);
		// PopUpDeleteItemFromTreeDialog deleteItemDialog = new
		// PopUpDeleteItemFromTreeDialog(
		// shell, SWT.DIALOG_TRIM);
		// deleteItemDialog.open();
		// shell.setEnabled(true);
		// // refreshBookmarks();
		// }
		// });
		//
		// pingButton.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// // System.out.println(ping(pcFromTreeItem.get(currentSelection).ip));
		// shell.setEnabled(false);
		// PopUpPingOutput popUpSelectPingType = new PopUpPingOutput(
		// shell, SWT.DIALOG_TRIM, pcFromTreeItem
		// .get(currentSelection).ip);
		// popUpSelectPingType.open();
		// shell.setEnabled(true);
		// }
		// });
		//
		// remoteDesktopButton.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent arg0) {
		// try {
		// String[] cmd = {
		// "cmd",
		// "/c",
		// "%windir%/system32/mstsc.exe /v:"
		// + pcFromTreeItem.get(currentSelection)
		// .getIp() + " /admin" };
		// Runtime.getRuntime().exec(cmd);
		// } catch (IOException e) {
		// new PopUpInfo(shell, SWT.DIALOG_TRIM,
		// "Nepodarilo sa spusti� Remote Desktop", "Chyba")
		// .open();
		// }
		// }
		// });

		// vncButton.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent arg0) {
		// try {
		// Runtime.getRuntime().exec(
		// vncPath
		// + " "
		// + pcFromTreeItem.get(currentSelection)
		// .getIp());
		// } catch (IOException e) {
		// new PopUpInfo(shell, SWT.DIALOG_TRIM,
		// "Nepodarilo sa spusti� VNC", "Chyba").open();
		// }
		// }
		// });
		//
		// settingButton.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// shell.setEnabled(false);
		// WindowSettings settingsWindow = new WindowSettings(shell,
		// SWT.TITLE);
		// settingsWindow.open();
		// shell.setEnabled(true);
		// }
		// });

		refreshVNCPath("");
		// refreshBookmarks();
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (bookmarks.isEmpty()) {
				addPCButton.setEnabled(false);
				deleteItemButton.setEnabled(false);
				editItemButton.setEnabled(false);
				pingButton.setEnabled(false);
				vncButton.setEnabled(false);
				remoteDesktopButton.setEnabled(false);
			} else {
				editItemButton.setEnabled(true);
			}
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void setSpecialTable(Browser browser, URL url) {
		StringBuilder htmlBuileder = new StringBuilder();
		try {

			URLConnection con = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String l;
			while ((l = in.readLine()) != null) {
				htmlBuileder.append(l + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String html = htmlBuileder.toString();
		int beginningOfTable = html.indexOf("div_datagrid");
		int endOfTable = html.indexOf("dg_pager");
		if (beginningOfTable < 0 && endOfTable < 0)
			return;
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<body>");
		sb.append(html.substring(beginningOfTable - 9, endOfTable + 103));
		sb.append("</body>");
		sb.append("</html>");
		browser.setText(sb.toString());
	}

	private TreeItem makeNewTreeItem(Tree parent, String name) {
		TreeItem newTreeItem = new TreeItem(parent, SWT.NONE);
		newTreeItem.setText(name);
		return newTreeItem;
	}

	String ping(String ip) {
		String pingResult = "";
		String pingCmd = "ping " + ip;
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				// System.out.println(inputLine);
				pingResult += inputLine;
				pingResult += "\n";
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		return pingResult;
	}

	void refreshVNCPath(String path) {
		Scanner sc = null;
		PrintWriter pw = null;
		try {
			if (path.equals("")) {
				sc = new Scanner(fileOfVNCPath);
				vncPath = sc.next();
			} else {
				pw = new PrintWriter(fileOfVNCPath);
				pw.write(path);
				vncPath = path;
			}
		} catch (FileNotFoundException fnfex) {
			PopUpFileNotFound popupFileNotFound = new PopUpFileNotFound(shell,
					SWT.DIALOG_TRIM, fileOfVNCPath.getAbsolutePath());
			popupFileNotFound.open();

			File newVNCPathFile = new File(fileOfVNCPath.getAbsolutePath());
			File srcFile = new File("src");
			srcFile.mkdir();

			try {
				newVNCPathFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (Exception ex) {
			File newBookmarksFile = new File(fileOfVNCPath.getAbsolutePath());
			File srcFile = new File("src");
			srcFile.mkdir();
			boolean deleted = false;
			for (int i = 0; i < 20; i++) {
				deleted = fileOfVNCPath.delete();
				if (deleted)
					break;
				System.gc();
				Thread.yield();
			}
			boolean renamed = false;
			if (deleted) {
				for (int i = 0; i < 20; i++) {
					renamed = newBookmarksFile.renameTo(fileOfVNCPath);
					if (renamed)
						break;
					System.gc();
					Thread.yield();
				}
				try {
					newBookmarksFile.createNewFile();
				} catch (IOException e) {
					PopUpInfo popupFileProblemNeedExit = new PopUpInfo(shell,
							SWT.DIALOG_TRIM,
							"Nepodarilo sa otvori� nov� s�bor, po ukon�en� programu vyma� s�bor "
									+ fileOfVNCPath + " a re�tartni program",
							"Probl�m so s�borom");
					popupFileProblemNeedExit.open();
					System.gc();
					Thread.yield();
					System.exit(0);
				}
			}
		} finally {
			if (sc != null)
				sc.close();
			if (pw != null)
				pw.close();
		}
	}

	public File getFileOfBookmarks() {
		return fileOfBookmarks;
	}

	public Shell getShell() {
		return shell;
	}

	public String getVncPath() {
		return vncPath;
	}

	public void setVncPath(String vncPath) {
		this.vncPath = vncPath;
	}

}
