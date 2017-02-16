package kybsysbrowser.dialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import kybsysbrowser.dialog.exceptionSolving.FileNotFoundDialog;
import kybsysbrowser.dialog.exceptionSolving.IOExceptionFileWriterErrorDialog;
import kybsysbrowser.dialog.exceptionSolving.InfoDialog;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class AddPCDialog extends Dialog {

	private Object result;
	private Shell shell;
	private TreeItem parentItem;
	private Text textPCName;
	private Text textIPAdress;
	private File fileOfBookmarks;
	private String connectionType = "RD";
	private boolean editMode;
	private PC currentPC;
	private Shell parentShell;

	public AddPCDialog(Shell parent, int style, TreeItem parentItem,
			boolean editMode, PC currentPC) {
		super(parent, style);
		this.parentItem = parentItem;
		this.fileOfBookmarks = new File(DAOFactory.INSTANCE.getSettingDAO()
				.getSetting("File of bookmarks"));
		this.editMode = editMode;
		this.currentPC = currentPC;
		this.parentShell = parent;
		setText("Pridanie poËÌtaËa k systÈmu " + parentItem.getText());
	}

	public Object open() {
		createContents();
		Display display = getParent().getDisplay();
		Rectangle parentBounds = parentShell.getBounds();
		shell.setLocation(
				parentBounds.x + parentBounds.width / 2 - shell.getSize().x / 2,
				parentBounds.y + parentBounds.height / 2 - shell.getSize().y
						/ 2);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(360, 180);
		shell.setText(getText());

		Label lblSystem = new Label(shell, SWT.WRAP);
		lblSystem.setBounds(10, 10, 454, 20);
		lblSystem.setText("Pridanie poËÌtaËa k systÈmu: "
				+ parentItem.getText());

		Label lblPCName = new Label(shell, SWT.NONE);
		lblPCName.setBounds(10, 36, 93, 13);
		lblPCName.setText("N·zov poËÌtaËa:");

		textPCName = new Text(shell, SWT.BORDER);
		textPCName.setBounds(137, 33, 205, 19);

		Label lblIpAdress = new Label(shell, SWT.NONE);
		lblIpAdress.setBounds(10, 61, 93, 13);
		lblIpAdress.setText("IP adresa poËÌtaËa:");

		textIPAdress = new Text(shell, SWT.BORDER);
		textIPAdress.setBounds(137, 58, 205, 19);

		Label lblConnectionType = new Label(shell, SWT.NONE);
		lblConnectionType.setBounds(10, 85, 93, 13);
		lblConnectionType.setText("Typ pripojenia:");

		final Button btnRemoteDesktop = new Button(shell, SWT.RADIO);
		btnRemoteDesktop.setBounds(137, 83, 100, 16);
		btnRemoteDesktop.setText("Remote Desktop");

		final Button btnVnc = new Button(shell, SWT.RADIO);
		btnVnc.setBounds(243, 83, 40, 16);
		btnVnc.setText("VNC");

		final Button btnNoConnection = new Button(shell, SWT.RADIO);
		btnNoConnection.setBounds(289, 83, 53, 16);
		btnNoConnection.setText("éiadne");

		btnVnc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnRemoteDesktop.setSelection(false);
				btnNoConnection.setSelection(false);
				btnVnc.setSelection(true);
				connectionType = "VNC";
			}
		});

		btnNoConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnRemoteDesktop.setSelection(false);
				btnVnc.setSelection(false);
				btnNoConnection.setSelection(true);
				connectionType = "NoConnectionDefinied";
			}
		});

		btnRemoteDesktop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnVnc.setSelection(false);
				btnNoConnection.setSelection(false);
				btnRemoteDesktop.setSelection(true);
				connectionType = "RD";
			}
		});

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 112, 327, 2);

		Button btnAddPC = new Button(shell, SWT.NONE);
		btnAddPC.setBounds(10, 121, 78, 23);
		btnAddPC.setText("Pridaù poËÌtaË");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(94, 121, 68, 23);
		btnCancel.setText("Zruöiù");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});

		if (editMode) {
			shell.setText("Zmena vlastnostÌ poËÌtaËa");
			btnAddPC.setText("Uloûiù zmeny");
			lblSystem.setText("Zmena vlastnostÌ poËÌtaËa: "
					+ currentPC.getName());
			textPCName.setText(currentPC.getName());
			textIPAdress.setText(currentPC.getIp());
			connectionType = currentPC.getConnectionType();
			if (connectionType.equals("RD")) {
				btnRemoteDesktop.setSelection(true);
			} else {
				if (connectionType.equals("VNC"))
					btnVnc.setSelection(true);
				else
					btnNoConnection.setSelection(true);
			}
			btnAddPC.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (textPCName.getText().trim().length() == 0) {
						InfoDialog chybaVstupuPopUP = new InfoDialog(shell,
								SWT.DIALOG_TRIM, "Zadaj n·zov poËÌtaËa",
								"N·zov");
						chybaVstupuPopUP.open();
						return;
					}
					if (textIPAdress.getText().trim().length() == 0) {
						InfoDialog chybaVstupuPopUP = new InfoDialog(shell,
								SWT.DIALOG_TRIM, "Zadaj IP adresu", "IP");
						chybaVstupuPopUP.open();
						return;
					}
					if (btnVnc.isFocusControl())
						connectionType = "VNC";
					if (btnRemoteDesktop.isFocusControl())
						connectionType = "RD";
					if (btnNoConnection.isFocusControl())
						connectionType = "ConnectionTypeNotDefinied";
					Scanner sc = null;
					Scanner scannerLine = null;
					FileWriter fw = null;
					File copyOfBookmarks = new File("newFileOfBookmars.txt");
					try {
						sc = new Scanner(fileOfBookmarks);
						fw = new FileWriter(copyOfBookmarks);
						while (sc.hasNextLine()) {
							String line = sc.nextLine();
							if (!line.equals(parentItem.getText())) {
								fw.write(line + "\n");
							} else {
								fw.write(line + "\n");
								String systemLines = sc.nextLine();
								scannerLine = new Scanner(systemLines);
								scannerLine.useDelimiter("\t");
								// linka zapisana
								while (!scannerLine.next().equals(
										currentPC.getName())
										&& !systemLines.equals("---")) {
									fw.write(systemLines + "\n");
									systemLines = sc.nextLine();
									scannerLine = new Scanner(systemLines);
									scannerLine.useDelimiter("\t");
								}
								fw.write(textPCName.getText().trim() + "\t"
										+ connectionType + "\t"
										+ textIPAdress.getText().trim() + "\n");
							}
						}
						if (fw != null)
							fw.close();
						if (sc != null)
							sc.close();

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
								renamed = copyOfBookmarks
										.renameTo(fileOfBookmarks);
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
					} catch (FileNotFoundException fnf) {
						FileNotFoundDialog popUpFileNF = new FileNotFoundDialog(
								shell, SWT.DIALOG_TRIM, fileOfBookmarks
										.getAbsolutePath());
						popUpFileNF.open();
					} catch (IOException ioex) {
						IOExceptionFileWriterErrorDialog popUpIO = new IOExceptionFileWriterErrorDialog(
								shell, SWT.DIALOG_TRIM, copyOfBookmarks
										.getAbsolutePath());
						popUpIO.open();
					}
					shell.close();
				}
			});
		} else {
			btnAddPC.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (textPCName.getText().trim().length() == 0) {
						InfoDialog chybaVstupuPopUP = new InfoDialog(shell,
								SWT.DIALOG_TRIM, "Zadaj n·zov poËÌtaËa",
								"N·zov");
						chybaVstupuPopUP.open();
						return;
					}
					if (textIPAdress.getText().trim().length() == 0) {
						InfoDialog chybaVstupuPopUP = new InfoDialog(shell,
								SWT.DIALOG_TRIM, "Zadaj IP adresu", "IP");
						chybaVstupuPopUP.open();
						return;
					}
					Scanner sc = null;
					FileWriter fw = null;
					File copyOfBookmarks = new File("newFileOfBookmars.txt");
					try {
						sc = new Scanner(fileOfBookmarks);
						fw = new FileWriter(copyOfBookmarks);
						while (sc.hasNextLine()) {
							String line = sc.nextLine();
							if (!line.equals(parentItem.getText())) {
								fw.write(line + "\n");
							} else {
								fw.write(line + "\n");
								String systemLines = sc.nextLine();
								while (!systemLines.equals("---")) {
									fw.write(systemLines + "\n");
									systemLines = sc.nextLine();
								}
								fw.write(textPCName.getText().trim() + "\t"
										+ connectionType + "\t"
										+ textIPAdress.getText().trim() + "\n");
								fw.write("---");
								if (sc.hasNextLine())
									fw.write("\n");
							}
						}
						if (fw != null)
							fw.close();
						if (sc != null)
							sc.close();
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
								renamed = copyOfBookmarks
										.renameTo(fileOfBookmarks);
								if (renamed)
									break;
								System.gc();
								Thread.yield();
							}

						}
					} catch (FileNotFoundException fnf) {
						FileNotFoundDialog popUpFileNF = new FileNotFoundDialog(
								shell, SWT.DIALOG_TRIM, fileOfBookmarks
										.getAbsolutePath());
						popUpFileNF.open();
					} catch (IOException ioex) {
						IOExceptionFileWriterErrorDialog popUpIO = new IOExceptionFileWriterErrorDialog(
								shell, SWT.DIALOG_TRIM, copyOfBookmarks
										.getAbsolutePath());
						popUpIO.open();
					}
					shell.close();
				}
			});

			btnNoConnection.notifyListeners(SWT.Selection, new Event());
		}
	}
}
