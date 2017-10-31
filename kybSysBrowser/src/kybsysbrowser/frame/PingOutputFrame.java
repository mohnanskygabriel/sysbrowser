package kybsysbrowser.frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class PingOutputFrame extends Dialog {

	protected Object result;
	protected Shell shell;
	private String ip;
	private Label lblOutput;
	private Label separator;
	private Label lblOutput_1;
	private FormData fd_separator;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PingOutputFrame(Shell parent, int style, String ip) {
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
		shell.setSize(420, 211);
		shell.setLayout(new FormLayout());
		lblOutput_1 = new Label(shell, SWT.RESIZE);
		FormData fd_lblOutput_1 = new FormData();
		fd_lblOutput_1.top = new FormAttachment(0, 10);
		fd_lblOutput_1.left = new FormAttachment(0, 10);
		lblOutput_1.setLayoutData(fd_lblOutput_1);
		this.lblOutput = lblOutput_1;
		lblOutput_1.setText("");

		separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		fd_lblOutput_1.bottom = new FormAttachment(separator, -6);
		fd_lblOutput_1.right = new FormAttachment(separator, 0, SWT.RIGHT);
		fd_separator = new FormData();
		fd_separator.left = new FormAttachment(0, 14);
		fd_separator.right = new FormAttachment(100, -10);
		fd_separator.bottom = new FormAttachment(0, 146);
		fd_separator.top = new FormAttachment(0, 133);
		separator.setLayoutData(fd_separator);

		Button btnRestart = new Button(shell, SWT.CENTER);
		FormData fd_btnRestart = new FormData();
		fd_btnRestart.left = new FormAttachment(0, 10);
		btnRestart.setLayoutData(fd_btnRestart);
		btnRestart.setText("Reötart");

		Button btnStopAndExit = new Button(shell, SWT.CENTER);
		fd_btnRestart.top = new FormAttachment(btnStopAndExit, 0, SWT.TOP);
		FormData fd_btnStopAndExit = new FormData();
		fd_btnStopAndExit.left = new FormAttachment(btnRestart, 6);
		fd_btnStopAndExit.top = new FormAttachment(separator, 6);
		btnStopAndExit.setLayoutData(fd_btnStopAndExit);
		btnStopAndExit.setText("Zatvoriù okno");

		btnRestart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblOutput_1.setText("");
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

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.defaultCharset()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				lblOutput.setText(lblOutput.getText() + inputLine + "\n");
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
