# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table item (
  serial                    varchar(255) not null,
  name                      varchar(255),
  make                      varchar(255),
  model                     varchar(255),
  upc                       varchar(255),
  assigned_to_name          varchar(255),
  constraint pk_item primary key (serial))
;

create table item_history (
  id                        bigint not null,
  user                      varchar(255),
  changed_date              timestamp,
  item_serial               varchar(255),
  constraint pk_item_history primary key (id))
;

create table user_account (
  name                      varchar(255) not null,
  email                     varchar(255),
  verified_status           varchar(255),
  constraint pk_user_account primary key (name))
;

create sequence item_seq;

create sequence item_history_seq;

create sequence user_account_seq;

alter table item add constraint fk_item_assignedTo_1 foreign key (assigned_to_name) references user_account (name) on delete restrict on update restrict;
create index ix_item_assignedTo_1 on item (assigned_to_name);
alter table item_history add constraint fk_item_history_item_2 foreign key (item_serial) references item (serial) on delete restrict on update restrict;
create index ix_item_history_item_2 on item_history (item_serial);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists item;

drop table if exists item_history;

drop table if exists user_account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists item_seq;

drop sequence if exists item_history_seq;

drop sequence if exists user_account_seq;

