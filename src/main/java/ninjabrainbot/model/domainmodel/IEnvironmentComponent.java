package ninjabrainbot.model.domainmodel;

/**
 * A data component that represents the state of an external variable that cannot be controlled,
 * such as the Minecraft world. IEnvironmentComponents are not controlled by domain model reset
 * or undo actions. Can only be modified under the DomainModel write lock.
 */
public interface IEnvironmentComponent<T> extends IDomainModelComponent<T> {
}
