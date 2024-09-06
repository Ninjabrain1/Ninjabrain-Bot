package ninjabrainbot.model.domainmodel;

import java.io.Serializable;

/**
 * Represents fundamental data in the data state, i.e. data that cannot be derived or inferred from other data in the data state.
 * Examples of fundamental data are player inputs, and an example of non-fundamental data is the calculated stronghold position,
 * since it can be derived from the ender eye throws. This distinction is meaningful because only fundamental data needs to be
 * saved, the rest of the data state can be recreated from the fundamental data. Also, fundamental data does not change based on
 * external factors (settings changes), but e.g. the calculated stronghold position might change if the user changes settings.
 * Write permissions of IFundamentalComponents are automatically handled by the DomainModel.
 * Any modifications to a IFundamentalComponent are automatically saved by the DomainModel, for the undo action to work.
 * The generic type T should be immutable to ensure that no modifications to the data go unnoticed by the domain model.
 */
public interface IFundamentalComponent<T, U extends Serializable> extends IDomainModelComponent<T> {

	T getAsImmutable();

	void set(T value);

	void reset();

	boolean contentEquals(T value);

	boolean isReset();

//	String uniqueId();

	U getAsSerializable();

	void setFromDeserializedObject(U deserialized);

}