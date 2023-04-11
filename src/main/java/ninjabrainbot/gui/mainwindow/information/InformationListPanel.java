package ninjabrainbot.gui.mainwindow.information;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;

import ninjabrainbot.data.information.InformationMessage;
import ninjabrainbot.data.information.InformationMessageList;
import ninjabrainbot.event.ArrayListImplementingReadOnlyList;
import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.components.panels.ResizablePanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class InformationListPanel extends ResizablePanel implements ThemedComponent {

	final StyleManager styleManager;

	final List<InformationTextPanel> cachedInformationTextPanels = new ArrayList<>();

	public InformationListPanel(StyleManager styleManager, InformationMessageList informationMessageList) {
		styleManager.registerThemedComponent(this);
		setOpaque(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		this.styleManager = styleManager;

		SwingUtilities.invokeLater(() -> synchronizeInformationMessages(informationMessageList.get()));

		informationMessageList.subscribeEDT(this::onInformationMessageListChanged);
	}

	private void onInformationMessageListChanged(IReadOnlyList<InformationMessage> informationMessages) {
		// avoid concurrent modification when messages are synchronized on the UI thread later by copying the list
		ArrayListImplementingReadOnlyList<InformationMessage> informationMessagesCopy = new ArrayListImplementingReadOnlyList<>();
		informationMessages.forEach(informationMessagesCopy::add);
		SwingUtilities.invokeLater(() -> synchronizeInformationMessages(informationMessagesCopy));
	}

	private void synchronizeInformationMessages(IReadOnlyList<InformationMessage> informationMessages) {
		for (InformationTextPanel informationTextPanel : cachedInformationTextPanels) {
			informationTextPanel.setVisible(false);
		}
		int messageIndex = 0;
		for (InformationMessage informationMessage : informationMessages) {
			if (cachedInformationTextPanels.size() <= messageIndex) {
				InformationTextPanel newInformationTextPanel = new InformationTextPanel(styleManager);
				cachedInformationTextPanels.add(newInformationTextPanel);
				add(newInformationTextPanel);
			}
			InformationTextPanel informationTextPanel = cachedInformationTextPanels.get(messageIndex);
			informationTextPanel.setInformationMessage(informationMessage);
			informationTextPanel.setVisible(true);
			messageIndex++;
		}
		whenSizeModified.notifySubscribers(this);
	}

	@Override
	public void updateSize(StyleManager styleManager) {

	}

	@Override
	public void updateColors() {

	}

}
