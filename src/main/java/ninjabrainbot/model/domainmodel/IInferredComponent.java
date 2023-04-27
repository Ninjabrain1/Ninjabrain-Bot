package ninjabrainbot.model.domainmodel;

/**
 * A data component that cannot be set externally, but is instead inferred from DataComponents
 * and the EnvironmentComponents. Can only be modified under the DomainModel write lock.
 */
public interface IInferredComponent<T> extends IDomainModelComponent<T> {
}
