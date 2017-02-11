package popups;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PopUpPingOutput extends Dialog {

	protected Object result;
	protected Shell shell;
	private String ip;
	private Label lblOutput;
	private Text text;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PopUpPingOutput(Shell parent, int style, String ip) {
		super(parent, style);
		setText("PING");
		this.ip = ip;
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

		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 10, 334, 117);

		shell.open();
		shell.layout();
		pingCurrentSelection();
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
		shell.setSize(360, 200);
		final Label lblOutput = new Label(shell, SWT.RESIZE);
		this.lblOutput = lblOutput;
		lblOutput.setBounds(10, 10, 334, 117);
		lblOutput.setText("");

		Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setBounds(14, 133, 330, 2);

		Button btnRestart = new Button(shell, SWT.CENTER);
		btnRestart.setBounds(10, 141, 68, 23);
		btnRestart.setText("Restart");

		Button btnStopAndExit = new Button(shell, SWT.CENTER);
		btnStopAndExit.setBounds(210, 141, 134, 23);
		btnStopAndExit.setText("Zastaviù a zatvoriù okno");

		btnRestart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblOutput.setText("");
				pingCurrentSelection();
			}
		});

		btnStopAndExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});

	}

	protected void pingCurrentSelection() {
		String pingCmd = "ping " + ip;
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				lblOutput.setText(lblOutput.getText() + inputLine + "\n");
				// System.out.println(inputLine);
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
