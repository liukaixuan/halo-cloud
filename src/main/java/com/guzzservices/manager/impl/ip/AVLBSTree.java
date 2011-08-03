package com.guzzservices.manager.impl.ip;

public class AVLBSTree<E extends Comparable<E>> extends DefaultBSTree<E> {

	static class AVLTreeNode<E extends Comparable<E>> extends BSTNode<E> {
		int bf;// balance factor

		boolean marked;

		AVLTreeNode(E key) {
			super(key);
			bf = 0;
			marked = false;
		}
	}

	private static class BooleanHolder {
		BooleanHolder(boolean v) {
			value = v;
		}

		boolean value;
	}

	public void insert(E ele) {
		if (root == null) {
			root = new AVLTreeNode<E>(ele);
			return;
		}
		BooleanHolder bh = new BooleanHolder(false);

		root = _insert((AVLTreeNode<E>) root, ele, bh);
	}

	@Override
	public boolean delete(E ele) {
		if (root == null) {
			return false;
		}
		// we first marked the delete
		// this will make the delete procedure be more simple as insert
		if (!_mark_delete(ele))
			return false;
		BooleanHolder heightChange = new BooleanHolder(false);
		root = __real_delete((AVLTreeNode<E>) root, ele, heightChange);

		// we can assume it will always be deleted
		return true;
	}

	/**
	 * If we find the delete node have to child, we will exchange it value with
	 * its previous in-order sibling, and mark it , to help the real delete
	 * procedure know its marked and go to its left child.
	 * 
	 * @param ele
	 * @return
	 */
	private boolean _mark_delete(E ele) {
		BSTNode<E> p = root;
		int cmp;
		while (p != null && (cmp = p.key.compareTo(ele)) != 0) {
			if (cmp > 0)
				p = p.left;
			else
				p = p.right;
		}

		if (p == null) {
			// not found
			return false;
		}
		// we only take care this case
		if (p.left != null && p.right != null) {
			BSTNode<E> toMark = p;
			p = p.left;
			while (p.right != null) {
				p = p.right;
			}

			// do mark now

			toMark.key = p.key;
			// transfer the value to p;
			p.key = ele;
			((AVLTreeNode<E>) toMark).marked = true;
			((AVLTreeNode<E>) p).marked = false;
		}
		return true;
	}

	private BSTNode<E> __real_delete(AVLTreeNode<E> pointer, E ele, BooleanHolder heightChange) {
		int cmp = ele.compareTo(pointer.key);
		if (cmp == 0) {

			// we really found it
			// we can assume the pointer now have at most only one child.

			heightChange.value = true;

			if (pointer.left == null && pointer.right == null) {
				return null;
			} else {
				return pointer.left != null ? pointer.left : pointer.right;
			}
		} else if (cmp > 0 && !pointer.marked) {
			pointer.marked = false;// okay, we mark it back.
			pointer.right = __real_delete((AVLTreeNode<E>) pointer.right, ele,
					heightChange);
			if (heightChange.value) {
				if (pointer.bf == -1) {
					if (((AVLTreeNode<E>) pointer.left).bf <= 0)// 0 or -1
					{
						if (((AVLTreeNode<E>) pointer.left).bf == 0) {
							heightChange.value = false;// this case height will
														// not decrease
						}
						pointer = LL_Rotation(pointer);
					} else {
						pointer = LR_Rotation(pointer);
					}
				} else if (pointer.bf == 0) {
					pointer.bf = -1;
				} else {
					pointer.bf = 0;
					heightChange.value = false;// the height not decrease
				}
			}
			return pointer;
		} else {
			pointer.marked = false;// okay, we mark it back.
			pointer.left = __real_delete((AVLTreeNode<E>) pointer.left, ele,
					heightChange);
			if (heightChange.value) {
				if (pointer.bf == 1) {
					if (((AVLTreeNode<E>) pointer.right).bf >= 0)// 0 or 1
					{
						if (((AVLTreeNode<E>) pointer.right).bf == 0) {
							heightChange.value = false;// this case height will
														// not decrease
						}
						pointer = RR_Rotation(pointer);
					} else {
						pointer = RL_Rotation(pointer);
					}
				} else if (pointer.bf == 0) {
					pointer.bf = 1;
				} else {
					pointer.bf = 0;
					heightChange.value = false;// the height not decrease
				}
			}
			return pointer;
		}
	}

