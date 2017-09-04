
create table clients (
	id 						serial primary key,
	first_name				varchar(128),
	last_name				varchar(128),
	created_on				timestamp not null,
	created_by				varchar(128),				
	modified_on				timestamp,
	modified_by				varchar(128)
);

create table cities (
	id 						serial primary key,
	name						varchar(256),
	created_on				timestamp not null,
	created_by				varchar(128),				
	modified_on				timestamp,
	modified_by				varchar(128)
);

create table trips (
	id 						serial primary key,
	client_id				bigint not null references clients(id),
	city_id					bigint not null references cities(id),
	date_from				date not null,
	date_to					date not null,
	hotel_cost_daily		decimal,
	travel_cost				decimal,				
	created_on				timestamp not null,
	created_by				varchar(128),				
	modified_on				timestamp,
	modified_by				varchar(128)
);
