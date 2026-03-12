package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.domainmodel.IListComponent;
import org.json.JSONObject;

public class CalculatorStateQuery implements IQuery {
	private final IDataState dataState;

	public CalculatorStateQuery(IDataState dataState) {
		this.dataState = dataState;
	}

	@Override
	public String get() {
		IListComponent<IEnderEyeThrow> throwList = dataState.getThrowList();
		int angleCount = throwList.size();
		JSONObject root = new JSONObject();
		root.put("boatState", dataState.boatDataState().boatState().get());
		root.put("adjustment", angleCount > 0 ? throwList.getLast().correctionIncrements() : 0);
		root.put("angleCount", angleCount);
		root.put("isAllAdvancementsModeEnabled", dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get());
		root.put("locked", dataState.locked().get());
		return root.toString();
	}

	@Override
	public boolean supportsSubscriptions() {
		return true;
	}
}
