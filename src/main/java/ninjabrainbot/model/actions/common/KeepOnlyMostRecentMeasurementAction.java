package ninjabrainbot.model.actions.common;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.domainmodel.IListComponent;

public class KeepOnlyMostRecentMeasurementAction implements IAction {

    private final IListComponent<IEnderEyeThrow> throwList;
    private final IObservable<Boolean> locked;

    public KeepOnlyMostRecentMeasurementAction(IDataState dataState) {
        this(dataState.getThrowList(), dataState.locked());
    }

    public KeepOnlyMostRecentMeasurementAction(IListComponent<IEnderEyeThrow> throwList, IObservable<Boolean> locked) {
        this.throwList = throwList;
        this.locked = locked;
    }

    @Override
    public void execute() {
        if (locked.get())
            return;

        if (throwList.size() < 2)
            return;

        while (throwList.size() > 1)
            throwList.remove(throwList.get(0));
    }
}
