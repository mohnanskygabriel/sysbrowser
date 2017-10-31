package kybsysbrowser.dialog.exceptionSolving;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class InputMissingDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected String[] missingInputs;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public InputMissingDialog(Shell parent, int style, String[] missingInputs) {

		super(parent, style);
		setText("Ch�baj�ce vstupy");
		setMissingInputs(missingInputs);

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
		shell.setMinimumSize(140, 100);
		Label lblText = new Label(shell, SWT.LEFT);
		lblText.setBounds(10, 10, 299, 44);
		StringBuilder sb = new StringBuilder();
		for (String input : getMissingInputs()) {
			sb.append(input + "\n");
		}
		lblText.setText("Mus� zada� " + "\n" + sb.toString());
		lblText.setSize(lblText.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	private String[] getMissingInputs() {
		return missingInputs;
	}

	private void setMissingInputs(String[] missingInputs) {
		this.missingInputs = missingInputs;
	}

}
