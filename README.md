# ServletMavenDemo
简单的原生Servlet、filter原理探索

SELECT
	sum(passed_validate_time) AS face_back_success,
	SUM(no_passed_validate_time) + sum(face_error_time) AS face_back_failure,
	(sum(passed_validate_time)) / (
		sum(passed_validate_time) + SUM(no_passed_validate_time) + sum(face_error_time)
	) as face_back_missSuccessRate
FROM
	table
WHERE
	application_name = 'OCR反面验证';

SELECT
	sum(passed_validate_time) AS face_front_success,
	SUM(no_passed_validate_time) + sum(face_error_time) AS face_front_failure,
	(sum(passed_validate_time)) / (
		sum(passed_validate_time) + SUM(no_passed_validate_time) + sum(face_error_time) 
	) as face_front_missSuccessRate
FROM
	table
WHERE
	application_name = 'OCR正面验证';

SELECT
	sum(passed_validate_time) AS face_sdk_success,
	SUM(no_passed_validate_time) + sum(face_error_time) AS face_sdk_failure,
	(sum(passed_validate_time)) / (
		sum(passed_validate_time) + SUM(no_passed_validate_time) + sum(face_error_time)
	) as face_sdk_missSuccessRate
FROM
	table
WHERE
	application_name = 'OCR活体检测';

SELECT
	sum(passed_validate_time) AS face_verify_success,
	SUM(no_passed_validate_time) + sum(face_error_time) AS face_verify_failure,
	(sum(passed_validate_time)) / (
		sum(passed_validate_time) + SUM(no_passed_validate_time) + sum(face_error_time)
	) as face_verify_missSuccessRate
FROM
	table
WHERE
	application_name = 'OCR人脸比对';
	
	
	
	
	

SET @allcCount = (
	SELECT
		(
			sum(passed_validate_time) + sum(no_passed_validate_time) + sum(face_error_time)
		) AS bb
	FROM
		table
	where application_name = 'OCR反面验证'
);

SELECT
	floor(@allcCount) as face_back_count_times,
	count(1) AS face_back_user_count,
	ROUND(floor(@allcCount)/(count(1)),2) as face_back_avg_times
FROM
	(
		SELECT
			COUNT(1) AS user_count
		FROM
			table
    where application_name = 'OCR反面验证'
		GROUP BY
			user_id
	) AS tmp;


SET @allcCount = (
	SELECT
		(
			sum(passed_validate_time) + sum(no_passed_validate_time) + sum(face_error_time)
		) AS bb
	FROM
		table
	where application_name = 'OCR正面验证'
);

SELECT
	floor(@allcCount) as face_front_count_times,
	count(1) AS face_front_user_count,
	ROUND(floor(@allcCount)/(count(1)),2) as face_front_avg_times
FROM
	(
		SELECT
			COUNT(1) AS user_count
		FROM
			table
    where application_name = 'OCR正面验证'
		GROUP BY
			user_id
	) AS tmp;

SET @allcCount = (
	SELECT
		(
			sum(passed_validate_time) + sum(no_passed_validate_time) + sum(face_error_time)
		) AS bb
	FROM
		table
	where application_name = 'OCR活体检测'
);

SELECT
	floor(@allcCount) as face_sdk_count_times,
	count(1) AS face_sdk_user_count,
	ROUND(floor(@allcCount)/(count(1)),2) as face_sdk_avg_times
FROM
	(
		SELECT
			COUNT(1) AS user_count
		FROM
			table
    where application_name = 'OCR活体检测'
		GROUP BY
			user_id
	) AS tmp;



SET @allcCount = (
	SELECT
		(
			sum(passed_validate_time) + sum(no_passed_validate_time) + sum(face_error_time)
		) AS bb
	FROM
		table
	where application_name = 'OCR人脸比对'
);

SELECT
	floor(@allcCount) as face_verify_count_times,
	count(1) AS face_verify_user_count,
	ROUND(floor(@allcCount)/(count(1)),2) as face_verify_avg_times
FROM
	(
		SELECT
			COUNT(1) AS user_count
		FROM
			table
    where application_name = 'OCR人脸比对'
		GROUP BY
			user_id
	) AS tmp;








-- 备份 表数据 sqlserver  （修改备份日期）
select  * into table20180917 from table;
select  * into table20180917 from table;

-- --备份表数据 mysql
-- create table test like table;
-- insert into test select * from table;


mysql -h 127.0.0.1 -u root -p -P 3306 --default-character-set=utf8 -D test.ddd -e "select TICKET_ID,APPLY_ID from table"  > D:/Vss/table.txt

mysql -h 127.0.0.1 -u root -p -P 3306 --default-character-set=utf8 -D test.ddd -e "select ID,BUSINESS_ID,IS_DELETED from table"  > D:/Vss/table.txt



-- 3、将第一步产生的两个txt文件的数据对应到入到对应的这两张中

-- 下方这两条sql由于权限问题，没有测试使用
--  (可以借助工具 “Navicat”将数据导入到对应的表中，写此脚本使用的是此方式，推荐此方式) 或DBA可以用自己的方式将数据导入

-- bulk insert ts_apply_info from '\\127.0.0.1\D:\Vss\TS_APPLY_INFO.txt';
-- bulk insert ts_ticket_info from '\\127.0.0.1\D:\Vss\TS_TICKET_INFO.txt';


-- 4、运行下方sql语句修改BaiRongRisks表

if EXISTS(select * from master.dbo.syscursors where cursor_name='orderNum_09_cursor')
BEGIN
	CLOSE orderNum_09_cursor
deallocate  orderNum_09_cursor
end

declare orderNum_09_cursor cursor scroll
for select APPLY_ID,TICKET_ID,BUSSINESS_ID from table app
        inner join table tic on app.TICKET_ID = tic.ID and tic.IS_DELETED = 0;
open orderNum_09_cursor
declare @apply varchar(90) ,@tick varchar(90) ,@business varchar(90)

fetch First from orderNum_09_cursor into @apply,@tick,@business
while @@fetch_status=0
 begin
   print (@apply+'--------'+@tick+'--------'+@business)
   update table set ApplyId = @business where ApplyId = @apply;
   fetch next from orderNum_09_cursor into @apply ,@tick ,@business --移动游标
 end

--  5、运行下方sql语句修改RiskDecisions表（别与第四步一起运行、需要分开运行）
if EXISTS(select * from master.dbo.syscursors where cursor_name='orderNum_10_cursor')
BEGIN
	CLOSE orderNum_10_cursor
deallocate  orderNum_10_cursor
end


declare orderNum_10_cursor cursor scroll
for select APPLY_ID,TICKET_ID,BUSSINESS_ID from table app
        inner join table tic on app.TICKET_ID = tic.ID and tic.IS_DELETED = 0;
open orderNum_10_cursor
declare @apply varchar(90) ,@tick varchar(90) ,@business varchar(90)

fetch First from orderNum_10_cursor into @apply,@tick,@business
while @@fetch_status=0
 begin
   print (@apply+'--------'+@tick+'--------'+@business)
   update table set ApplyId = @business where ApplyId = @apply;
   fetch next from orderNum_10_cursor into @apply ,@tick ,@business --移动游标
 end

-- set XACT_ABORT on
-- BEGIN tran
--  commit





mysqldump -h localhost -uroot -p database table -e"select ApplyId from table" > D:/Vss/test.sql;

mysqldump -h localhost -uzdtest -p za888888 database table> D:/Vss/test.sql;


-- 备份 表数据 sqlserver
select  * into table20180917 from table;

-- 备份表数据 mysql
create table test like table;
insert into test select * from table;
