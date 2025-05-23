package com.balanceeat.demo.domain.nutrition.entity;

public enum FoodCategory {
	GRAINS_AND_ROOT_PRODUCTS("곡류, 서류 제품"),
	FRUITS("과일류"),
	GRILLED("구이류"),
	SOUPS_AND_STEWS("국 및 탕류"),
	KIMCHI("김치류"),
	VEGETABLE_SIDE_DISHES("나물·숙채류"),
	LEGUMES_NUTS_SEEDS("두류, 견과 및 종실류"),
	NOODLES_AND_DUMPLINGS("면 및 만두류"),
	RICE_DISHES("밥류"),
	STIR_FRIED("볶음류"),
	BREAD_AND_SNACKS("빵 및 과자류"),
	RAW_SALADS("생채·무침류"),
	FISH_MEAT_EGGS("수·조·어·육류"),
	DAIRY_AND_ICE("유제품류 및 빙과류"),
	BEVERAGES_AND_TEA("음료 및 차류"),
	SAUCES_AND_SEASONINGS("장류, 양념류"),
	PICKLED_FOODS("장아찌·절임류"),
	PAN_FRIED("전·적 및 부침류"),
	SALTED_SEAFOOD("젓갈류"),
	BRAISED("조림류"),
	PORRIDGE_AND_SOUP("죽 및 스프류"),
	STEWS_AND_HOTPOTS("찌개 및 전골류"),
	STEAMED("찜류"),
	VEGETABLES_AND_SEAWEED("채소, 해조류"),
	FRIED("튀김류");
	
	private final String label;
	
	FoodCategory(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static FoodCategory fromLabel(String label) {
		for (FoodCategory category : values()) {
			if (category.label.equals(label)) {
				return category;
			}
		}
		throw new IllegalArgumentException("Unknown category label: " + label);
	}
	
	@Override
	public String toString() {
		return name();
	}
}
