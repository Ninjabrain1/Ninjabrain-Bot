package ninjabrainbot.util;

public class Modifiable<T> implements IModifiable<T> {

	protected ObservableProperty<T> whenModified;

	public Modifiable() {
		whenModified = new ObservableProperty<T>();
	}

	@Override
	public ISubscribable<T> whenModified() {
		return whenModified;
	}

}
