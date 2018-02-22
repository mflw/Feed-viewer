package com.helloworld;
//package src.main.java.com.helloworld;

public class Category {
    long id;
    Long parentId;
    

    public Category(long id, long parentId) {
    	this.id = id;
    	this.parentId = parentId;
    }
    public Category(long id) {
        this.id = id;
        this.parentId = null;
    }

    public long getId() {
    	return this.id;
    }

    public long getParentId() {
    	return this.parentId;
    }
}
