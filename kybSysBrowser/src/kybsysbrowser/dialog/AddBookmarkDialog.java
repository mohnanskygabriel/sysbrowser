package kybsysbrowser.dialog;

import java.io.FileNotFoundException;

import kybsysbrowser.dialog.exceptionSolving.InputMissingDialog;
import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.WindowFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class AddBookmarkDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text inputUserBookmarkName;
	private Text inputUserURL;
	private Button btnSave;
	private boolean editMode;
	private Button btnCancel;
	private Label label;

	public AddBookmarkDialog(Shell parent, int style, boolean editMode) {
		super(parent, style);
		setText("Pridanie novÈho systÈmu");
		this.editMode = editMode;
	}

	public Object open() {
		createContents();
		Display display = getParent().getDisplay();
		Rectangle parentBounds = getParent().getBounds();
		shell.setLocation(parentBounds.x + parentBounds.width / 2 - shell.getSize().x / 2,
				parentBounds.y + parentBounds.height / 2 - shell.getSize().y / 2);
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
		shell.setText("Pridanie novÈho systÈmu");
		shell.setSize(500, 160);
		shell.setLayout(new FormLayout());
		Label lblNazovSystemu = new Label(shell, SWT.NONE);
		FormData fd_lblNazovSystemu = new FormData();
		fd_lblNazovSystemu.right = new FormAttachment(0, 87);
		fd_lblNazovSystemu.bottom = new FormAttachment(0, 26);
		fd_lblNazovSystemu.left = new FormAttachment(0, 10);
		fd_lblNazovSystemu.top = new FormAttachment(0, 10);
		lblNazovSystemu.setLayoutData(fd_lblNazovSystemu);
		lblNazovSystemu.setText("N·zov systÈmu:");
		inputUserBookmarkName = new Text(shell, SWT.BORDER);
		FormData fd_inputUserBookmarkName = new FormData();
		fd_inputUserBookmarkName.top = new FormAttachment(0, 7);
		fd_inputUserBookmarkName.right = new FormAttachment(100, -9);
		fd_inputUserBookmarkName.left = new FormAttachment(0, 93);
		inputUserBookmarkName.setLayoutData(fd_inputUserBookmarkName);
		Label lblOdkazNaWebstranku = new Label(shell, SWT.NONE);
		FormData fd_lblOdkazNaWebstranku = new FormData();
		fd_lblOdkazNaWebstranku.bottom = new FormAttachment(lblNazovSystemu, 19, SWT.BOTTOM);
		fd_lblOdkazNaWebstranku.right = new FormAttachment(0, 181);
		fd_lblOdkazNaWebstranku.top = new FormAttachment(lblNazovSystemu, 6);
		fd_lblOdkazNaWebstranku.left = new FormAttachment(0, 10);
		lblOdkazNaWebstranku.setLayoutData(fd_lblOdkazNaWebstranku);
		lblOdkazNaWebstranku.setText("Odkaz na webstr·nku systÈmu:");
		inputUserURL = new Text(shell, SWT.BORDER);
		FormData fd_inputUserURL = new FormData();
		fd_inputUserURL.left = new FormAttachment(0, 10);
		fd_inputUserURL.right = new FormAttachment(100, -9);
		fd_inputUserURL.top = new FormAttachment(0, 51);
		inputUserURL.setLayoutData(fd_inputUserURL);
		label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(inputUserURL, 9, SWT.BOTTOM);
		fd_label.top = new FormAttachment(inputUserURL, 7);
		fd_label.left = new FormAttachment(0, 10);
		fd_label.right = new FormAttachment(100, -9);
		label.setLayoutData(fd_label);
		btnSave = new Button(shell, SWT.NONE);
		FormData fd_btnSave = new FormData();
		fd_btnSave.top = new FormAttachment(label, 6);
		fd_btnSave.right = new FormAttachment(lblOdkazNaWebstranku, 0, SWT.RIGHT);
		fd_btnSave.left = new FormAttachment(lblNazovSystemu, 0, SWT.LEFT);
		btnSave.setLayoutData(fd_btnSave);
		
				btnSave.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						checkInput();
						try {
							if (!editMode)
								DAOFactory.INSTANCE.getBookmarkDao()
										.insertBookmark(new Bookmark(inputUserBookmarkName.getText(), inputUserURL.getText()));
							else {
								Bookmark bm = (Bookmark) WindowFactory.INSTANCE.getWindow_Browser().getTree()
										.getLastSelectedPathComponent();
								bm.setName(inputUserBookmarkName.getText());
								bm.setUrl(inputUserURL.getText());
								DAOFactory.INSTANCE.getBookmarkDao().editBookmark(bm);
							}
						} catch (FileNotFoundException fnfEx) {
							fnfEx.printStackTrace();
						} finally {
							shell.close();
						}
					}
				});
		btnCancel = new Button(shell, SWT.NONE);
		fd_btnSave.bottom = new FormAttachment(btnCancel, 0, SWT.BOTTOM);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(100, -136);
		fd_btnCancel.left = new FormAttachment(btnSave, 6);
		fd_btnCancel.top = new FormAttachment(label, 6);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		btnCancel.setText("Zruöiù");
		if (editMode) {
			inputUserBookmarkName.setText(
					((Bookmark) WindowFactory.INSTANCE.getWindow_Browser().getTree().getLastSelectedPathComponent())
							.getName());
			inputUserURL.setText(
					((Bookmark) WindowFactory.INSTANCE.getWindow_Browser().getTree().getLastSelectedPathComponent())
							.getUrl());
			btnSave.setText("Uloûiù zmeny");
		} else {
			btnSave.setText("Pridaù systÈm");
			inputUserURL.setText("http://");
		}
	}

	private void checkInput() {
		String[] missingInputs = null;
		if (inputUserBookmarkName.getText().equals("")) {
			missingInputs = new String[1];
			missingInputs[0] = "n·zov systÈmu";
			if (inputUserURL.getText().equals("")) {
				String[] missingInputs2 = { "n·zov systÈmu,", "adresu URL" };
				missingInputs = missingInputs2;
			}
		} else {
			if (inputUserURL.getText().equals("")) {
				missingInputs = new String[1];
				missingInputs[0] = "adresu URL";
			}
		}
		if (missingInputs != null) {
			InputMissingDialog popUpMissingInputs = new InputMissingDialog(shell, SWT.DIALOG_TRIM, missingInputs);
			popUpMissingInputs.open();
			return;
		}
	}

}
