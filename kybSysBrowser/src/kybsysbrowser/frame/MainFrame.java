package kybsysbrowser.frame;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.DebugGraphics;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import kybsysbrowser.dialog.AddBookmarkDialog;
import kybsysbrowser.dialog.AddPCDialog;
import kybsysbrowser.dialog.DeleteItemFromTreeDialog;
import kybsysbrowser.dialog.SettingsDialog;
import kybsysbrowser.dialog.exceptionSolving.InfoDialog;
import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.ModelFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

public class MainFrame {

	/*
	 * TODO: vytvorit triedu ktora bude uchovavat vsetky riesenia exceptionov a
	 * na riesenie exceptionov uz iba volat z tejto triedy dane riesenie vynimky
	 * vyriesit stranky ako TSP-KIS ktore bezia na starom IE iba squele
	 */

	private JTree tree;
	private Browser browser;
	protected Shell shell = null;

	/**
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void initialize() {
		Display display = Display.getDefault();
		this.shell = new Shell(display);
		shell.setSize(800, 600);
		Rectangle parentBounds = display.getBounds();
		shell.setLocation(parentBounds.width / 2 - shell.getBounds().width / 2,
				parentBounds.height / 2 - shell.getBounds().height / 2);
		shell.setText("KybSys browser");
		shell.setLayout(new FormLayout());

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
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(0, 167);
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.top = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		Frame frame = SWT_AWT.new_Frame(composite);
		frame.setResizable(false);
		tree = new JTree(ModelFactory.INSTANCE.getBookmarkTreeModel());
		frame.add(tree, BorderLayout.CENTER);
		tree.setAutoscrolls(true);
		tree.setRootVisible(false);

		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
		scrollPane.setBorder(null);
		scrollPane.setAutoscrolls(true);
		scrollPane.setDoubleBuffered(true);
		frame.add(scrollPane, BorderLayout.CENTER);

		browser = new Browser(shell, SWT.NORMAL);
		FormData fd_browser = new FormData();
		fd_browser.bottom = new FormAttachment(100, -10);
		fd_browser.right = new FormAttachment(100, -10);
		fd_browser.left = new FormAttachment(composite, 6);
		browser.setLayoutData(fd_browser);
		

		Button backButton = new Button(shell, SWT.NONE);
		fd_browser.top = new FormAttachment(0, 29);
		backButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				browser.back();
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(browser, -6);
		backButton.setLayoutData(fd_btnNewButton);
		backButton.setText("Sp‰ù");

		Button btnOpenInBrowser = new Button(shell, SWT.NONE);
		btnOpenInBrowser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String url = null;
				if (tree.getLastSelectedPathComponent().getClass() == Bookmark.class)
					url = ((Bookmark) tree.getLastSelectedPathComponent())
							.getUrl();
				else
					url = ((Bookmark) ((TreeNode) tree
							.getLastSelectedPathComponent()).getParent())
							.getUrl();

				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					try {
						if (url != null)
							desktop.browse(new URI(url));
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		FormData fd_btnO = new FormData();
		fd_btnO.top = new FormAttachment(composite, 0, SWT.TOP);
		fd_btnO.right = new FormAttachment(browser, 0, SWT.RIGHT);
		btnOpenInBrowser.setLayoutData(fd_btnO);
		btnOpenInBrowser.setText("Otvoriù v prehliadaËi");

		Button btnBrowserForward = new Button(shell, SWT.NONE);
		fd_btnNewButton.right = new FormAttachment(btnBrowserForward, -6);
		btnBrowserForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				browser.forward();
			}
		});
		FormData fd_btnNewButton1 = new FormData();
		fd_btnNewButton1.bottom = new FormAttachment(browser, -6);
		fd_btnNewButton1.right = new FormAttachment(btnOpenInBrowser, -6);
		btnBrowserForward.setLayoutData(fd_btnNewButton1);
		btnBrowserForward.setText("Dopredu");

		Button btnRefresh = new Button(shell, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				browser.refresh();
			}
		});
		FormData fd_btnRefresh = new FormData();
		fd_btnRefresh.bottom = new FormAttachment(browser, -6);
		fd_btnRefresh.right = new FormAttachment(backButton, -6);
		btnRefresh.setLayoutData(fd_btnRefresh);
		btnRefresh.setText("Refresh");

		Label browserStatus = new Label(shell, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.right = new FormAttachment(btnRefresh, -6);
		fd_lblNewLabel.bottom = new FormAttachment(browser, -6);
		fd_lblNewLabel.left = new FormAttachment(composite, 6);
		browserStatus.setLayoutData(fd_lblNewLabel);
		browserStatus.setText("");

		new Thread(new Runnable() {
			public void run() {
				while (!shell.isDisposed()) {
					try {
						Thread.sleep(1000);

					} catch (Exception e) {
						e.printStackTrace();
					}
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							// refresh browser if is bookmark selected & new url
							// is not part of selected bookmark url
							if (tree.getLastSelectedPathComponent() != null
									&& tree.getLastSelectedPathComponent()
											.getClass() == Bookmark.class
									&& browser
											.getUrl()
											.indexOf(
													((Bookmark) tree
															.getLastSelectedPathComponent())
															.getUrl()) == -1) {
								browserStatus.setText("NaËÌt·vam...");
								browser.setUrl(((Bookmark) tree
										.getLastSelectedPathComponent())
										.getUrl());
							}

						}
					});
				}
			}
		}).start();

		browser.addProgressListener(new ProgressListener() {

			@Override
			public void completed(ProgressEvent arg0) {
				browserStatus.setText("NaËÌtanÈ");
			}

			@Override
			public void changed(ProgressEvent arg0) {
				if (browserStatus.getText().equals("NaËÌt·vam..."))
					browserStatus.setText("NaËÌt·vam...");
			}
		});

		addBookmarkButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				AddBookmarkDialog formAddBookmark = new AddBookmarkDialog(
						shell, SWT.DIALOG_TRIM, false);
				formAddBookmark.open();
				ModelFactory.INSTANCE.getBookmarkTreeModel().reload();
				shell.setEnabled(true);
			}
		});

		addPCButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				AddPCDialog addPCDialog = new AddPCDialog(shell,
						SWT.DIALOG_TRIM, false);
				addPCDialog.open();
				ModelFactory.INSTANCE.getBookmarkTreeModel()
						.nodeStructureChanged(
								(TreeNode) tree.getLastSelectedPathComponent());
				shell.setEnabled(true);
			}
		});

		editItemButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				shell.setEnabled(false);
				char[] treeState = getTreeExpansionState();
				if (tree.getLastSelectedPathComponent().getClass() == PC.class) {
					AddPCDialog addPCDialog = new AddPCDialog(addPCButton
							.getParent().getShell(), SWT.DIALOG_TRIM, true);
					addPCDialog.open();
				} else {
					AddBookmarkDialog formAddBookmark = new AddBookmarkDialog(
							shell, SWT.DIALOG_TRIM, true);
					formAddBookmark.open();
				}
				ModelFactory.INSTANCE.getBookmarkTreeModel().reload();
				setTreeExpansionState(treeState);
				shell.setEnabled(true);
			}
		});

		deleteItemButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				DeleteItemFromTreeDialog deleteItemDialog = new DeleteItemFromTreeDialog(
						shell, SWT.DIALOG_TRIM);
				char[] treeState = getTreeExpansionState();
				deleteItemDialog.open();
				ModelFactory.INSTANCE.getBookmarkTreeModel().reload();
				setTreeExpansionState(treeState);
				shell.setEnabled(true);
			}
		});

		pingButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// System.out.println(ping(pcFromTreeItem.get(currentSelection).ip));
				shell.setEnabled(false);
				PingOutputFrame popUpSelectPingType = new PingOutputFrame(
						shell, SWT.DIALOG_TRIM, ((PC) tree
								.getLastSelectedPathComponent()).getIp());
				popUpSelectPingType.open();
				shell.setEnabled(true);
			}
		});

		remoteDesktopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					String[] cmd = {
							"cmd",
							"/c",
							"%windir%/system32/mstsc.exe /v:"
									+ ((PC) tree.getLastSelectedPathComponent())
											.getIp() + " /admin" };
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					new InfoDialog(shell, SWT.DIALOG_TRIM,
							"Nepodarilo sa spustiù Remote Desktop", "Chyba")
							.open();
				}
			}
		});

		vncButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Runtime.getRuntime()
							.exec(DAOFactory.INSTANCE.getSettingDAO()
									.getSetting("VNC path")
									+ " "
									+ ((PC) tree.getLastSelectedPathComponent())
											.getIp());
				} catch (IOException e) {
					new InfoDialog(shell, SWT.DIALOG_TRIM,
							"Nepodarilo sa spustiù VNC", "Chyba").open();
				}
			}
		});

		settingButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				SettingsDialog settingsWindow = new SettingsDialog(shell,
						SWT.TITLE);
				settingsWindow.open();
				shell.setEnabled(true);
			}
		});

		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (tree.getSelectionCount() != 1) {
				addPCButton.setEnabled(false);
				deleteItemButton.setEnabled(false);
				editItemButton.setEnabled(false);
				pingButton.setEnabled(false);
				vncButton.setEnabled(false);
				remoteDesktopButton.setEnabled(false);
			} else {
				if (tree.getLastSelectedPathComponent().getClass() == Bookmark.class) {
					addPCButton.setEnabled(true);
					pingButton.setEnabled(false);
					vncButton.setEnabled(false);
					remoteDesktopButton.setEnabled(false);
				} else {

					if (((PC) tree.getLastSelectedPathComponent())
							.getConnectionType().equals("RD")) {
						vncButton.setEnabled(false);
						remoteDesktopButton.setEnabled(true);
					} else {
						if (((PC) tree.getLastSelectedPathComponent())
								.getConnectionType().equals("VNC")) {
							vncButton.setEnabled(true);
							remoteDesktopButton.setEnabled(false);
						} else {
							vncButton.setEnabled(false);
							remoteDesktopButton.setEnabled(false);
						}
					}
					addPCButton.setEnabled(false);
					pingButton.setEnabled(true);

				}
				deleteItemButton.setEnabled(true);
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
				pingResult += inputLine;
				pingResult += "\n";
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pingResult;
	}

	public Shell getShell() {
		return shell;
	}

	public JTree getTree() {
		return tree;
	}

	private char[] getTreeExpansionState() {
		int rowCount = tree.getRowCount();
		char[] treeState = new char[rowCount];
		for (int i = 0; i < rowCount; i++) {
			if (i == tree.getLeadSelectionRow()) {
				treeState[i] = 'd';
				continue;
			}
			if (tree.isExpanded(i))
				treeState[i] = 'e';
			else
				treeState[i] = 'c';
		}
		return treeState;
	}

	private void setTreeExpansionState(char[] treeState) {
		int row = 0;
		for (int i = 0; i < treeState.length; i++) {
			if (treeState[i] == 'd')
				continue;
			if (treeState[i] == 'e')
				tree.expandRow(row);
			row++;
		}
	}
}
