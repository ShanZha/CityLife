package com.fourkkm.citylife;

public interface IModoerManager<E> {

	public void add(E e);

	public void remove(E e);

	public void clear();

	public void destroy();
}
