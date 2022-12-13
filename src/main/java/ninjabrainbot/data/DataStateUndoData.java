package ninjabrainbot.data;

import java.util.List;

import ninjabrainbot.data.divine.DivineContext;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.ThrowSet;

public class DataStateUndoData {

	final List<IThrow> eyeThrows;
	final IThrow playerPos;
	final Fossil fossil;

	public DataStateUndoData(ThrowSet throwSet, IThrow playerPos, DivineContext divineContext) {
		eyeThrows = throwSet.toList();
		this.playerPos = playerPos;
		fossil = divineContext.getFossil();
	}

}
