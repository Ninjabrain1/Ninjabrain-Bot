package ninjabrainbot.data;

import java.util.List;

import ninjabrainbot.data.calculator.divine.DivineContext;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;
import ninjabrainbot.event.IObservableList;

public class DataStateUndoData {

	final List<IEnderEyeThrow> eyeThrows;
	final IEnderEyeThrow playerPos;
	final Fossil fossil;

	public DataStateUndoData(IObservableList<IEnderEyeThrow> throwSet, IEnderEyeThrow playerPos, DivineContext divineContext) {
//		eyeThrows = throwSet.toList();
//		this.playerPos = playerPos;
//		fossil = divineContext.getFossil();
		eyeThrows = null;
		this.playerPos = playerPos;
		fossil = divineContext.getFossil();
	}

}
