package popups;

import java.io.FileNotFoundException;

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

import entities.Bookmark;
import entities.PC;
import factories.DAOFactory;
import factories.WindowBrowserFactory;

public class PopUpDeleteItemFromTreeDialog extends Dialog {

	private Object result;
	private Shell shell;

	public PopUpDeleteItemFromTreeDialog(Shell parent, int style) {
		super(parent, style);
		setText("Zmazaù poloûku?");
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(300, 100);
		shell.setText(getText());
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label lblMessage = new Label(shell, SWT.WRAP);
		lblMessage.setLayoutData(new RowData(290, 30));
		Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new RowData(290, 10));
		Button yesButton = new Button(shell, SWT.PUSH);
		yesButton.setLayoutData(new RowData(100, SWT.DEFAULT));
		yesButton.setText("¡no");
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new RowData(80, 10));
		Button noButton = new Button(shell, SWT.PUSH);
		noButton.setLayoutData(new RowData(100, SWT.DEFAULT));
		noButton.setText("Nie");

		Object selectionInTree = WindowBrowserFactory.INSTANCE
				.getWindow_Browser().getTree().getSelectionPath()
				.getLastPathComponent();

		if (selectionInTree.getClass() == PC.class)
			lblMessage.setText("UrËite chces vymazaù poloûku "
					+ selectionInTree.toString()
					+ " ? Vymaû˙ sa aj pridelenÈ poËÌtaËe v zozname!");
		else
			lblMessage.setText("UrËite chces vymazaù poloûku "
					+ selectionInTree.toString() + " ?");

		yesButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (selectionInTree.getClass() == Bookmark.class) {
						DAOFactory.INSTANCE.getBookmarkDao().deleteBookmark(
								(Bookmark) selectionInTree);
					} else {
						DAOFactory.INSTANCE.getPCDAO().deletePC(
								(PC) selectionInTree);
					}
				} catch (FileNotFoundException fnfEx) {
					PopUpFileNotFound popupFnF = new PopUpFileNotFound(
							WindowBrowserFactory.INSTANCE.getWindow_Browser()
									.getShell(), SWT.DIALOG_TRIM,
							WindowBrowserFactory.INSTANCE.getWindow_Browser()
									.getFileOfBookmarks().getAbsolutePath());
					popupFnF.open();
				} finally {
					shell.close();
				}

			}
		});

		noButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}
}
