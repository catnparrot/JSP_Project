CREATE TABLE t_member(
	id varchar2(10) PRIMARY KEY,
	pwd varchar2(10),
	name varchar2(50),
	email varchar2(50),
	joinDate DATE DEFAULT sysdate
);

INSERT INTO t_member
VALUES('hong', '1212', '홍길동', 'hong@gmail.com', sysdate);

INSERT INTO t_member
VALUES('lee', '1212', '이순신', 'lee@test.com', sysdate);

INSERT INTO t_member
VALUES('kim', '1212', '김유신', 'kim@jweb.com', sysdate);
COMMIT;

SELECT * FROM t_member;