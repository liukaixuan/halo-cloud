package com.guzzservices.manager.impl.wordFilter;

/**
 * 过滤词引擎- 过滤词实体对象
 */
public class Word {
	
	/**
	 * 标识
	 */
	private int id;
	
	/**
	 * 过滤词
	 */
	private String word;
	
	/**
	 * 过滤级别
	 */
	private int level;
	
	/**
	 * 链表中下一节点
	 */
	private Word next;
	
	public static String WORD_COLOR="color";
	public static String WORD_LEVEL="level";
	public static String WORD_GROUP="group";
	public static String WORD_ID="id";
	public static String WORD_SYSTEM_GROUP="systemGroup";

	public Word getNext() {
		return next;
	}

	public void setNext(Word next) {
		this.next = next;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String toString() {
		StringBuffer buff=new StringBuffer(64);
		buff.append(word).append("/").append(level);
		return buff.toString();
	}

}
