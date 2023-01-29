package itmo.java.advanced_124_31.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DriverOrderStatus {

	ON_ORDER("На заказе"),
	FREE("Свободен");

	public final String description;
}
