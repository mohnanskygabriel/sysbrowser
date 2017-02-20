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

public class SettingsDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text textVNCPath;
	private Text textBookmarksPath;
	protected Shell parentShell;

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
		lblPathForBookmarksFile.setBounds(10, 77, 183, 13);
		lblPathForBookmarksFile
				.setText("Cesta pre s˙bor s poloûkami v strome:");

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 126, 333, 2);
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
		shell.setSize(360, 200);
		shell.setText(getText());

		Label lblCestaPreAplikciu = new Label(shell, SWT.NONE);
		lblCestaPreAplikciu.setBounds(10, 10, 122, 13);
		lblCestaPreAplikciu.setText("Cesta pre aplik·ciu VNC:");

		Button btnSetVNCPath = new Button(shell, SWT.NONE);
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
		btnSetVNCPath.setBounds(199, 5, 144, 23);

		Button setBookmarksFilepathButton = new Button(shell, SWT.NONE);
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
		setBookmarksFilepathButton.setBounds(199, 72, 144, 23);

		textVNCPath = new Text(shell, SWT.BORDER);
		textVNCPath.setBounds(10, 34, 333, 19);
		textVNCPath.setText(DAOFactory.INSTANCE.getSettingDAO().getSetting(
				"VNC path"));
		textVNCPath.setEditable(false);

		textBookmarksPath = new Text(shell, SWT.BORDER);
		textBookmarksPath.setText((DAOFactory.INSTANCE.getSettingDAO()
				.getSetting("File of bookmarks")));
		textBookmarksPath.setEditable(false);
		textBookmarksPath.setBounds(10, 101, 333, 19);

		Button btnSaveChanges = new Button(shell, SWT.NONE);
		btnSaveChanges.setBounds(137, 141, 100, 23);
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
		btnCancelChanges.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnCancelChanges.setText("Zruöiù zmeny");
		btnCancelChanges.setBounds(243, 141, 100, 23);

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 64, 333, 2);

	}
}
