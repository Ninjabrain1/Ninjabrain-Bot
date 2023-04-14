package ninjabrainbot.model.domainmodel;

import ninjabrainbot.event.IObservable;

/**
 * A data component that cannot be set externally, but is instead inferred from DataComponents
 * and the EnvironmentState. Can only be modified under the DomainModel write lock.
 */
public interface IInferredComponent<T> extends IObservable<T> {
}
