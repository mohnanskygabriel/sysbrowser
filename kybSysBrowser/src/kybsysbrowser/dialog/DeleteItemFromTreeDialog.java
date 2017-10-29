package kybsysbrowser.dialog;

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

import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.WindowFactory;

public class DeleteItemFromTreeDialog extends Dialog {

	private Object result;
	private Shell shell;

	public DeleteItemFromTreeDialog(Shell parent, int style) {
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
		shell.setSize(469, 109);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		Label lblMessage = new Label(shell, SWT.WRAP | SWT.CENTER);
		FormData fd_lblMessage = new FormData();
		fd_lblMessage.right = new FormAttachment(0, 453);
		fd_lblMessage.top = new FormAttachment(0, 10);
		fd_lblMessage.left = new FormAttachment(0, 10);
		lblMessage.setLayoutData(fd_lblMessage);
		Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_separator = new FormData();
		fd_separator.bottom = new FormAttachment(0, 44);
		fd_separator.right = new FormAttachment(0, 453);
		fd_separator.top = new FormAttachment(0, 29);
		fd_separator.left = new FormAttachment(0, 10);
		separator.setLayoutData(fd_separator);
		Button yesButton = new Button(shell, SWT.PUSH);
		FormData fd_yesButton = new FormData();
		fd_yesButton.top = new FormAttachment(0, 50);
		fd_yesButton.left = new FormAttachment(0, 10);
		yesButton.setLayoutData(fd_yesButton);
		yesButton.setText("¡no");
		Button noButton = new Button(shell, SWT.PUSH);
		FormData fd_noButton = new FormData();
		fd_noButton.top = new FormAttachment(0, 50);
		fd_noButton.left = new FormAttachment(0, 47);
		noButton.setLayoutData(fd_noButton);
		noButton.setText("Nie");

		Object selectionInTree = WindowFactory.INSTANCE.getWindow_Browser().getTree().getLastSelectedPathComponent();

		if (selectionInTree.getClass() == Bookmark.class)
			lblMessage.setText("UrËite chces vymazaù poloûku " + selectionInTree.toString()
					+ " ? Vymaû˙ sa aj pridelenÈ poËÌtaËe v zozname!");
		else
			lblMessage.setText("UrËite chces vymazaù poloûku " + selectionInTree.toString() + " ?");

		yesButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (selectionInTree.getClass() == Bookmark.class) {
						DAOFactory.INSTANCE.getBookmarkDao().deleteBookmark((Bookmark) selectionInTree);
					} else {
						DAOFactory.INSTANCE.getPCDAO().deletePC((PC) selectionInTree);
					}
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
