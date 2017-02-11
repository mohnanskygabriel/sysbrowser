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

public class PopUpInfo extends Dialog {

	protected Object result;
	protected Shell shell;
	protected String text;

	public PopUpInfo(Shell parent, int style, String text, String windowTitle) {
		super(parent, style);
		setText(windowTitle);
		this.text = text;
	}

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

	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setText(getText());
		Label lblText = new Label(shell, SWT.LEFT);
		lblText.setBounds(10, 10, 299, 44);
		lblText.setText(text);
		Button btnOk = new Button(shell, SWT.CENTER);
		btnOk.setBounds(29, 55, 68, 23);
		btnOk.setText("OK");
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		lblText.setSize(lblText.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

}