package ninjabrainbot.util;

public class Wrapper<T> {

	T value;

	public void set(Wrapper<T> other) {
		this.value = other.value;
	}

	public void set(T value) {
		this.value = value;
	}

	public T get() {
		return value;
	}

}