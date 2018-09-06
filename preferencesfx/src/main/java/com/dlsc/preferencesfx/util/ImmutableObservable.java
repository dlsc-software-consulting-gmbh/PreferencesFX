package com.dlsc.preferencesfx.util;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ImmutableObservable<T> implements ObservableValue<T> {

  public final T value;

  public ImmutableObservable(T value) {
    this.value = value;
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public void addListener(ChangeListener listener) {}
  @Override
  public void removeListener(ChangeListener listener) {}
  @Override
  public void addListener(InvalidationListener listener) {}
  @Override
  public void removeListener(InvalidationListener listener) {}

}
