# ServletMavenDemo
简单的原生Servlet、filter原理探索

use zac_third

SELECT
	sum(passed_validate_time) AS face_back_success,
	SUM(no_passed_validate_time) + sum(face_error_time) AS face_back_failure,
	(sum(passed_validate_time)) / (
		sum(passed_validate_time) + SUM(no_passed_validate_time) + sum(face_error_time)
	) as face_back_missSuccessRate
FROM
	face_retry_monitor_info1
WHERE
	application_name = 'OCR反面验证';

SELECT
	sum(passed_validate_time) AS face_front_success,
	SUM(no_passed_validate_time) + sum(face_error_time) AS face_front_failure,
	(sum(passed_validate_time)) / (
		sum(passed_validate_time) + SUM(no_passed_validate_time) + sum(face_error_time) 
	) as face_front_missSuccessRate
FROM
	face_retry_monitor_info1
WHERE
	application_name = 'OCR正面验证';

SELECT
	sum(passed_validate_time) AS face_sdk_success,
	SUM(no_passed_validate_time) + sum(face_error_time) AS face_sdk_failure,
	(sum(passed_validate_time)) / (
		sum(passed_validate_time) + SUM(no_passed_validate_time) + sum(face_error_time)
	) as face_sdk_missSuccessRate
FROM
	face_retry_monitor_info1
WHERE
	application_name = 'OCR活体检测';

SELECT
	sum(passed_validate_time) AS face_verify_success,
	SUM(no_passed_validate_time) + sum(face_error_time) AS face_verify_failure,
	(sum(passed_validate_time)) / (
		sum(passed_validate_time) + SUM(no_passed_validate_time) + sum(face_error_time)
	) as face_verify_missSuccessRate
FROM
	face_retry_monitor_info1
WHERE
	application_name = 'OCR人脸比对';
	
	
	
	
	

SET @allcCount = (
	SELECT
		(
			sum(passed_validate_time) + sum(no_passed_validate_time) + sum(face_error_time)
		) AS bb
	FROM
		face_retry_monitor_info1
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
			face_retry_monitor_info1
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
		face_retry_monitor_info1
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
			face_retry_monitor_info1
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
		face_retry_monitor_info1
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
			face_retry_monitor_info1
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
		face_retry_monitor_info1
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
			face_retry_monitor_info1
    where application_name = 'OCR人脸比对'
		GROUP BY
			user_id
	) AS tmp;








-- 备份 表数据 sqlserver  （修改备份日期）
select  * into RiskDecisions20180917 from RiskDecisions;
select  * into BaiRongRisks20180917 from BaiRongRisks;

-- --备份表数据 mysql
-- create table test like bairongrisks;
-- insert into test select * from bairongrisks;


-- 工单的数据库地址(mysql: IP 172.17.4.9、database: ticketsystem )
-- csdata的数据库地址(sqlserver: IP 172.17.0.1、database: csdata )
-- 1、将工单系统mysql 数据库表数据导出为txt格式 (改变下方IP、userName、passWord，改为工单的相关资料)
mysql -h 172.21.128.3 -u root -p -P 3306 --default-character-set=utf8 -D test.ticketsystem -e "select TICKET_ID,APPLY_ID from TS_APPLY_INFO"  > D:/Vss/TS_APPLY_INFO.txt

mysql -h 172.21.128.3 -u root -p -P 3306 --default-character-set=utf8 -D test.ticketsystem -e "select ID,BUSINESS_ID,IS_DELETED from TS_TICKET_INFO"  > D:/Vss/TS_TICKET_INFO.txt

-- 2、在小商平台sqlserver数据库创建表TS_APPLY_INFO 、TS_TICKET_INFO

-- ts_ticket_info表
DROP TABLE [dbo].[ts_ticket_info]
GO
CREATE TABLE [dbo].[ts_ticket_info] (
[ID] varchar(64) NOT NULL ,
[BUSSINESS_ID] varchar(64) NULL ,
[IS_DELETED] int NULL
)

