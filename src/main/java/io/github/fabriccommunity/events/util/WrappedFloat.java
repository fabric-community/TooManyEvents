package io.github.fabriccommunity.events.util;

/**
 * A wrapper around a mutable float. Useful for modifying float values in events, as AtomicFloat doesn't exist.<br/>
 * Note that this is simpler and more lightweight than an atomic value.
 * @author Valoeghese
 */
public final class WrappedFloat {
	public WrappedFloat() {
		this(0.0f);
	}

	public WrappedFloat(float initialValue) {
		this.value = initialValue;
	}

	private float value;

	/**
	 * Sets the value stored in this {@link WrappedFloat}.
	 */
	public float get() {
		return this.value;
	}

	/**
	 * Sets the value stored in this {@link WrappedFloat}.
	 */
	public void set(float value) {
		this.value = value;
	}
}
