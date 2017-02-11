package windows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

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

import popups.PopUpInfo;
import popups.PopUpInputMissing;
import entities.Bookmark;
import factories.DAOFactory;
import factories.WindowBrowserFactory;

public class WindowAddBookmark extends Dialog {

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

	public WindowAddBookmark(Shell parent, int style, boolean editMode) {
		super(parent, style);
		setText("Pridanie novÈho systÈmu");
		this.editMode = editMode;
		this.parentShell = parent;
	}

	public Object open() {
		WindowBrowser browser = WindowBrowserFactory.INSTANCE
				.getWindow_Browser();
		/*if (browser.getCurrentSelection() == null) {
			currentBookmark = null;
		} else {
			currentBookmark = browser.getBookmarkFromString().get(
					browser.getCurrentSelection().getText());
		}*/
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
		shell.setText("Pridanie novÈho systÈmu");
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		shell.setSize(480, 122);
		Label lblNazovSystemu = new Label(shell, SWT.NONE);
		lblNazovSystemu.setLayoutData(new RowData(SWT.DEFAULT, 16));
		lblNazovSystemu.setText("N·zov systÈmu:");
		inputUserBookmarkName = new Text(shell, SWT.BORDER);
		inputUserBookmarkName.setLayoutData(new RowData(260, SWT.DEFAULT));
		Label lblOdkazNaWebstranku = new Label(shell, SWT.NONE);
		lblOdkazNaWebstranku.setText("Odkaz na webstr·nku systÈmu:");
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
		btnCancel.setText("Zruöiù");
		if (editMode) {
			inputUserBookmarkName.setText(currentBookmark.getName());
			inputUserURL.setText(currentBookmark.getUrl());
			btnSave.setText("Uloûiù zmeny");
		} else {
			btnSave.setText("Pridaù systÈm");
			inputUserURL.setText("http://");
		}
		
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		//		checkBookmarkExistence();
				checkInput();
				try {
					if (!editMode)
						DAOFactory.INSTANCE.getBookmarkDao().insertBookmark(
								new Bookmark(inputUserBookmarkName.getText(),
										inputUserURL.getText()));
					else
						DAOFactory.INSTANCE.getBookmarkDao().editBookmark(
								currentBookmark,
								new Bookmark(inputUserBookmarkName.getText(),
										inputUserURL.getText()));

				} catch (FileNotFoundException fnfEx) {
					fnfEx.printStackTrace();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
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
			PopUpInputMissing popUpMissingInputs = new PopUpInputMissing(shell,
					SWT.DIALOG_TRIM, missingInputs);
			popUpMissingInputs.open();
			return;
		}
	}

	// private void checkBookmarkExistence() {
	// WindowBrowser browser = WindowBrowserFactory.INSTANCE
	// .getWindow_Browser();
	// Set<String> bookmarkNameSet = browser.getBookmarkFromString().keySet();
	// for (String bookmarkName : bookmarkNameSet) {
	// if (bookmarkName.equals(inputUserBookmarkName.getText())
	// && browser.getBookmarkFromString().get(
	// inputUserBookmarkName.getText()) != currentBookmark) {
	// PopUpInfo popUpItemExist = new PopUpInfo(shell,
	// SWT.DIALOG_TRIM, inputUserBookmarkName.getText()
	// + " uû existuje, zadaj in˝ vstup!!!",
	// "Vstup uû exisstuje");
	// popUpItemExist.open();
	// return;
	// }
	// }
	// }
}
