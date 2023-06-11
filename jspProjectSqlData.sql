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



CREATE TABLE t_Board(
	articleNO number(10) PRIMARY KEY,
	parentNO number(10) DEFAULT 0,
	title varchar2(500) NOT NULL,
	content varchar2(4000),
	imageFileName varchar2(100),
	writedate date DEFAULT sysdate NOT NULL,
	id varchar2(10),
	CONSTRAINT FK_ID FOREIGN KEY(id)
	REFERENCES t_member(id)
);

INSERT INTO t_board(articleNO, parentNO, title, content, imageFileName, writedate, id)
VALUES(1, 0, '테스트글입니다.', '테스트글입니다.', null, sysdate, 'hong');

INSERT INTO t_board(articleNO, parentNO, title, content, imageFileName, writedate, id)
VALUES(2, 0, '안녕하세요', '상품 후기입니다.', null, sysdate, 'hong');

INSERT INTO t_board(articleNO, parentNO, title, content, imageFileName, writedate, id)
VALUES(3, 2, '답변입니다.', '상품 후기에 대한 답변입니다.', null, sysdate, 'hong');

INSERT INTO t_board(articleNO, parentNO, title, content, imageFileName, writedate, id)
VALUES(4, 0, '답변입니다.', '김유신 테스트글입니다.', null, sysdate, 'kim');

INSERT INTO t_board(articleNO, parentNO, title, content, imageFileName, writedate, id)
VALUES(5, 3, '답변입니다', '상품 좋습니다.', null, sysdate, 'lee');

INSERT INTO t_board(articleNO, parentNO, title, content, imageFileName, writedate, id)
VALUES(6, 2, '상품 후기입니다..', '이순신씨의 상품 사용 후기를 올립니다.', null, sysdate, 'lee');

COMMIT;

SELECT * FROM t_board;



SELECT LEVEL,
		articleNo,
		parentNO,
		LPAD(' ', 4*(LEVEL-1)) || title title,
		content,
		writeDate,
		id
		FROM t_board
		START WITH parentNO=0
		CONNECT BY PRIOR articleNO=parentNO
		ORDER siblings BY articleNO DESC;



DELETE FROM t_board
WHERE articleNO IN (
	SELECT articleNO FROM t_board
	START WITH articleNO=2
	CONNECT BY PRIOR articleNO = parentNO
);


SELECT * FROM (
			SELECT ROWNUM AS recNum,
						LVL,
						articleNO,
						parentNO,
						title,
						content,
						id,
						writedate
						FROM (
								SELECT LEVEL AS LVL,
										articleNO,
										parentNO,
										title,
										content,
										id,
										writedate
									FROM t_board
								START WITH parentNO=0
								CONNECT BY PRIOR articleNO=parentNO
									ORDER SIBLINGS BY articleNO DESC						
						)
				)
WHERE recNum BETWEEN 1 AND 10;
-- recNum BETWEEN(SECTION-1)*100+(pageNum-1)*10+1 AND (SECTION-1)*100+pageNum*10;
-- recNum BETWEEN 1 AND 10; -- <-section 값이 1이고 pageNum값이 1인 경우


-- DROP TABLE T_BOARD;