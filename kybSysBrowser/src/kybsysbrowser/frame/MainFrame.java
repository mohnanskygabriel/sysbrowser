package kybsysbrowser.frame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JRootPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

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

import kybsysbrowser.dialog.AddBookmarkDialog;
import kybsysbrowser.dialog.AddPCDialog;
import kybsysbrowser.dialog.DeleteItemFromTreeDialog;
import kybsysbrowser.dialog.SettingsDialog;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.ModelFactory;

public class MainFrame {

	/*
	 * TODO: po zmene stromu:
	 * vytvorit triedu ktora bude uchovavat vsetky riesenia exceptionov a na
	 * riesenie exceptionov uz iba volat z tejto triedy dane riesenie vynimky
	 * vyriesit stranky ako TSP-KIS ktore bezia na starom IE iba squele
	 */

	private JTree tree;
	private Shell shell = null;

	/**
	 * 
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
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.widthHint = 150;
		composite.setLayoutData(gd_composite);
		Frame frame = SWT_AWT.new_Frame(composite);
		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);

		tree = new JTree(ModelFactory.INSTANCE.getBookmarkTreeModel());
		tree.setRootVisible(false);
		rootPane.getContentPane().add(tree, BorderLayout.CENTER);

		final Browser browser = new Browser(shell, SWT.NORMAL);
		GridData gd_browser = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_browser.widthHint = 600;
		gd_browser.heightHint = 540;
		browser.setLayoutData(gd_browser);

		addBookmarkButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				AddBookmarkDialog formAddBookmark = new AddBookmarkDialog(shell, SWT.DIALOG_TRIM, false);
				formAddBookmark.open();
				ModelFactory.INSTANCE.getBookmarkTreeModel().reload();
				shell.setEnabled(true);
			}
		});

		addPCButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				AddPCDialog addPCDialog = new AddPCDialog(shell, SWT.DIALOG_TRIM, false);
				addPCDialog.open();
				ModelFactory.INSTANCE.getBookmarkTreeModel()
						.nodeStructureChanged((TreeNode) tree.getLastSelectedPathComponent());
				shell.setEnabled(true);
			}
		});

		editItemButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				shell.setEnabled(false);
				char[] treeState = getTreeExpansionState();
				if (tree.getLastSelectedPathComponent().getClass() == PC.class) {
					AddPCDialog addPCDialog = new AddPCDialog(addPCButton.getParent().getShell(), SWT.DIALOG_TRIM,
							true);
					addPCDialog.open();
				} else {
					AddBookmarkDialog formAddBookmark = new AddBookmarkDialog(shell, SWT.DIALOG_TRIM, true);
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
				DeleteItemFromTreeDialog deleteItemDialog = new DeleteItemFromTreeDialog(shell, SWT.DIALOG_TRIM);
				char[] treeState = getTreeExpansionState();
				deleteItemDialog.open();
				ModelFactory.INSTANCE.getBookmarkTreeModel().reload();
				setTreeExpansionState(treeState);
				shell.setEnabled(true);
			}
		});
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
		settingButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setEnabled(false);
				SettingsDialog settingsWindow = new SettingsDialog(shell, SWT.TITLE);
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
				addPCButton.setEnabled(true);
				deleteItemButton.setEnabled(true);
				editItemButton.setEnabled(true);
				pingButton.setEnabled(true);
				vncButton.setEnabled(true);
				remoteDesktopButton.setEnabled(true);
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
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
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

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
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
