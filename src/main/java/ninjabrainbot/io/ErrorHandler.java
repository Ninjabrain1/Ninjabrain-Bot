package ninjabrainbot.io;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ErrorHandler {

	public void handleStartupException(Exception exception) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		exception.printStackTrace();
		String stackTrace = stringWriter.toString();
		JTextArea textArea = new JTextArea(stackTrace);
		JOptionPane.showMessageDialog(null, textArea, "An error occurred during startup", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
}
