package kybsysbrowser.dialog;

import kybsysbrowser.dialog.exceptionSolving.InfoDialog;
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.WindowBrowserFactory;
import kybsysbrowser.frame.MainFrame;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

public class SettingsDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text textVNCPath;
	protected Shell parentShell;
	private Text text;

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

		Label lblCestaPreSbor = new Label(shell, SWT.NONE);
		lblCestaPreSbor.setBounds(10, 77, 183, 13);
		lblCestaPreSbor
				.setText("Cesta pre s\u00FAbor s polo\u017Ekami v strome:");

		Button setBookmarksFilepatchButton = new Button(shell, SWT.NONE);
		setBookmarksFilepatchButton.setText("Nastavenie vlastnej cesty");
		setBookmarksFilepatchButton.setBounds(199, 72, 144, 23);

		text = new Text(shell, SWT.BORDER);
		text.setText("");
		text.setEditable(false);
		text.setBounds(10, 101, 424, 19);

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 126, 424, 2);

		Button btnPredvolen = new Button(shell, SWT.RADIO);
		btnPredvolen.setBounds(288, 8, 83, 16);
		btnPredvolen.setText("Predvolen\u00E1");

		Button button = new Button(shell, SWT.RADIO);
		button.setText("Predvolen\u00E1");
		button.setBounds(349, 75, 83, 16);
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
		shell.setSize(450, 200);
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
				textVNCPath.setText(fileDialog.getFilterPath() + "\\"
						+ fileDialog.getFileName());
			}
		});
		btnSetVNCPath.setText("Nastavenie vlastnej cesty");
		btnSetVNCPath.setBounds(138, 5, 144, 23);

		textVNCPath = new Text(shell, SWT.BORDER);
		textVNCPath.setBounds(10, 34, 424, 19);
		textVNCPath.setText(DAOFactory.INSTANCE.getSettingDAO().getSetting(
				"VNC path"));
		textVNCPath.setEditable(false);

		Button btnSaveChanges = new Button(shell, SWT.NONE);
		btnSaveChanges.setBounds(228, 141, 100, 23);
		btnSaveChanges.setText("Uloûiù zmeny");
		btnSaveChanges.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				InfoDialog popUpOk = new InfoDialog(shell, SWT.DIALOG_TRIM,
						"Zmeny uloûenÈ", "Zmeny uloûenÈ");
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
		btnCancelChanges.setBounds(334, 141, 100, 23);

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 64, 424, 2);

	}
}
