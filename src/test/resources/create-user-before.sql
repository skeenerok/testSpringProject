delete from user_role;
delete from message;
delete from user_subscriptions;
delete from usr;

insert into  usr(id, active, password, username) values
(1,true, '$2a$08$y0zIAxZtgDgi6QvitzYJM.YUtJbYGpP48cafENkCWsC/OE.ifM2Pm', 'admin'),
(2,true, '$2a$08$y0zIAxZtgDgi6QvitzYJM.YUtJbYGpP48cafENkCWsC/OE.ifM2Pm', 'mike');

insert into  user_role(user_id, roles) values
(1,'USER'), (1, 'ADMIN'),
(2, 'USER');