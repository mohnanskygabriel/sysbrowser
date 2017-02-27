package kybsysbrowser.dialog;

import java.io.FileNotFoundException;

import javax.swing.tree.TreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import kybsysbrowser.dialog.exceptionSolving.InfoDialog;
import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.ModelFactory;
import kybsysbrowser.factory.WindowFactory;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class AddPCDialog extends Dialog {

	private Object result;
	private Shell shell;
	private Text textPCName;
	private Text textIPAdress;
	private String connectionType = "RD";
	private boolean editMode;
	private Shell parentShell;

	public AddPCDialog(Shell parent, int style, boolean editMode) {
		super(parent, style);
		this.editMode = editMode;
		this.parentShell = parent;
		setText("Pridanie poËÌtaËa k systÈmu "
				+ WindowFactory.INSTANCE.getWindow_Browser().getTree().getLastSelectedPathComponent());
	}

	public Object open() {
		createContents();
		Display display = getParent().getDisplay();
		Rectangle parentBounds = parentShell.getBounds();
		shell.setLocation(parentBounds.x + parentBounds.width / 2 - shell.getSize().x / 2,
				parentBounds.y + parentBounds.height / 2 - shell.getSize().y / 2);
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
		shell.setSize(360, 190);
		shell.setText(getText());
		shell.setLayout(new FormLayout());

		Label lblSystem = new Label(shell, SWT.WRAP);
		FormData fd_lblSystem = new FormData();
		fd_lblSystem.left = new FormAttachment(0, 10);
		fd_lblSystem.top = new FormAttachment(0, 10);
		lblSystem.setLayoutData(fd_lblSystem);
		lblSystem.setText("Pridanie poËÌtaËa k systÈmu "
				+ WindowFactory.INSTANCE.getWindow_Browser().getTree().getLastSelectedPathComponent());

		Label lblPCName = new Label(shell, SWT.NONE);
		FormData fd_lblPCName = new FormData();
		fd_lblPCName.left = new FormAttachment(lblSystem, 0, SWT.LEFT);
		lblPCName.setLayoutData(fd_lblPCName);
		lblPCName.setText("N·zov poËÌtaËa:");

		textPCName = new Text(shell, SWT.BORDER);
		fd_lblPCName.top = new FormAttachment(textPCName, 3, SWT.TOP);
		FormData fd_textPCName = new FormData();
		fd_textPCName.left = new FormAttachment(lblPCName, 22);
		fd_textPCName.right = new FormAttachment(100, -10);
		fd_textPCName.top = new FormAttachment(lblSystem, 6);
		textPCName.setLayoutData(fd_textPCName);

		Label lblIpAdress = new Label(shell, SWT.NONE);
		FormData fd_lblIpAdress = new FormData();
		fd_lblIpAdress.left = new FormAttachment(lblSystem, 0, SWT.LEFT);
		lblIpAdress.setLayoutData(fd_lblIpAdress);
		lblIpAdress.setText("IP adresa poËÌtaËa:");

		textIPAdress = new Text(shell, SWT.BORDER);
		fd_lblIpAdress.top = new FormAttachment(textIPAdress, 3, SWT.TOP);
		FormData fd_textIPAdress = new FormData();
		fd_textIPAdress.top = new FormAttachment(textPCName, 6);
		fd_textIPAdress.left = new FormAttachment(textPCName, 0, SWT.LEFT);
		fd_textIPAdress.right = new FormAttachment(100, -10);
		textIPAdress.setLayoutData(fd_textIPAdress);

		Label lblConnectionType = new Label(shell, SWT.NONE);
		FormData fd_lblConnectionType = new FormData();
		fd_lblConnectionType.left = new FormAttachment(lblSystem, 0, SWT.LEFT);
		lblConnectionType.setLayoutData(fd_lblConnectionType);
		lblConnectionType.setText("Typ pripojenia:");

		final Button btnRemoteDesktop = new Button(shell, SWT.RADIO);
		fd_lblConnectionType.top = new FormAttachment(btnRemoteDesktop, 2, SWT.TOP);
		FormData fd_btnRemoteDesktop = new FormData();
		fd_btnRemoteDesktop.top = new FormAttachment(textIPAdress, 6);
		fd_btnRemoteDesktop.left = new FormAttachment(textPCName, 0, SWT.LEFT);
		btnRemoteDesktop.setLayoutData(fd_btnRemoteDesktop);
		btnRemoteDesktop.setText("Remote Desktop");

		final Button btnVnc = new Button(shell, SWT.RADIO);
		FormData fd_btnVnc = new FormData();
		fd_btnVnc.top = new FormAttachment(textIPAdress, 6);
		fd_btnVnc.left = new FormAttachment(btnRemoteDesktop, 6);
		btnVnc.setLayoutData(fd_btnVnc);
		btnVnc.setText("VNC");

		final Button btnNoConnection = new Button(shell, SWT.RADIO);
		FormData fd_btnNoConnection = new FormData();
		fd_btnNoConnection.top = new FormAttachment(textIPAdress, 6);
		fd_btnNoConnection.left = new FormAttachment(btnVnc, 6);
		btnNoConnection.setLayoutData(fd_btnNoConnection);
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

		if (!editMode)
			btnNoConnection.setSelection(true);

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(textPCName, 0, SWT.RIGHT);
		fd_label.bottom = new FormAttachment(lblConnectionType, 19, SWT.BOTTOM);
		fd_label.top = new FormAttachment(lblConnectionType, 6);
		fd_label.left = new FormAttachment(lblSystem, 0, SWT.LEFT);
		label.setLayoutData(fd_label);

		Button btnAddPC = new Button(shell, SWT.NONE);
		FormData fd_btnAddPC = new FormData();
		fd_btnAddPC.top = new FormAttachment(label, 6);
		fd_btnAddPC.left = new FormAttachment(lblSystem, 0, SWT.LEFT);
		btnAddPC.setLayoutData(fd_btnAddPC);
		btnAddPC.setText("Pridaù poËÌtaË");

		Button btnCancel = new Button(shell, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(label, 6);
		fd_btnCancel.left = new FormAttachment(textPCName, 0, SWT.LEFT);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Zruöiù");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});

		if (editMode) {
			PC currentPC = (PC) WindowFactory.INSTANCE.getWindow_Browser().getTree().getLastSelectedPathComponent();
			shell.setText("Zmena vlastnostÌ poËÌtaËa");
			btnAddPC.setText("Uloûiù zmeny");
			lblSystem.setText("Zmena vlastnostÌ poËÌtaËa: " + currentPC.getName());
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
						InfoDialog chybaVstupuPopUP = new InfoDialog(shell, SWT.DIALOG_TRIM, "Zadaj n·zov poËÌtaËa",
								"N·zov");
						chybaVstupuPopUP.open();
						return;
					}
					if (textIPAdress.getText().trim().length() == 0) {
						InfoDialog chybaVstupuPopUP = new InfoDialog(shell, SWT.DIALOG_TRIM, "Zadaj IP adresu", "IP");
						chybaVstupuPopUP.open();
						return;
					}
					if (btnVnc.isFocusControl())
						connectionType = "VNC";
					if (btnRemoteDesktop.isFocusControl())
						connectionType = "RD";
					if (btnNoConnection.isFocusControl())
						connectionType = "ConnectionTypeNotDefinied";

					currentPC.setConnectionType(connectionType);
					currentPC.setIp(textIPAdress.getText());
					currentPC.setName(textPCName.getText());
					try {
						DAOFactory.INSTANCE.getPCDAO().editPC(currentPC);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					shell.close();
				}
			});
		} else {
			btnAddPC.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (textPCName.getText().trim().length() == 0) {
						InfoDialog chybaVstupuPopUP = new InfoDialog(shell, SWT.DIALOG_TRIM, "Zadaj n·zov poËÌtaËa",
								"N·zov");
						chybaVstupuPopUP.open();
						return;
					}
					if (textIPAdress.getText().trim().length() == 0) {
						InfoDialog chybaVstupuPopUP = new InfoDialog(shell, SWT.DIALOG_TRIM, "Zadaj IP adresu", "IP");
						chybaVstupuPopUP.open();
						return;
					}
					PC newPC = null;
					try {
						newPC = new PC(textPCName.getText().trim(), textIPAdress.getText().trim(), connectionType);
					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
					try {
						DAOFactory.INSTANCE.getPCDAO().insertPC(newPC, ((Bookmark) WindowFactory.INSTANCE
								.getWindow_Browser().getTree().getLastSelectedPathComponent()).getId());
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					ModelFactory.INSTANCE.getBookmarkTreeModel().nodeStructureChanged((TreeNode) WindowFactory.INSTANCE
							.getWindow_Browser().getTree().getLastSelectedPathComponent());
					shell.close();
				}
			});
		}
	}
}
