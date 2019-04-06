package com.misterpemodder.customgamerules.api.rule.value;

@FunctionalInterface
public interface ValueValidator<V> {
  static final ValueValidator<?> ALWAYS_VALID = v -> true;

  @SuppressWarnings("unchecked")
  static <T> ValueValidator<T> alwaysValid() {
    return (ValueValidator<T>) ALWAYS_VALID;
  }

  /**
   * Checks if the given value is valid.
   * 
   * @param value The value to check.
   * @return true if valid, false otherwise.
   */
  boolean isValid(V value);
}
