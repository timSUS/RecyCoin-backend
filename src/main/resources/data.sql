insert into client(id, is_active) values (1, true);
insert into client(id, is_active) values (2, false);
insert into client(id, is_active) values (3, true);

insert into distributor(id, is_active, max_tokens_per_day) values (1, true, 2);
insert into distributor(id, is_active, max_tokens_per_day) values (2, true, 5);
insert into distributor(id, is_active, max_tokens_per_day) values (3, true, 1000);
insert into distributor(id, is_active, max_tokens_per_day) values (4, false, 1000);