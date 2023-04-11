package ninjabrainbot.gui.mainwindow.alladvancements;

import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.style.StyleManager;

public class RemoveStructureButton extends FlatButton implements IDisposable {

	final DisposeHandler disposeHandler = new DisposeHandler();

	public RemoveStructureButton(StyleManager styleManager, IObservable<StructurePosition> structurePosition) {
		super(styleManager, "-");
		setBackgroundColor(styleManager.currentTheme.COLOR_SLIGHTLY_WEAK);
		setForegroundColor(styleManager.currentTheme.TEXT_COLOR_NEUTRAL);
		setHoverColor(styleManager.currentTheme.COLOR_EXIT_BUTTON_HOVER);
		//addActionListener(remove structure);

		updateVisibility(structurePosition.get());
		structurePosition.subscribe(this::updateVisibility);
	}

	private void updateVisibility(StructurePosition structurePosition) {
		setVisible(structurePosition != null);
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
