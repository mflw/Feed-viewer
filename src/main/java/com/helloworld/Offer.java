package com.helloworld;

public class Offer {
	private boolean available;
	private long id;
	private String name;
	private int price;
	private Category category;

	private String url;
	private String description;

	public Offer(long id, boolean available, String name, Category category, int price) {
		this.id = id;
		this.available = available;
		this.name = name;
		this.price = price;
		this.category = category;
	}

	public long getId() {
    	return this.id;
    }

    public boolean getAvailable() {
    	return this.available;
    }

    public String getName() {
    	return this.name;
    }

    public long getCategoryId() {
    	return category.getId();
    }

    public int getPrice() {
    	return this.price;
    }


}