package kybsysbrowser.dialog;

import kybsysbrowser.dialog.exceptionSolving.InfoDialog;
import kybsysbrowser.factory.DAOFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class SettingsDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text textVNCPath;
	private Text textBookmarksPath;
	protected Shell parentShell;
	private Button btnSaveChanges;
	private FormData fd_textBookmarksPath;
	private Label lblCestaPreAplikciu;
	private Label label_1;
	private FormData fd_setBookmarksFilepathButton;
	private FormData fd_btnSaveChanges;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SettingsDialog(Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		setText("Nastavenia aplik·cie KybSysBrowser");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		Display display = getParent().getDisplay();
		Rectangle parentBounds = parentShell.getBounds();
		shell.setLocation(
				parentBounds.x + parentBounds.width / 2 - shell.getSize().x / 2,
				parentBounds.y + parentBounds.height / 2 - shell.getSize().y
						/ 2);

		Label lblPathForBookmarksFile = new Label(shell, SWT.NONE);
		fd_setBookmarksFilepathButton.left = new FormAttachment(lblPathForBookmarksFile, 6);
		fd_textBookmarksPath.top = new FormAttachment(0, 105);
		FormData fd_lblPathForBookmarksFile = new FormData();
		fd_lblPathForBookmarksFile.top = new FormAttachment(label_1, 9);
		fd_lblPathForBookmarksFile.left = new FormAttachment(lblCestaPreAplikciu, 0, SWT.LEFT);
		lblPathForBookmarksFile.setLayoutData(fd_lblPathForBookmarksFile);
		lblPathForBookmarksFile
				.setText("Cesta pre s˙bor s poloûkami v strome:");

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		fd_btnSaveChanges.top = new FormAttachment(label, 6);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(textBookmarksPath, 6);
		fd_label.left = new FormAttachment(lblCestaPreAplikciu, 0, SWT.LEFT);
		fd_label.right = new FormAttachment(0, 343);
		label.setLayoutData(fd_label);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(359, 195);
		shell.setText(getText());
		shell.setLayout(new FormLayout());

		lblCestaPreAplikciu = new Label(shell, SWT.NONE);
		FormData fd_lblCestaPreAplikciu = new FormData();
		fd_lblCestaPreAplikciu.left = new FormAttachment(0, 10);
		fd_lblCestaPreAplikciu.top = new FormAttachment(0, 10);
		lblCestaPreAplikciu.setLayoutData(fd_lblCestaPreAplikciu);
		lblCestaPreAplikciu.setText("Cesta pre aplik·ciu VNC:");

		Button btnSetVNCPath = new Button(shell, SWT.NONE);
		FormData fd_btnSetVNCPath = new FormData();
		fd_btnSetVNCPath.top = new FormAttachment(lblCestaPreAplikciu, -5, SWT.TOP);
		fd_btnSetVNCPath.left = new FormAttachment(lblCestaPreAplikciu, 6);
		btnSetVNCPath.setLayoutData(fd_btnSetVNCPath);
		btnSetVNCPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.DIALOG_TRIM);
				fileDialog.open();
				String newPath = fileDialog.getFilterPath().replace("\\", "/")
						+ "/" + fileDialog.getFileName().replace("\\", "/");
				if (newPath.length() > 1)
					textVNCPath.setText(newPath);
			}
		});
		btnSetVNCPath.setText("Nastavenie vlastnej cesty");

		Button setBookmarksFilepathButton = new Button(shell, SWT.CENTER);
		fd_setBookmarksFilepathButton = new FormData();
		fd_setBookmarksFilepathButton.bottom = new FormAttachment(73, -23);
		fd_setBookmarksFilepathButton.right = new FormAttachment(100, -18);
		setBookmarksFilepathButton.setLayoutData(fd_setBookmarksFilepathButton);
		setBookmarksFilepathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fileDialog = new FileDialog(shell, SWT.DIALOG_TRIM);
				fileDialog.open();
				String newPath = fileDialog.getFilterPath().replace("\\", "/")
						+ "/" + fileDialog.getFileName().replace("\\", "/");
				if (newPath.length() > 1)
					textBookmarksPath.setText(newPath);
			}
		});
		setBookmarksFilepathButton.setText("Nastavenie vlastnej cesty");

		textVNCPath = new Text(shell, SWT.BORDER);
		FormData fd_textVNCPath = new FormData();
		fd_textVNCPath.top = new FormAttachment(btnSetVNCPath, 6);
		fd_textVNCPath.left = new FormAttachment(lblCestaPreAplikciu, 0, SWT.LEFT);
		textVNCPath.setLayoutData(fd_textVNCPath);
		textVNCPath.setText(DAOFactory.INSTANCE.getSettingDAO().getSetting(
				"VNC path"));
		textVNCPath.setEditable(false);

		textBookmarksPath = new Text(shell, SWT.BORDER);
		fd_textBookmarksPath = new FormData();
		fd_textBookmarksPath.right = new FormAttachment(textVNCPath, 0, SWT.RIGHT);
		fd_textBookmarksPath.left = new FormAttachment(0, 10);
		textBookmarksPath.setLayoutData(fd_textBookmarksPath);
		textBookmarksPath.setText((DAOFactory.INSTANCE.getSettingDAO()
				.getSetting("File of bookmarks")));
		textBookmarksPath.setEditable(false);

		btnSaveChanges = new Button(shell, SWT.NONE);
		fd_btnSaveChanges = new FormData();
		fd_btnSaveChanges.left = new FormAttachment(lblCestaPreAplikciu, 0, SWT.LEFT);
		btnSaveChanges.setLayoutData(fd_btnSaveChanges);
		btnSaveChanges.setText("Uloûiù zmeny");
		btnSaveChanges.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				InfoDialog popUpOk = new InfoDialog(shell, SWT.DIALOG_TRIM,
						"Zmeny uloûenÈ", "Zmeny uloûenÈ");
				DAOFactory.INSTANCE.getSettingDAO().editSetting(
						"File of bookmarks", textBookmarksPath.getText());
				DAOFactory.INSTANCE.getSettingDAO().editSetting("VNC path",
						textVNCPath.getText());
				popUpOk.open();
				shell.close();
			}
		});

		Button btnCancelChanges = new Button(shell, SWT.NONE);
		FormData fd_btnCancelChanges = new FormData();
		fd_btnCancelChanges.top = new FormAttachment(btnSaveChanges, 0, SWT.TOP);
		fd_btnCancelChanges.left = new FormAttachment(btnSaveChanges, 6);
		btnCancelChanges.setLayoutData(fd_btnCancelChanges);
		btnCancelChanges.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnCancelChanges.setText("Zruöiù zmeny");

		label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		fd_textVNCPath.right = new FormAttachment(label_1, 0, SWT.RIGHT);
		fd_setBookmarksFilepathButton.top = new FormAttachment(label_1, 6);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(textVNCPath, 17);
		fd_label_1.left = new FormAttachment(lblCestaPreAplikciu, 0, SWT.LEFT);
		fd_label_1.right = new FormAttachment(0, 343);
		label_1.setLayoutData(fd_label_1);

	}
}
