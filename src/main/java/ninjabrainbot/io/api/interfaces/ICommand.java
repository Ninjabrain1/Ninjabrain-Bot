package ninjabrainbot.io.api.interfaces;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public interface ICommand extends IApiCommand {

	Iterable<IAction> mapToActions(IDomainModel domainModel, IDataState dataState);

}
