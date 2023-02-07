-- -- insert into public.houses(id, built_date, created_at, floors_count, material, name, status, updated_at, user_id)
-- -- values (2, '1999', '2023-02-02 20:03:12.183292', 4, 0, 'House', 'CREATED', '2023-02-02 20:03:12.183292', 1);
--
-- INSERT INTO public.users (id primary key, age, created_at, first_name, gender, last_name, updated_at, email, status)
-- VALUES (1, 30, '2023-02-02 20:03:12.183292', 'Alex', 'MALE', 'Smith', '2023-02-02 20:03:12.183292', 'test@test.com', 'CREATED');
-- @formatter:off
-- // Execute below query first
INSERT INTO public.driver_licenses (id, created_at, received_at, status, updated_at, driver_id) VALUES ('106 САВ', '2023-02-07 15:44:59.241971', '2013-06-24', 'CREATED', null, 1);
-- // Execute below query after driver_licenses
INSERT INTO public.drivers (id, birthday, created_at, driver_order_status, name, phone_number, status, surname, updated_at, license_id) VALUES (1, '1991-10-20', '2023-02-07 15:44:08.579306', 'FREE', 'Petr', '191-11-11', 'CREATED', 'Kochkin', null, 1);
-- // Execute below query after drivers
INSERT INTO public.cars (id, baby_chair, car_class, color, created_at, name, seats, state_number, status, updated_at, vehicle_year, driver_id) VALUES (1, false, 3, 3, '2023-01-30 20:41:03.312403', 'BMW', 2, 'А565АА498', 'CREATED', null, 2012, 1);
INSERT INTO public.work_shifts (id, closed_at, created_at, grade, status, updated_at, car_id, driver_id) VALUES (2, null, '2023-02-07 15:45:17.273824', 1, 'CREATED', null, 1, 1);
-- @formatter:on
