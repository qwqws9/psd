#CREATE DATABASE `dsp` /*!40100 COLLATE 'latin1_swedish_ci' */;

CREATE TABLE usr (
	USR_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	EMAIL VARCHAR(100) NOT NULL,
	SUB VARCHAR(100) NOT NULL,
	ROLE VARCHAR(20) NOT NULL,
	REG_DT VARCHAR(14) NOT NULL,
	USE_YN CHAR(1) NOT NULL,
	LAST_DT VARCHAR(14) NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE show_room (
	SHOW_ROOM_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	AVATAR_NAME VARCHAR(1000) NOT NULL,
	PREVIEW_INDEX VARCHAR(100) NOT NULL,
	JOB_VALUE VARCHAR(10) NOT NULL,
	PARTS_NAME VARCHAR(20) NOT NULL,
	ITEM_ID VARCHAR(100) NULL,
	RARITY VARCHAR(50) NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

# 2021-03-08 JOB_DESC (추가)
CREATE TABLE job (
	JOB_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	JOB_VALUE VARCHAR(10) NOT NULL,
	JOB_NAME VARCHAR(100) NOT NULL,
	JOB_ID VARCHAR(100) NOT NULL,
	JOB_DESC VARCHAR(100) NULL,
	UNIQUE KEY uk_job (JOB_VALUE)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE job_detail (
	JOB_DETAIL_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	FIRST_JOB VARCHAR(10) NOT NULL,
	SECOND_JOB VARCHAR(100) NOT NULL,
	THIRD_JOB VARCHAR(100) NULL,
	FOURTH_JOB VARCHAR(100) NULL,
	JOB_VALUE VARCHAR(10) NOT NULL,
	FOREIGN KEY (JOB_VALUE) REFERENCES job (JOB_VALUE)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

# 2021-03-06 RATE 컬럼 <삭제>
CREATE TABLE emblem (
	EMBLEM_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	EMBLEM_ID VARCHAR(100) NOT NULL,
	EMBLEM_NAME VARCHAR(300) NOT NULL,
	JOB_DETAIL_SEQ INT NOT NULL,
	BUFF_YN VARCHAR(1) NULL
	FOREIGN KEY (JOB_DETAIL_SEQ) REFERENCES job_detail (JOB_DETAIL_SEQ)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

CREATE TABLE rank_data (
	RANK_DATA_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	CHARACTER_ID VARCHAR(100) NOT NULL,
	JOB_DETAIL_SEQ INT NOT NULL,
	SERVER_ID VARCHAR(20) NOT NULL,
	FOREIGN KEY (JOB_DETAIL_SEQ) REFERENCES job_detail (JOB_DETAIL_SEQ)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

# 2021-03-10 PARTS_NAME, CHOICE_OPTION 길이 <수정>
CREATE TABLE option_ability (
	OPTION_ABILITY_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	PARTS_NAME VARCHAR(20) NOT NULL,
	JOB_DETAIL_SEQ INT NOT NULL,
	CHOICE_OPTION VARCHAR(100) NOT NULL,
	RATE VARCHAR(10) NULL,
	FOREIGN KEY (JOB_DETAIL_SEQ) REFERENCES job_detail (JOB_DETAIL_SEQ)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

# 2021-03-06 테이블 <신규>
# SELECT * FROM emblem_rate ORDER BY cast(rate AS DECIMAL(4,2)) DESC;
CREATE TABLE emblem_rate (
	EMBLEM_RATE_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	EMBLEM_ID VARCHAR(50) NULL,
	JOB_DETAIL_SEQ INT NOT NULL,
	EMBLEM_NAME VARCHAR(50) NOT NULL,
	RATE VARCHAR(10) NULL,
	EMBLEM_COLOR VARCHAR(20) NOT NULL,
	FOREIGN KEY (JOB_DETAIL_SEQ) REFERENCES job_detail (JOB_DETAIL_SEQ)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

# 2021-03-10 테이블 <신규>
# 2021-03-11 DEGREE 컬럼 <추가>
CREATE TABLE market_mst (
	MARKET_MST_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	TITLE VARCHAR(100) NOT NULL,
	JOB_VALUE VARCHAR(10) NOT NULL,
	JOB_DETAIL_SEQ INT NOT NULL,
	PRICE VARCHAR(20) NOT NULL,
	EMBLEM_CODE VARCHAR(10) NOT NULL,
	DEGREE VARCHAR(10) NOT NULL,
	FOREIGN KEY (JOB_DETAIL_SEQ) REFERENCES job_detail (JOB_DETAIL_SEQ)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

# 2021-03-10 테이블 <신규>
CREATE TABLE market_detail (
	MARKET_DETAIL_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	MARKET_MST_SEQ INT NOT NULL,
	ITEM_NAME VARCHAR(100) NOT NULL,
	SLOT_ID VARCHAR(100) NULL,
	CHOICE_OPTION VARCHAR(100) NULL,
	EMBLEM_NAME1 VARCHAR(100) NULL,
	EMBLEM_COLOR1 VARCHAR(100) NULL,
	EMBLEM_NAME2 VARCHAR(100) NULL,
	EMBLEM_COLOR2 VARCHAR(100) NULL,
	EMBLEM_NAME3 VARCHAR(100) NULL,
	EMBLEM_COLOR3 VARCHAR(100) NULL,
	FOREIGN KEY (MARKET_MST_SEQ) REFERENCES market_mst (MARKET_MST_SEQ)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

# 2021-03-12 테이블 <신규>
CREATE TABLE ranking (
	RANKING_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	SERVER_NAME VARCHAR(20) NOT NULL,
	CHARACTER_NAME VARCHAR(50) NOT NULL,
	CHARACTER_ID VARCHAR(50) NOT NULL,
	JOB_VALUE VARCHAR(10) NOT NULL,
	PRICE VARCHAR(100) NULL,
	FOREIGN KEY (JOB_VALUE) REFERENCES job_detail (JOB_VALUE)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;



















INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('0','귀검사(남)','41f1cdc2ff58bb5fdc287be0db2a8df3','남귀검사');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('1','격투가(여)','a7a059ebe9e6054c0644b40ef316d6e9','여격투가');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('2','거너(남)','afdf3b989339de478e85b614d274d1ef','남거너');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('3','마법사(여)','3909d0b188e9c95311399f776e331da5','여마법사');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('4','프리스트(남)','f6a4ad30555b99b499c07835f87ce522','남프리스트');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('5','거너(여)','944b9aab492c15a8474f96947ceeb9e4','여거너');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('6','도적','ddc49e9ad1ff72a00b53c6cff5b1e920','도적');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('7','격투가(남)','ca0f0e0e9e1d55b5f9955b03d9dd213c','남격투가');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('8','마법사(남)','a5ccbaf5538981c6ef99b236c0a60b73','남마법사');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('9','다크나이트','17e417b31686389eebff6d754c3401ea');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('10','크리에이터','b522a95d819a5559b775deb9a490e49a');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('11','귀검사(여)','1645c45aabb008c98406b3a16447040d','여귀검사');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('12','나이트','0ee8fa5dc525c1a1f23fc6911e921e4a','나이트');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('13','마창사','3deb7be5f01953ac8b1ecaa1e25e0420','마창사');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('14','프리스트(여)','0c1b401bb09241570d364420b3ba3fd7','여프리스트');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('15','총검사','986c2b3d72ee0e4a0b7fcfbe786d4e02','총검사');
INSERT INTO job(JOB_VALUE, JOB_NAME, JOB_ID, JOB_DESC) VALUES('99','기타','99', null);


# JOB_DETAIL 생성후 추가등록해주기.
INSERT INTO job_detail (JOB_DETAIL_SEQ, FIRST_JOB, SECOND_JOB, THIRD_JOB, FOURTH_JOB, JOB_VALUE) VALUES ('99', '기타', '기타', '기타', '기타', '99');











# 랭킹 테스트 쿼리
WITH T AS (
	SELECT '1000' AS price, 'A' AS c_name, '1' AS job_value
	UNION ALL
	SELECT '1100' AS price, 'Q' AS c_name, '1' AS job_value
	UNION ALL
	SELECT '1200' AS price, 'W' AS c_name, '1' AS job_value
	UNION ALL
	SELECT '5312' AS price, 'E' AS c_name, '1' AS job_value
	UNION ALL
	SELECT '4323' AS price, 'R' AS c_name, '1' AS job_value
	UNION ALL
	SELECT '4521' AS price, 'T' AS c_name, '2' AS job_value
	UNION ALL
	SELECT '4964' AS price, 'Y' AS c_name, '2' AS job_value
	UNION ALL
	SELECT '9472' AS price, 'U' AS c_name, '2' AS job_value
	UNION ALL
	SELECT '6372' AS price, 'I' AS c_name, '2' AS job_value
	UNION ALL
	SELECT '6842' AS price, 'O' AS c_name, '2' AS job_value
	UNION ALL
	SELECT '4593' AS price, 'S' AS c_name, '3' AS job_value
	UNION ALL
	SELECT '3457' AS price, 'D' AS c_name, '3' AS job_value
	UNION ALL
	SELECT '2357' AS price, 'F' AS c_name, '3' AS job_value
	UNION ALL
	SELECT '5473' AS price, 'G' AS c_name, '3' AS job_value
	UNION ALL
	SELECT '5768' AS price, 'H' AS c_name, '3' AS job_value
	UNION ALL
	SELECT '8853' AS price, 'J' AS c_name, '4' AS job_value
	UNION ALL
	SELECT '6455' AS price, 'K' AS c_name, '4' AS job_value
	UNION ALL
	SELECT '3442' AS price, 'X' AS c_name, '4' AS job_value
	UNION ALL
	SELECT '9876' AS price, 'W' AS c_name, '4' AS job_value
)
SELECT T.* FROM (
SELECT job_value, c_name,SUM(price) total FROM T
GROUP BY job_value, c_name WITH ROLLUP) T
ORDER BY job_value, total desc




#ALTER TABLE usr CONVERT TO CHARSET utf8;
#ALTER TABLE show_room CONVERT TO CHARSET utf8;
#ALTER TABLE job CONVERT TO CHARSET UTF8;