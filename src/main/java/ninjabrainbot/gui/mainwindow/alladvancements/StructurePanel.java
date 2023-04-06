package ninjabrainbot.gui.mainwindow.alladvancements;

import javax.swing.ImageIcon;
import javax.swing.border.MatteBorder;

import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.ColumnLayout;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class StructurePanel extends ThemedPanel implements IDisposable {

	ThemedLabel location;
	ThemedLabel nether;
	ThemedLabel angle;

	private final boolean showBorder;
	private final WrappedColor borderColor;

	private final DisposeHandler disposeHandler = new DisposeHandler();
	private Subscription angleSubscription;

	public StructurePanel(StyleManager styleManager, IObservable<StructurePosition> structurePosition, ImageIcon icon, boolean showBorder) {
		super(styleManager);
		setOpaque(true);
		ThemedLabel iconLabel = new ThemedLabel(styleManager, true);
		iconLabel.setIcon(icon);
		location = new ThemedLabel(styleManager, true);
		nether = new ThemedLabel(styleManager, true);
		angle = new ThemedLabel(styleManager, true);
		RemoveStructureButton removeStructureButton = new RemoveStructureButton(styleManager, structurePosition);

		ColumnLayout layout = new ColumnLayout(0);
		layout.setRelativeWidth(iconLabel, 0.4f);
		layout.setRelativeWidth(location, 1.9f);
		layout.setRelativeWidth(nether, 1.7f);
		layout.setRelativeWidth(angle, 0.8f);
		layout.setRelativeWidth(removeStructureButton, 0.4f, true);
		setLayout(layout);

		add(iconLabel);
		add(location);
		add(nether);
		add(angle);
		add(removeStructureButton);

		this.showBorder = showBorder;
		borderColor = styleManager.currentTheme.COLOR_DIVIDER;
		setBackgroundColor(styleManager.currentTheme.COLOR_SLIGHTLY_WEAK);

		location.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		nether.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		angle.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		removeStructureButton.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);

		onStructurePositionUpdated(structurePosition.get());
		disposeHandler.add(structurePosition.subscribeEDT(this::onStructurePositionUpdated));
		disposeHandler.add(removeStructureButton);
	}

	public void onStructurePositionUpdated(StructurePosition structurePosition) {
		if (structurePosition != null) {
			location.setText(String.format("(%.0f, %.0f)", structurePosition.xInOverworld(), structurePosition.zInOverworld()));
			nether.setText(String.format("(%.0f, %.0f)", structurePosition.xInNether(), structurePosition.zInNether()));
			angle.setText(String.format("%.1f", structurePosition.getTravelAngle()));
		} else {
			location.setText("");
			nether.setText("");
			angle.setText("");
		}

		if (angleSubscription != null) {
			angleSubscription.dispose();
			angleSubscription = null;
		}
		if (structurePosition != null)
			angleSubscription = structurePosition.whenRelativePlayerPositionChanged().subscribeEDT(this::onStructurePositionUpdated);
	}

	@Override
	public void updateColors() {
		super.updateColors();
		if (showBorder)
			setBorder(new MatteBorder(0, 0, 1, 0, borderColor.color()));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
		if (angleSubscription != null)
			angleSubscription.dispose();
	}
}
