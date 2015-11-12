package com.cfo.stock.web.rest.common;

public enum ProductType {
	
	ITOUGU("1", "itougu"),//爱投顾
	JRJCGBB("1", "cgbb");//金融界炒股必备
	
	private final String value;
	private final String name;
	
	private ProductType(String value,String name) {
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
		
	
}
