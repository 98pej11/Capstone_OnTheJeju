package capstone.jejuTourrecommend.spot.domain.mainSpot;

import capstone.jejuTourrecommend.common.exceptionClass.UserException;

import java.util.Arrays;

public enum Category {
	VIEW("뷰"),
	PRICE("가격"),
	FACILITY("편의시설"),
	SURROUND("서비스"),
	ALL("전체");

	private String name;

	Category(String name) {
		this.name = name;
	}

	public static Category fromName(String name) {
		return Arrays.stream(Category.values())
			.filter(category -> category.name.equals(name))
			.findAny()
			.orElseThrow(()->new UserException("올바른 카테고리 값이 아닙니다"));

	}

}
