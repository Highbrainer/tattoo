package com.jeevaneo.pdf.tattoo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.jeevaneo.pdf.tattoo.Activator;

public class AboutHandler {
	@Execute
	public void execute(Shell shell) {
		MessageDialog.openInformation(shell, "About", "Pdf Tattoo for Maif\nVersion " + Activator.getContext().getBundle().getVersion().toString());
	}
}
