package ninjabrainbot.event;

public interface IObservableList<T> extends IObservable<IReadOnlyList<T>>, IReadOnlyList<T> {

	int size();

}