GO

ALTER TABLE [dbo].[ts_ticket_info] ADD PRIMARY KEY ([ID])
GO

-- ts_apply_info表
DROP TABLE [dbo].[ts_apply_info]
GO
CREATE TABLE [dbo].[ts_apply_info] (
[APPLY_ID] varchar(64) NULL ,
[TICKET_ID] varchar(64) NOT NULL
)

GO

ALTER TABLE [dbo].[ts_apply_info] ADD PRIMARY KEY ([TICKET_ID])
GO

-- 3、将第一步产生的两个txt文件的数据对应到入到对应的这两张中

-- 下方这两条sql由于权限问题，没有测试使用
--  (可以借助工具 “Navicat”将数据导入到对应的表中，写此脚本使用的是此方式，推荐此方式) 或DBA可以用自己的方式将数据导入

-- bulk insert ts_apply_info from '\\10.18.192.29\D:\Vss\TS_APPLY_INFO.txt';
-- bulk insert ts_ticket_info from '\\10.18.192.29\D:\Vss\TS_TICKET_INFO.txt';


-- 4、运行下方sql语句修改BaiRongRisks表

if EXISTS(select * from master.dbo.syscursors where cursor_name='orderNum_09_cursor')
BEGIN
	CLOSE orderNum_09_cursor
deallocate  orderNum_09_cursor
end

declare orderNum_09_cursor cursor scroll
for select APPLY_ID,TICKET_ID,BUSSINESS_ID from ts_apply_info app
        inner join ts_ticket_info tic on app.TICKET_ID = tic.ID and tic.IS_DELETED = 0;
open orderNum_09_cursor
declare @apply varchar(90) ,@tick varchar(90) ,@business varchar(90)

fetch First from orderNum_09_cursor into @apply,@tick,@business
while @@fetch_status=0
 begin
   print (@apply+'--------'+@tick+'--------'+@business)
   update BaiRongRisks set ApplyId = @business where ApplyId = @apply;
   fetch next from orderNum_09_cursor into @apply ,@tick ,@business --移动游标
 end

--  5、运行下方sql语句修改RiskDecisions表（别与第四步一起运行、需要分开运行）
if EXISTS(select * from master.dbo.syscursors where cursor_name='orderNum_10_cursor')
BEGIN
	CLOSE orderNum_10_cursor
deallocate  orderNum_10_cursor
end


declare orderNum_10_cursor cursor scroll
for select APPLY_ID,TICKET_ID,BUSSINESS_ID from ts_apply_info app
        inner join ts_ticket_info tic on app.TICKET_ID = tic.ID and tic.IS_DELETED = 0;
open orderNum_10_cursor
declare @apply varchar(90) ,@tick varchar(90) ,@business varchar(90)

fetch First from orderNum_10_cursor into @apply,@tick,@business
while @@fetch_status=0
 begin
   print (@apply+'--------'+@tick+'--------'+@business)
   update RiskDecisions set ApplyId = @business where ApplyId = @apply;
   fetch next from orderNum_10_cursor into @apply ,@tick ,@business --移动游标
 end

-- set XACT_ABORT on
-- BEGIN tran
--  commit

 -- 6、删除表
 DROP TABLE [dbo].[ts_ticket_info];
 DROP TABLE [dbo].[ts_apply_info]




mysqldump -h localhost -uroot -p test bairongrisks -e"select ApplyId from bairongrisks" > D:/Vss/test.sql;

mysqldump -h 172.21.129.1 -uzdtest -p za888888 csdata bairongrisks> D:/Vss/test.sql;


-- 备份 表数据 sqlserver
select  * into RiskDecisions20180917 from RiskDecisions;

-- 备份表数据 mysql
create table test like bairongrisks;
insert into test select * from bairongrisks;
