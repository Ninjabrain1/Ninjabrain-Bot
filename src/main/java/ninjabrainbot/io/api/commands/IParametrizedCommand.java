package ninjabrainbot.io.api.commands;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public interface IParametrizedCommand<TArgs> {

	String name();

	Iterable<IAction> mapToActions(IDomainModel domainModel, IDataState dataState, TArgs args);

}
