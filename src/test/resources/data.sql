INSERT INTO drivers (id, birthday, created_at, driver_order_status, name, phone_number,
					 status, surname, updated_at, license_id)
VALUES (1, '1991-10-20', '2023-02-07 15:44:08.579306', 'FREE', 'Petr', '191-11-11',
		'CREATED', 'Kochkin', null, null);

INSERT INTO cars (id, baby_chair, car_class, color, created_at, name, seats, state_number,
				  status, updated_at, vehicle_year, driver_id)
VALUES (1, false, 3, 3, '2023-01-30 20:41:03.312403', 'BMW', 2, 'А565АА498', 'CREATED',
		null, 2012, null);

INSERT INTO cars (id, baby_chair, car_class, color, created_at, name, seats, state_number,
				  status, updated_at, vehicle_year, driver_id)
VALUES (2, true, 3, 4, '2023-01-30 20:41:03.510895', 'TOYOTA', 4, 'А551АА235', 'CREATED',
		null, 1997, 1);

INSERT INTO driver_licenses (id, created_at, received_at, status, updated_at, driver_id)
VALUES ('106 ААА', '2023-02-07 15:44:59.241971', '2013-06-24', 'CREATED', null, 1);

INSERT INTO work_shifts (id, closed_at, created_at, grade, status, updated_at, car_id,
						 driver_id)
VALUES (2, null, '2023-02-07 15:45:17.273824', 1, 'CREATED', null, 1, 1);