	private AVLTreeNode<E> _insert(AVLTreeNode<E> pointer, E ele, BooleanHolder heightAdded) {
		int cmp = ele.compareTo(pointer.key);
		if (cmp == 0) {
			throw new IllegalArgumentException("duplicate key");
		}
		if (cmp < 0) {
			if (pointer.left == null) {
				AVLTreeNode<E> node = new AVLTreeNode<E>(ele);
				node.bf = 0;
				pointer.left = node;
				if (pointer.right == null) {
					pointer.bf = -1;
					heightAdded.value = true;
				} else {
					pointer.bf = 0;// while no left child, the right tree can't
									// have more than two children
					heightAdded.value = false;
				}
				return pointer;
			}
			
			heightAdded.value = false;
			pointer.left = _insert((AVLTreeNode<E>) pointer.left, ele,
					heightAdded);
			if (heightAdded.value) {
				if (pointer.bf == -1) {
					if (((AVLTreeNode<E>) pointer.left).bf == -1) {
						// LL Rotation
						pointer = LL_Rotation(pointer);
					} else if (((AVLTreeNode<E>) pointer.left).bf == 1) {
						// LR Double Rotation
						pointer = LR_Rotation(pointer);
					}
					// can't be 0

					heightAdded.value = false;

				} else if (pointer.bf == 1) {
					pointer.bf = 0;
					heightAdded.value = false;
				} else {
					pointer.bf = -1;
				}
			}
			return pointer;
		} else {
			if (pointer.right == null) {
				AVLTreeNode<E> node = new AVLTreeNode<E>(ele);
				node.bf = 0;
				pointer.right = node;
				if (pointer.left == null) {
					pointer.bf = 1;
					heightAdded.value = true;
				} else {
					pointer.bf = 0;// while no right child, the left tree can't
									// have more than two children
					heightAdded.value = false;
				}
				return pointer;
			}
			
			pointer.right = _insert((AVLTreeNode<E>) pointer.right, ele,
					heightAdded);
			if (heightAdded.value) {
				if (pointer.bf == 1) {
					if (((AVLTreeNode<E>) pointer.right).bf == 1) {
						// RR Rotation
						pointer = RR_Rotation(pointer);

					} else if (((AVLTreeNode<E>) pointer.right).bf == -1) {
						// RL Double Rotation
						pointer = RL_Rotation(pointer);
					}
					// can't be 0

					heightAdded.value = false;

				} else if (pointer.bf == -1) {
					pointer.bf = 0;
					heightAdded.value = false;
				} else {
					pointer.bf = 1;

				}
			}
			return pointer;
		}
	}

	private AVLTreeNode<E> LR_Rotation(AVLTreeNode<E> pointer) {
		AVLTreeNode<E> a, b, c;
		a = pointer;
		b = (AVLTreeNode<E>) pointer.left;
		c = (AVLTreeNode<E>) b.right;

		b.right = c.left;
		c.left = b;
		a.left = c.right;
		c.right = a;

		if (c.bf == 1) {
			b.bf = -1;
			c.bf = 0;
			a.bf = 0;
		} else if (c.bf == -1) {
			a.bf = 1;
			b.bf = 0;
			c.bf = 0;
		} else if (c.bf == 0)// which means delete cause rotation
		{
			c.bf = b.bf = 0;
			a.bf = 0;
		}

		return c;
	}

	private AVLTreeNode<E> LL_Rotation(AVLTreeNode<E> pointer) {
		AVLTreeNode<E> a, b;
		a = pointer;
		b = (AVLTreeNode<E>) a.left;

		a.left = b.right;
		b.right = a;
		if (b.bf == 0) {
			b.bf = 1;
			// a's bf still be -1;
		} else if (b.bf == -1) {
			a.bf = b.bf = 0;
		}

		return b;
	}

	private AVLTreeNode<E> RL_Rotation(AVLTreeNode<E> pointer) {
		AVLTreeNode<E> a, b, c;
		a = pointer;
		b = (AVLTreeNode<E>) a.right;
		c = (AVLTreeNode<E>) b.left;

		b.left = c.right;
		c.right = b;
		a.right = c.left;
		c.left = a;

		if (c.bf == 1) {
			a.bf = -1;
			b.bf = c.bf = 0;
		} else if (c.bf == -1) {
			b.bf = 1;
			a.bf = c.bf = 0;
		} else// delete cause rotation
		{
			a.bf = b.bf = c.bf = 0;
		}

		return c;
	}

	private AVLTreeNode<E> RR_Rotation(AVLTreeNode<E> pointer) {
		AVLTreeNode<E> a, b;
		a = pointer;
		b = (AVLTreeNode<E>) a.right;

		a.right = b.left;
		b.left = a;

		if (b.bf == 0)// this means the remove cause RR_Rotation
		{
			b.bf = -1;
			// a.bf still be 1
		} else {
			a.bf = b.bf = 0;
		}

		return b;
	}

}
