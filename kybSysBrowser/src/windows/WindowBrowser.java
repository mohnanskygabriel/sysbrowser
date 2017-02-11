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

import models.BookmarksTreeModel;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import popups.PopUpFileNotFound;
import popups.PopUpInfo;
import entities.Bookmark;
import factories.DAOFactory;

public class WindowBrowser {

	/*
	 * TODO Tree prerobit na JTree a dat TreeModel pouzit JSON na ukladanie
	 * entit bookmark a pc VykomentovanÈ inötanËnÈ premennÈ zruöiù a robiù iba s
	 * tree v ostatn˝ch triedach getTree.getItems naprÌklad pouûÌvaù, miesto
	 * currentselection tieû pÌtaù tree selectedItem ak moûno tak aj mapu
	 * stringov na bookmarky uplne zruöiù a nahradiù modelom pre tree pre
	 * premenne ciest suborov vytvoriù entitu settings a
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
		addBookmarkButton.setText("Pridaù systÈm");

		final MenuItem addPCButton = new MenuItem(menu, SWT.PUSH);
		addPCButton.setText("Pridaù PC");

		MenuItem editItemButton = new MenuItem(menu, SWT.PUSH);
		editItemButton.setText("Upraviù poloûku");

		final MenuItem deleteItemButton = new MenuItem(menu, SWT.PUSH);
		deleteItemButton.setText("Odobraù poloûku");

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
		BookmarksTreeModel bookmarksTreeModel = new BookmarksTreeModel();
		JTree tree = new JTree(bookmarksTreeModel);
		Bookmark root = (Bookmark) bookmarksTreeModel.getRoot();
		System.out.println(root.getName());
		rootPane.getContentPane().add(tree, BorderLayout.CENTER);
		final Browser browser = new Browser(shell, SWT.NORMAL);
		GridData gd_browser = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_browser.widthHint = 600;
		gd_browser.heightHint = 540;
		browser.setLayoutData(gd_browser);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

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
		// "Nepodarilo sa spustiù Remote Desktop", "Chyba")
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
		// "Nepodarilo sa spustiù VNC", "Chyba").open();
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

		/*
		 * tree.addSelectionListener(new SelectionAdapter() { public void
		 * widgetSelected(SelectionEvent e) { TreeItem selected = (TreeItem)
		 * e.item; currentSelection = selected; if
		 * (bookmarkFromString.containsKey(selected.getText())) {
		 * addPCButton.setEnabled(true); deleteItemButton.setEnabled(true);
		 * remoteDesktopButton.setEnabled(false); vncButton.setEnabled(false);
		 * pingButton.setEnabled(false); if
		 * (bookmarkFromString.get(selected.getText()).getUrl()
		 * .equals("http://10.4.85.13/ml1_kis/")) { URL url = null; try { url =
		 * new URL("http://10.4.85.13/ml1_kis/"); } catch (MalformedURLException
		 * e1) { e1.printStackTrace(); } setSpecialTable(browser, url); } else
		 * if (bookmarkFromString.get(selected.getText())
		 * .getUrl().equals("http://10.4.85.160/tva_kis/")) { URL url = null;
		 * try { url = new URL("http://10.4.85.160/tva_kis/"); } catch
		 * (MalformedURLException e1) { e1.printStackTrace(); }
		 * setSpecialTable(browser, url); } else { if
		 * (bookmarkFromString.get(selected.getText()).getUrl() == null ||
		 * bookmarkFromString.get(selected.getText())
		 * .getUrl().trim().equals("http://") ||
		 * bookmarkFromString.get(selected.getText())
		 * .getUrl().trim().equals("")) { // browser.setVisible(false); } else {
		 * browser.setVisible(true); browser.setUrl(bookmarkFromString.get(
		 * selected.getText()).getUrl()); if
		 * (selected.getText().equals("ML3 KIS")) {
		 * System.out.println(browser.getText()); } } } } else {
		 * addPCButton.setEnabled(false); pingButton.setEnabled(true); for (PC
		 * item : pcFromTreeItem.values()) { item.getConnectionType(); }
		 * 
		 * if (pcFromTreeItem.get(selected).connectionType .equals("VNC")) {
		 * vncButton.setEnabled(true); remoteDesktopButton.setEnabled(false); }
		 * else { if (pcFromTreeItem.get(selected).connectionType .equals("RD"))
		 * { vncButton.setEnabled(false); remoteDesktopButton.setEnabled(true);
		 * } else { vncButton.setEnabled(false);
		 * remoteDesktopButton.setEnabled(false); } } } } });
		 */

