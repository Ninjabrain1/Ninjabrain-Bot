package ninjabrainbot.data;

import java.util.List;

import ninjabrainbot.data.calculator.divine.DivineContext;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.event.IObservableList;

public class DataStateUndoData {

	final List<IThrow> eyeThrows;
	final IThrow playerPos;
	final Fossil fossil;

	public DataStateUndoData(IObservableList<IThrow> throwSet, IThrow playerPos, DivineContext divineContext) {
//		eyeThrows = throwSet.toList();
//		this.playerPos = playerPos;
//		fossil = divineContext.getFossil();
		eyeThrows = null;
		this.playerPos = playerPos;
		fossil = divineContext.getFossil();
	}

}
