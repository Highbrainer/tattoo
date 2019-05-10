package com.jeevaneo.pdf.tattoo.parts;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.jeevaneo.pdf.tattoo.PdfBox;

public class SamplePart {

	private TableViewer tableViewer;

	@Inject
	private MPart part;

	private PdfBox pdfBox;

	public SamplePart() throws IOException {
		pdfBox = new PdfBox();
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

//		Text txtInput = new Text(parent, SWT.BORDER);
//		txtInput.setMessage("Enter text to mark part as dirty");
//		txtInput.addModifyListener(e -> part.setDirty(true));
//		txtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(parent, SWT.None);
		label.setText("Fichier>Ouvrir ou Ctrl+O ou Glissez-déposez le fichier à 'tatouer' sur la zone ci-dessous...");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);

		tableViewer.getTable().setHeaderVisible(true);

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(createInitialDataModel());
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		new TableColumn(tableViewer.getTable(), SWT.None).setText("Fichier d'origine");
		new TableColumn(tableViewer.getTable(), SWT.None).setText("Fichier tatoué");
		new TableColumn(tableViewer.getTable(), SWT.None).setText("Heure");

		final int nBColumns = 3;
		for (int i = 0; i < nBColumns - 1; i++) {
			TableColumn col = tableViewer.getTable().getColumn(i);
			col.pack();
			tableColumnLayout.setColumnData(col, new ColumnWeightData(20, 200, true));
		}
		{
			TableColumn col = tableViewer.getTable().getColumn(2);
			col.pack();
			tableColumnLayout.setColumnData(col, new ColumnWeightData(2, 100, true));
		}

		tableViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent event) {
				Point pt = new Point(event.x, event.y);
				TableItem item = tableViewer.getTable().getItem(pt);
				if (item == null)
					return;
				for (int i = 0; i < nBColumns; i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(pt)) {
						int index = tableViewer.getTable().indexOf(item);
						String filename = item.getText(i);
						System.out.println("Item " + index + "-" + i + "=> " + filename);
						if (filename == null) {
							return;
						}
						File file = new File(filename);
						if (!file.exists()) {
							return;
						}
						try {
							Desktop.getDesktop().open(file);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		});

		// Allow data to be copied or moved to the drop target
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(tableViewer.getTable(), operations);

		// Receive data in Text or File format
		final TextTransfer textTransfer = TextTransfer.getInstance();
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[] { fileTransfer, textTransfer };
		target.setTransfer(types);

		target.addDropListener(new DropTargetListener() {
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
				// will accept text but prefer to have files dropped
				for (int i = 0; i < event.dataTypes.length; i++) {
					if (fileTransfer.isSupportedType(event.dataTypes[i])) {
						event.currentDataType = event.dataTypes[i];
						// files should only be copied
						if (event.detail != DND.DROP_COPY) {
							event.detail = DND.DROP_NONE;
						}
						break;
					}
				}
			}

			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
				if (textTransfer.isSupportedType(event.currentDataType)) {
					// NOTE: on unsupported platforms this will return null
					Object o = textTransfer.nativeToJava(event.currentDataType);
					String t = (String) o;
					if (t != null)
						System.out.println(t);
				}
			}

			public void dragOperationChanged(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
				// allow text to be moved but files should only be copied
				if (fileTransfer.isSupportedType(event.currentDataType)) {
					if (event.detail != DND.DROP_COPY) {
						event.detail = DND.DROP_NONE;
					}
				}
			}

			public void dragLeave(DropTargetEvent event) {
			}

			public void dropAccept(DropTargetEvent event) {
			}

			public void drop(DropTargetEvent event) {
				if (textTransfer.isSupportedType(event.currentDataType)) {
					String text = (String) event.data;
					TableItem item = new TableItem(tableViewer.getTable(), SWT.NONE);
					item.setText(0, text);
				}
				if (fileTransfer.isSupportedType(event.currentDataType)) {
					String[] files = (String[]) event.data;
					for (int i = 0; i < files.length; i++) {
						final String in = files[i];
						tattoo(in);

					}
				}
			}

		});

	}
	
	public void tattoo(final String in) {
		TableItem item = new TableItem(tableViewer.getTable(), SWT.NONE);
		item.setText(0, in);
		try {
			String out = in.replaceFirst("\\.pdf$", "-tattoo.pdf");
			pdfBox.tattoo(in, out);
			item.setText(1, out);
			item.setText(2, DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	@Persist
	public void save() {
		part.setDirty(false);
	}

	private List<String> createInitialDataModel() {
		return Arrays.asList();
	}
}