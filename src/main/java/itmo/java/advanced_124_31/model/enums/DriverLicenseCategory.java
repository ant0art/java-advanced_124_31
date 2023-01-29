package itmo.java.advanced_124_31.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DriverLicenseCategory {
	A("Мотоциклы"),
	A1("Легкие мотоциклы"),
	B("Легковые автомобили, небольшие грузовики (до 3,5 тонн)"),
	B1("Трициклы"),
	C("Грузовые автомобили (от 3,5 тонн)"),
	C1("Средние грузовики (от 3,5 до 7,5 тонн)"),
	D("Автобусы"),
	D1("Небольшие автобусы"),
	BE("Мотоциклы"),
	CE("Грузовые автомобили с прицепом"),
	C1E("Средние грузовики с прицепом"),
	DE("Автобусы с прицепом"),
	D1E("Небольшие автобусы с прицепом"),
	M("Мопеды"),
	Tm("Трамваи"),
	Tb("Троллейбусы");

	public final String description;
}
