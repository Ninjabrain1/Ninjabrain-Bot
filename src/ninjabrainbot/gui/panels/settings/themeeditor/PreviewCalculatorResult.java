package ninjabrainbot.gui.panels.settings.themeeditor;

import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.stronghold.Chunk;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;

public class PreviewCalculatorResult implements ICalculatorResult {

	List<ChunkPrediction> predictions = new ArrayList<ChunkPrediction>();
	
	public PreviewCalculatorResult(McVersion version) {
		IObservable<IThrow> playerPos = new ObservableField<IThrow>(new PreviewThrow(0, 1950));
		predictions.add(createPrediction(-2, 109, 1, playerPos, version));
		predictions.add(createPrediction(-59, 92, 0.75, playerPos,version));
		predictions.add(createPrediction(-69, 89, 0.5, playerPos, version));
		predictions.add(createPrediction(-49, 95, 0.25, playerPos, version));
		predictions.add(createPrediction(-79, 86, 0, playerPos, version));
	}
	
	private ChunkPrediction createPrediction(int x, int z, double certainty, IObservable<IThrow> playerPos, McVersion version) {
		ChunkPrediction c = new ChunkPrediction(new Chunk(x, z), playerPos, version);
		c.chunk.weight = certainty;
		return c;
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public ChunkPrediction getBestPrediction() {
		return predictions.get(0);
	}

	@Override
	public List<ChunkPrediction> getTopPredictions() {
		return predictions;
	}

	@Override
	public boolean success() {
		return true;
	}

}
