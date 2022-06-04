

--P1 reimbursements

--User stuff
CREATE TABLE user_roles (
	id varchar NOT NULL,
	role varchar UNIQUE,



	CONSTRAINT pk_users_roles
		primary key (id)
);

DROP TABLE IF EXISTS users;
CREATE TABLE users(
	id varchar,
	username varchar UNIQUE NOT null,
	email varchar UNIQUE NOT NULL,
	password varchar NOT NULL,
	given_name varchar NOT NULL,
	surname varchar NOT NULL,
	is_active boolean,
	role_id varchar,

	CONSTRAINT pk_users_id
		PRIMARY KEY (id),

	CONSTRAINT fk_role_id
		FOREIGN KEY (role_id) REFERENCES user_roles(id)
);






--Reimbursements

CREATE TABLE reimbursement_types(
	id varchar,
	type varchar,

	CONSTRAINT pk_type_id
		PRIMARY key(id)
);
CREATE TABLE reimbursement_statuses(
	id varchar,
	status varchar unique,

	CONSTRAINT pk_status_id
		PRIMARY KEY (id)
);
CREATE TABLE reimbursements(
	id varchar,
	amount numeric(6,2),
	submitted timestamp NOT NULL,
	resolved timestamp,
	description varchar NOT NULL,
	receipt bytea,
	payment_id varchar,
	author_id varchar NOT NULL, --FOREIGN KEY
	resolver_id varchar, --foreign key,
	status_id varchar, --foreign key
	type_id varchar NOT NULL, --foreign key


	CONSTRAINT fk_author_id
		FOREIGN KEY (author_id) REFERENCES users(id),
	CONSTRAINT fk_resolver_id
		FOREIGN KEY (resolver_id) REFERENCES users(id),
	CONSTRAINT fk_status_id
		FOREIGN KEY (status_id) REFERENCES reimbursement_statuses(id),
	CONSTRAINT fk_type_id
		FOREIGN KEY (type_id) REFERENCES reimbursement_types(id)


);






