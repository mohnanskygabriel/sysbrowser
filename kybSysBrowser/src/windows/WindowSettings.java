package windows;

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

import factories.WindowBrowserFactory;
import popups.PopUpInfo;

public class WindowSettings extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text textVNCPath;
	protected Shell parentShell;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public WindowSettings(Shell parent, int style) {
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
		final WindowBrowser mainApp = WindowBrowserFactory.INSTANCE
				.getWindow_Browser();
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 131);
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
		btnSetVNCPath.setText("Nastavenie cesty");
		btnSetVNCPath.setBounds(138, 5, 122, 23);

		textVNCPath = new Text(shell, SWT.BORDER);
		textVNCPath.setBounds(10, 34, 424, 19);
		textVNCPath.setText(mainApp.getVncPath());
		textVNCPath.setEditable(false);

		Button btnSaveChanges = new Button(shell, SWT.NONE);
		btnSaveChanges.setBounds(160, 72, 100, 23);
		btnSaveChanges.setText("Uloûiù zmeny");
		btnSaveChanges.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				PopUpInfo popUpOk = new PopUpInfo(shell, SWT.DIALOG_TRIM,
						"Zmeny uloûenÈ", "Zmeny uloûenÈ");
				mainApp.refreshVNCPath(textVNCPath.getText());
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
		btnCancelChanges.setBounds(334, 72, 100, 23);

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 64, 424, 2);

	}
}
