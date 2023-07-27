insert into users(id, created_at, created_by, modified_at, modified_by, nickname, password, profile_image_url, deleted) values(1, now(), 'nkd0310', now(), 'nkd0310', 'donAdmin', '$2a$12$PTyJWbp/Oj9meNZB2TL0wu3U5WCy9FQJpVV2XCPwoEv/kPRfk448K', 'aws.com', false);
insert into users(id, created_at, created_by, modified_at, modified_by, nickname, password, profile_image_url, deleted) values(2, now(), 'nkd0310', now(), 'nkd0310', 'donUser', '$2a$12$PTyJWbp/Oj9meNZB2TL0wu3U5WCy9FQJpVV2XCPwoEv/kPRfk448K', 'aws.com', false);

insert into role(id, name, description) values (1, 'ROLE_ADMIN', '관리자');
insert into role(id, name, description) values (2, 'ROLE_USER', '사용자');

insert into user_role(id, user_id, role_id) values (1, 1, 1);
insert into user_role(id, user_id, role_id) values (2, 1, 2);
insert into user_role(id, user_id, role_id) values (3, 2, 2);

insert into authority(id, name, description) values (1, 'READ_USER', '사용자 조회');
insert into authority(id, name, description) values (2, 'WRITE_USER', '사용자 쓰기');

insert into user_authority(id, user_id, authority_id) values (1, 1, 1);