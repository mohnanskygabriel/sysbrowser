package popups;

import java.io.FileNotFoundException;
import java.io.IOException;

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
import org.eclipse.swt.widgets.TreeItem;

import windows.WindowBrowser;
import factories.DAOFactory;
import factories.WindowBrowserFactory;

public class PopUpDeleteItemFromTreeDialog extends Dialog {

	private Object result;
	private Shell shell;
	private WindowBrowser browser = WindowBrowserFactory.INSTANCE
			.getWindow_Browser();

	/* TreeItem selectedTreeItem = browser.getCurrentSelection(); */

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
		/*
		 * try { selectedTreeItem.getParentItem().getText();
		 * lblMessage.setText("UrËite chces vymazaù poloûku " +
		 * selectedTreeItem.getText() + " ?"); } catch (NullPointerException ne)
		 * { lblMessage.setText("UrËite chces vymazaù poloûku " +
		 * selectedTreeItem.getText() +
		 * " ? Vymaû˙ sa aj pridelenÈ poËÌtaËe v zozname!"); }
		 */

		/*
		 * yesButton.addSelectionListener(new SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) { boolean
		 * isBookmark = true; if (!browser.getBookmarkFromString().containsKey(
		 * selectedTreeItem.getText())) isBookmark = false; try { if
		 * (isBookmark) { DAOFactory.INSTANCE.getBookmarkDao().deleteBookmark(
		 * browser.getBookmarkFromString() .get(browser.getCurrentSelection()
		 * .getText())); } else { DAOFactory.INSTANCE.getPCDAO().deletePC(
		 * browser.getPcFromTreeItem().get( selectedTreeItem)); } } catch
		 * (FileNotFoundException fNfEx) { PopUpFileNotFound popUpFnf = new
		 * PopUpFileNotFound(shell, SWT.DIALOG_TRIM,
		 * browser.getFileOfBookmarks() .getAbsolutePath()); popUpFnf.open(); }
		 * catch (IOException iox) { PopUpIOExceptionFileWriterError popUpIOEx =
		 * new PopUpIOExceptionFileWriterError( shell, SWT.DIALOG_TRIM, browser
		 * .getFileOfBookmarks().getAbsolutePath()); popUpIOEx.open(); } finally
		 * { browser.setCurrentSelection(null); shell.close(); } } });
		 */

		noButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}
}
