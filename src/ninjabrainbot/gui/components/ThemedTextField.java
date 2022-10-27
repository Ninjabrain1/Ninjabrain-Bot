package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;

public class ThemedTextField extends JTextField implements ThemedComponent {

	private static final long serialVersionUID = 1363577002580584264L;

	private ObservableField<String> validatedProcessedText = new ObservableField<String>();

	public ThemedTextField(StyleManager styleManager) {
		super();
		setBorder(BorderFactory.createEmptyBorder());
		setAlignmentX(1);
		styleManager.registerThemedComponent(this);

		((PlainDocument) getDocument()).setDocumentFilter(new InputVerifier());

		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setText(preProcessText(getText()));
			}
		});
	}

	protected String preProcessText(String text) {
		return text;
	}

	protected boolean verifyInput(String text) {
		return true;
	}

	public ISubscribable<String> whenTextChanged() {
		return validatedProcessedText;
	}

	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}

	public void updateColors(StyleManager styleManager) {
		Color bg = getBackgroundColor(styleManager.theme);
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor(styleManager.theme);
		if (fg != null)
			setForeground(fg);
	}

	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
	}

	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONG;
	}

	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_STRONG;
	}

	class InputVerifier extends DocumentFilter {
		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
			StringBuilder sb = new StringBuilder(currentText);

			String newText = preProcessText(sb.insert(offset, string).toString());

			if (verifyInput(newText)) {
				super.insertString(fb, offset, string, attr);
				validatedProcessedText.set(newText);
			}
		}

		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
			StringBuilder sb = new StringBuilder(currentText);

			String newText = preProcessText(sb.replace(offset, offset + length, "").toString());

			if (verifyInput(newText)) {
				super.remove(fb, offset, length);
				validatedProcessedText.set(newText);
			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
			StringBuilder sb = new StringBuilder(currentText);

			String newText = preProcessText(sb.replace(offset, offset + length, text).toString());

			if (verifyInput(newText)) {
				super.replace(fb, offset, length, text, attrs);
				validatedProcessedText.set(newText);
			}
		}
	}
}