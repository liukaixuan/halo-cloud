package com.guzzservices.manager.impl.ip;

public interface BSTree<E extends Comparable<E>> {

	public static interface Visitor<E extends Comparable<E>> {
		void visit(E ele);
	}

	void insert(E ele);

	boolean search(E ele);
	
	CityMark searchMatchedIP(long ipValue) ;

	boolean isEmpty();

	boolean delete(E ele);

	void preOrder(Visitor<E> v);

	void inOrder(Visitor<E> v);

	void postOrder(Visitor<E> v);

	void levelOrder(Visitor<E> v);

}
