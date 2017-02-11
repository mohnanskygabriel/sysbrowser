package popups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

public class PopUpIOExceptionFileWriterError extends Dialog {

	protected Object result;
	protected Shell shell;
	protected String filePath;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PopUpIOExceptionFileWriterError(Shell parent, int style,
			String filePath) {
		super(parent, style);
		setText("Chyba pri zápise");
		this.filePath = filePath;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		Display display = getParent().getDisplay();
		Rectangle parentBounds = display.getBounds();
		shell.setLocation(parentBounds.width / 2 - shell.getBounds().width / 2,
				parentBounds.height / 2 - shell.getBounds().height / 2);
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setText(getText());

		Label lblText = new Label(shell, SWT.LEFT);
		lblText.setBounds(10, 10, 299, 44);

		lblText.setText("Nastala chyba poèas zápisu do súboru " + "\n"
				+ filePath);

		Button btnOk = new Button(shell, SWT.CENTER);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		lblText.setSize(lblText.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		btnOk.setBounds(29, 55, 68, 23);
		btnOk.setText("OK");
		shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

}
