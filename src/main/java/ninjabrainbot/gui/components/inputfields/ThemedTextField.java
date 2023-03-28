package ninjabrainbot.gui.components.inputfields;

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
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class ThemedTextField extends JTextField implements ThemedComponent {

	protected ObservableField<String> validatedProcessedText = new ObservableField<String>();

	private WrappedColor bgCol;
	private WrappedColor fgCol;

	public ThemedTextField(StyleManager styleManager) {
		super();
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
		setAlignmentX(1);
		styleManager.registerThemedComponent(this);

		((PlainDocument) getDocument()).setDocumentFilter(new InputVerifier());

		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setText(preProcessText(getText()));
			}
		});

		bgCol = styleManager.currentTheme.COLOR_HEADER;
		fgCol = styleManager.currentTheme.TEXT_COLOR_HEADER;
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

	public void updateColors() {
		Color bg = getBackgroundColor();
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor();
		if (fg != null) {
			setForeground(fg);
			setCaretColor(fg);
		}
	}

	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
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

	public void setBackgroundColor(WrappedColor color) {
		bgCol = color;
	}

	public void setForegroundColor(WrappedColor color) {
		fgCol = color;
	}

	protected Color getBackgroundColor() {
		return bgCol.color();
	}

	protected Color getForegroundColor() {
		return fgCol.color();
	}
}