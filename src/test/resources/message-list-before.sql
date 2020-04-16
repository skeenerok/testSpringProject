delete from message;
insert into message(id, text, tag, user_id) values
(1,'first', 'my-tag1', 1),
(2,'second', 'my-tag1', 1),
(3,'third', 'my-tag3', 1),
(4,'fourth', 'my-tag4', 2);

alter sequence hibernate_sequence restart with 10;