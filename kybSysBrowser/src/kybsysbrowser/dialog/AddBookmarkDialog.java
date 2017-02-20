package kybsysbrowser.dialog;

import java.io.FileNotFoundException;

import kybsysbrowser.dialog.exceptionSolving.InputMissingDialog;
import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.factory.DAOFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddBookmarkDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text inputUserBookmarkName;
	private Text inputUserURL;
	private Button btnSave;
	private boolean editMode;
	private Bookmark currentBookmark;
	private Button btnCancel;
	private Label label;
	private Shell parentShell;

	public AddBookmarkDialog(Shell parent, int style, boolean editMode) {
		super(parent, style);
		setText("Pridanie nov�ho syst�mu");
		this.editMode = editMode;
		this.parentShell = parent;
	}

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

	private void createContents() {
		shell = new Shell(parentShell, SWT.DIALOG_TRIM);
		shell.setText("Pridanie nov�ho syst�mu");
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		shell.setSize(480, 122);
		Label lblNazovSystemu = new Label(shell, SWT.NONE);
		lblNazovSystemu.setLayoutData(new RowData(SWT.DEFAULT, 16));
		lblNazovSystemu.setText("N�zov syst�mu:");
		inputUserBookmarkName = new Text(shell, SWT.BORDER);
		inputUserBookmarkName.setLayoutData(new RowData(260, SWT.DEFAULT));
		Label lblOdkazNaWebstranku = new Label(shell, SWT.NONE);
		lblOdkazNaWebstranku.setText("Odkaz na webstr�nku syst�mu:");
		inputUserURL = new Text(shell, SWT.BORDER);
		inputUserURL.setLayoutData(new RowData(450, SWT.DEFAULT));
		label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new RowData(458, 3));
		btnSave = new Button(shell, SWT.NONE);
		btnSave.setLayoutData(new RowData(120, 20));
		btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		btnCancel.setLayoutData(new RowData(120, 20));
		btnCancel.setText("Zru�i�");
		if (editMode) {
			inputUserBookmarkName.setText(currentBookmark.getName());
			inputUserURL.setText(currentBookmark.getUrl());
			btnSave.setText("Ulo�i� zmeny");
		} else {
			btnSave.setText("Prida� syst�m");
			inputUserURL.setText("http://");
		}

		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkInput();
				try {
					if (!editMode)
						DAOFactory.INSTANCE.getBookmarkDao().insertBookmark(
								new Bookmark(inputUserBookmarkName.getText(),
										inputUserURL.getText()));
					else
						DAOFactory.INSTANCE.getBookmarkDao().editBookmark(
								new Bookmark(inputUserBookmarkName.getText(),
										inputUserURL.getText()));

				} catch (FileNotFoundException fnfEx) {
					fnfEx.printStackTrace();
				} finally {
					shell.close();
				}
			}
		});
	}

	private void checkInput() {
		String[] missingInputs = null;
		if (inputUserBookmarkName.getText().equals("")) {
			missingInputs = new String[1];
			missingInputs[0] = "n�zov syst�mu";
			if (inputUserURL.getText().equals("")) {
				String[] missingInputs2 = { "n�zov syst�mu,", "adresu URL" };
				missingInputs = missingInputs2;
			}
		} else {
			if (inputUserURL.getText().equals("")) {
				missingInputs = new String[1];
				missingInputs[0] = "adresu URL";
			}
		}
		if (missingInputs != null) {
			InputMissingDialog popUpMissingInputs = new InputMissingDialog(
					shell, SWT.DIALOG_TRIM, missingInputs);
			popUpMissingInputs.open();
			return;
		}
	}

}
