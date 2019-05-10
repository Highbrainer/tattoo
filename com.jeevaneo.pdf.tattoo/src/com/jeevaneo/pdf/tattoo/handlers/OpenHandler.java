package com.jeevaneo.pdf.tattoo.handlers;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.jeevaneo.pdf.tattoo.parts.SamplePart;

public class OpenHandler {

	@Inject
	private EModelService modelService;

	@Inject
	private MApplication app;
	
	@Execute
	public void execute(Shell shell) throws IOException, URISyntaxException {

		FileDialog dialog = new FileDialog(shell);
		dialog.setFilterExtensions(new String[] { "*.pdf" });
		String in = dialog.open();
		if (null == in) {
			return;
		}

//		FileDialog dialog2 = new FileDialog(shell, SWT.SAVE);
//		dialog2.setFilterExtensions(new String[] { "*.pdf" });
//		String out = dialog2.open();
//		if (null == out) {
//			return;
//		}

		String out = in.replaceFirst("\\.pdf$", "-tattoo.pdf");

		MPart mpart = (MPart) modelService.find("com.jeevaneo.pdf.tattoo.part.sample", app);
		
		SamplePart samplePart = (SamplePart) mpart.getObject();
		samplePart.tattoo(in);
	}
}