		refreshVNCPath("");
		// refreshBookmarks();
		shell.open();
		shell.layout();
		/*
		 * for (TreeItem treeItem : tree.getItems()) {
		 * treeItem.setExpanded(false); }
		 */
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

	/*
	 * public void refreshBookmarks() { // po vymazanÌ tree currentSelection
	 * neexistuje if (currentSelection != null) { if
	 * (!currentSelection.isDisposed() && currentSelection.getParentItem() !=
	 * null) { if (!currentSelection.getParentItem().isDisposed()) {
	 * lastSelectedBookmarkName = currentSelection.getParentItem() .getText(); }
	 * } else { if (!currentSelection.isDisposed()) lastSelectedBookmarkName =
	 * currentSelection.getText(); } }
	 */

	// funkcia pre naËÌtanie s˙boru fileOfBookmarks
	/*
	 * getBookmarksAndPCFromFile(); pcFromTreeItem = new HashMap<TreeItem,
	 * PC>(); LinkedList<String> sortedBookmarks = new LinkedList<String>(
	 * bookmarkFromString.keySet()); Collections.sort(sortedBookmarks);
	 * bookmarkToTreeItem = new HashMap<String, TreeItem>(); // maûem strom a
	 * vytv·ram nov˝ tree.clearAll(true); tree.setItemCount(0); TreeItem newItem
	 * = null;
	 * 
	 * for (String bookmarkName : sortedBookmarks) { newItem =
	 * makeNewTreeItem(tree, bookmarkName); bookmarkToTreeItem.put(bookmarkName,
	 * newItem); tree.showItem(newItem); }
	 * 
	 * for (String bookmark : sortedBookmarks) { List<PC> listOfComputers =
	 * bookmarkFromString.get(bookmark) .getComputerList(); for (PC pc :
	 * listOfComputers) { TreeItem newTreeItem = new TreeItem(
	 * bookmarkToTreeItem.get(bookmark), SWT.NONE);
	 * newTreeItem.setText(pc.getName()); pcFromTreeItem.put(newTreeItem, pc);
	 * tree.showItem(newTreeItem); } } // nastavÌm posledn˙ vybran˙ hodnotu, ak
	 * neexistuje jeho parent/topItem if (currentSelection != null) { Event ev =
	 * new Event(); if
	 * (bookmarkToTreeItem.containsKey(lastSelectedBookmarkName)) {
	 * tree.setSelection(bookmarkToTreeItem .get(lastSelectedBookmarkName));
	 * ev.item = bookmarkToTreeItem.get(lastSelectedBookmarkName); } else { if
	 * (tree.getTopItem() != null) { tree.setSelection(tree.getTopItem());
	 * ev.item = tree.getTopItem(); }
	 * 
	 * } // informujem listenerov stromu if (tree.getTopItem() != null)
	 * tree.notifyListeners(SWT.Selection, ev); } for (TreeItem treeItem :
	 * tree.getItems()) { treeItem.setExpanded(false); } // currentSelection =
	 * null; }
	 */

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
							"Nepodarilo sa otvoriù nov˝ s˙bor, po ukonËenÌ programu vymaû s˙bor "
									+ fileOfVNCPath + " a reötartni program",
							"ProblÈm so s˙borom");
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

	/*
	 * public TreeItem getCurrentSelection() { if (tree != null &&
	 * tree.getSelection() != null && tree.getSelection().length > 0) return
	 * tree.getSelection()[0];
	 * 
	 * return null; }
	 */

	public File getFileOfBookmarks() {
		return fileOfBookmarks;
	}

	// public Map<TreeItem, PC> getPcFromTreeItem() {
	// return pcFromTreeItem;
	// }
	//
	// public void setPcFromTreeItem(Map<TreeItem, PC> pcFromTreeItem) {
	// this.pcFromTreeItem = pcFromTreeItem;
	// }

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
