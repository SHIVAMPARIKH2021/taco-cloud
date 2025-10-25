create table if not exists taco_order (
 id identity primary key,
 delivery_name varchar(50) not null,
 delivery_street varchar(50) not null,
 delivery_city varchar(50) not null,
 delivery_state varchar(2) not null,
 delivery_zip varchar(50) not null,
 cc_number varchar(16) not null,
 cc_expiration varchar(5) not null,
 cc_cvv varchar(3) not null, placed_at timestamp not null
 );

 create table if not exists taco(
 id identity primary key,
 name varchar(50) not null,
 taco_order bigint not null,
 taco_order_id bigint not null references taco_order(id),
 created_at timestamp not null
 );

 create table if not exists ingredients(
 id varchar(4) primary key,
 name varchar(25) not null,
 type varchar(10) not null
 );

 create table if not exists ingredient_reference(
 ingredient varchar(4) not null references ingredients(id),
 taco bigint not null,
 taco_id bigint not null
 );