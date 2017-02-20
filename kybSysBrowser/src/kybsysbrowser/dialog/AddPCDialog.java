package kybsysbrowser.dialog;

import java.io.FileNotFoundException;

import javax.swing.tree.TreeNode;

import kybsysbrowser.dialog.exceptionSolving.InfoDialog;
import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.ModelFactory;
import kybsysbrowser.factory.WindowFactory;

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

public class AddPCDialog extends Dialog {

	private Object result;
	private Shell shell;
	private Text textPCName;
	private Text textIPAdress;
	private String connectionType = "RD";
	private boolean editMode;
	private PC currentPC;
	private Shell parentShell;

	public AddPCDialog(Shell parent, int style, boolean editMode) {
		super(parent, style);
		this.editMode = editMode;
		this.parentShell = parent;
		setText("Pridanie poËÌtaËa k systÈmu "
				+ WindowFactory.INSTANCE.getWindow_Browser().getTree()
						.getLastSelectedPathComponent());
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
		lblSystem.setText("Pridanie poËÌtaËa k systÈmu "
				+ WindowFactory.INSTANCE.getWindow_Browser().getTree()
						.getLastSelectedPathComponent());

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
					PC newPC = new PC();
					newPC.setConnectionType(connectionType);
					newPC.setIp(textIPAdress.getText().trim());
					newPC.setName(textPCName.getText().trim());
					try {
						DAOFactory.INSTANCE.getPCDAO().insertPC(
								newPC,
								((Bookmark) WindowFactory.INSTANCE
										.getWindow_Browser().getTree()
										.getLastSelectedPathComponent())
										.getId());
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					ModelFactory.INSTANCE.getBookmarkTreeModel()
							.nodeStructureChanged(
									(TreeNode) WindowFactory.INSTANCE
											.getWindow_Browser().getTree()
											.getLastSelectedPathComponent());
					shell.close();
				}
			});

			btnNoConnection.notifyListeners(SWT.Selection, new Event());
		}
	}
}
