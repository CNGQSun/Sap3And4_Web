package com.sap.model;

public class SqlStr {

	public static final String export_data_old = " with gg as(\r\n" + "select  *  from (\r\n" + "select   TRA_DAY\r\n"
			+ ", BORROW\r\n" + ", LOAN\r\n" + ", BALANCE\r\n" + ", ABSTRACT\r\n" + ", RE_PAY_NAME\r\n"
			+ ", RE_PAY_AC\r\n" + ", TRA_TYPE\r\n" + ", STACEY_YIN\r\n" + ", NEED_CHECK\r\n" + ", ACCOUNT\r\n"
			+ ", PO\r\n" + ",SPHIL_YU\r\n"
			+ ", PAYER, sum(cast(t.total_amt as numeric(20,2))) over( partition by t.loan,t.balance,t.re_pay_name) SUMS ,CREATED_ON,TOTAL_AMT,DOCUMENT_NUMBER,PARTNER  from (\r\n"
			+ "select * from BLANK_ORDER b\r\n" + "left JOIN BACK_ORDER a on \r\n"
			+ "( a.created_on > =(select convert(varchar(20),dateadd(month,-1,b.tra_day),120)) \r\n"
			+ " and SUBSTRING(a.created_on, 0, 10) <=b.tra_day and b.re_pay_name=a.description and a.payt='PPD' and risk_class='YS1' ) \r\n"
			+ ")t where len(loan)>0 )tt where (sums<=cast(loan as numeric(20,2))+1 and sums>=cast(loan as numeric(20,2))-1 ) or (total_amt<=cast(loan as numeric(20,2))+1 and total_amt>=cast(loan as numeric(20,2))-1 ) ),\r\n"
			+ "hh as (select   TRA_DAY\r\n" + ", BORROW\r\n" + ", LOAN\r\n" + ", BALANCE\r\n" + ", ABSTRACT\r\n"
			+ ", RE_PAY_NAME\r\n" + ", RE_PAY_AC\r\n" + ", TRA_TYPE\r\n" + ", STACEY_YIN\r\n" + ", NEED_CHECK\r\n"
			+ ", ACCOUNT\r\n" + ", PO\r\n" + ",SPHIL_YU\r\n"
			+ " ,PARTNER PAYER, sum(cast(t.total_amt as numeric(20,2))) over( partition by t.loan,t.balance,t.re_pay_name) SUMS ,CREATED_ON,TOTAL_AMT,DOCUMENT_NUMBER  from (\r\n"
			+ "select * from BLANK_ORDER b\r\n" + "inner JOIN BACK_ORDER a on \r\n"
			+ "( a.created_on > =(select convert(varchar(20),dateadd(month,-1,b.tra_day),120)) \r\n"
			+ " and SUBSTRING(a.created_on, 0, 10) <=b.tra_day and b.re_pay_name=a.description and a.payt='PPD' and risk_class='YS1' ) \r\n"
			+ ")t)\r\n" + "\r\n" + "select * from (\r\n" + "select DISTINCT  bos.TRA_DAY\r\n" + ", bos.BORROW\r\n"
			+ ", bos.LOAN\r\n" + ", bos.BALANCE\r\n" + ", bos.ABSTRACT\r\n" + ", bos.RE_PAY_NAME\r\n"
			+ ", bos.RE_PAY_AC\r\n" + ", bos.TRA_TYPE\r\n" + ", bos.STACEY_YIN\r\n" + ", bos.NEED_CHECK\r\n"
			+ ", bos.ACCOUNT\r\n" + ",bos.SPHIL_YU,t1.PO,hh.PAYER ,bos.ID from BLANK_ORDER  bos\r\n" + "\r\n"
			+ "left join hh on hh.loan=BOs.loan and hh.balance=bos.balance and hh.re_pay_name=bos.re_pay_name\r\n"
			+ "LEFT JOIN (\r\n" + "select TRA_DAY\r\n" + ", BORROW\r\n" + ", LOAN\r\n" + ", BALANCE\r\n"
			+ ", ABSTRACT\r\n" + ", RE_PAY_NAME\r\n" + ", RE_PAY_AC\r\n" + ", TRA_TYPE\r\n" + ", STACEY_YIN\r\n"
			+ ", NEED_CHECK\r\n" + ", ACCOUNT\r\n" + ",SPHIL_YU\r\n" + ",PARTNER PAYER,[PO]=(  \r\n"
			+ "select [DOCUMENT_NUMBER] +' ' from gg as b where a.loan=b.loan and a.balance=b.balance and a.re_pay_name=b.re_pay_name for xml path(''))  from (select * from gg t2 where not\r\n"
			+ "EXISTS (select 1 from gg t3 where t2.loan=t3.loan and t2.balance=t3.balance and t2.re_pay_name=t3.re_pay_name and (total_amt<=cast(loan as numeric(20,2))+1 and total_amt>=cast(loan as numeric(20,2))-1 )  GROUP BY loan,balance,re_pay_name HAVING count(1)>1 )\r\n"
			+ ") a )t1\r\n" + "on t1.loan=BOs.loan and t1.balance=bos.balance and t1.re_pay_name=bos.re_pay_name\r\n"
			+ ") hj\r\n" + "ORDER BY ID+0 asc";
	////修改前
	//public static final String export_data3 = "  with gg as(      select  *  from (      select   TRA_DAY  \r\n"
	//		+ "			   , BORROW      , LOAN      , BALANCE      , ABSTRACT      , RE_PAY_NAME  \r\n"
	//		+ "			   , RE_PAY_AC      , TRA_TYPE      , STACEY_YIN      , NEED_CHECK      , ACCOUNT  \r\n"
	//		+ "			   , PO      ,SPHIL_YU  \r\n"
	//		+ "			   , PAYER, sum(cast(t.total_amt as numeric(20,2))) over( partition by t.loan,t.balance,t.re_pay_name) SUMS ,CREATED_ON,TOTAL_AMT,DOCUMENT_NUMBER,PARTNER  from (  \r\n"
	//		+ "			   select * from BLANK_ORDER b      left JOIN BACK_ORDER a on   \r\n"
	//		+ "			   ( a.created_on > =(select convert(varchar(20),dateadd(month,-1,b.tra_day),120))   \r\n"
	//		+ "			    and SUBSTRING(a.created_on, 0, 10) <=b.tra_day and b.re_pay_name=a.description and a.payt='PPD' and (risk_class='YS1' OR risk_class='YSN') )   \r\n"
	//		+ "			   )t where len(loan)>0 )tt where (sums<=cast(loan as numeric(20,2))+ 1 and sums>=cast(loan as numeric(20,2))-1 ) or (total_amt<=cast(loan as numeric(20,2))+ 1 and total_amt>=cast(loan as numeric(20,2))-1 ) ),  \r\n"
	//		+ "			   hh as (select   TRA_DAY      , BORROW      , LOAN      , BALANCE      , ABSTRACT  \r\n"
	//		+ "			   , RE_PAY_NAME      , RE_PAY_AC      , TRA_TYPE      , STACEY_YIN      , NEED_CHECK  \r\n"
	//		+ "			   , ACCOUNT      , PO      ,SPHIL_YU  \r\n"
	//		+ "			    ,PARTNER PAYER, sum(cast(t.total_amt as numeric(20,2))) over( partition by t.loan,t.balance,t.re_pay_name) SUMS ,CREATED_ON,TOTAL_AMT,DOCUMENT_NUMBER  from (  \r\n"
	//		+ "			   select * from BLANK_ORDER b      inner JOIN BACK_ORDER a on   \r\n"
	//		+ "			   ( a.created_on > =(select convert(varchar(20),dateadd(month,-1,b.tra_day),120))   \r\n"
	//		+ "			    and SUBSTRING(a.created_on, 0, 10) <=b.tra_day and b.re_pay_name=a.description and a.payt='PPD' and (risk_class='YS1' OR risk_class='YSN') )   \r\n"
	//		+ "			   )t)       \r\n" + "select * into BLANK_ORDER_TEMP from   \r\n" + "\r\n"
	//		+ " (      select DISTINCT  bos.TRA_DAY      , bos.BORROW  \r\n"
	//		+ "			   , bos.LOAN      , bos.BALANCE      , bos.ABSTRACT      , bos.RE_PAY_NAME  \r\n"
	//		+ "			   , bos.RE_PAY_AC      , bos.TRA_TYPE      , bos.STACEY_YIN      , bos.NEED_CHECK  \r\n"
	//		+ "			   , bos.ACCOUNT      ,bos.SPHIL_YU,t1.PO,hh.PAYER ,bos.ID from BLANK_ORDER  bos        \r\n"
	//		+ "			   left join hh on hh.loan=BOs.loan and hh.balance=bos.balance and hh.re_pay_name=bos.re_pay_name  \r\n"
	//		+ "			   LEFT JOIN (      select TRA_DAY      , BORROW      , LOAN      , BALANCE  \r\n"
	//		+ "			   , ABSTRACT      , RE_PAY_NAME      , RE_PAY_AC      , TRA_TYPE      , STACEY_YIN  \r\n"
	//		+ "			   , NEED_CHECK      , ACCOUNT      ,SPHIL_YU      ,PARTNER PAYER,[PO]=(    \r\n"
	//		+ "			   select [DOCUMENT_NUMBER]  + ' ' from gg as b where a.loan=b.loan and a.balance=b.balance and a.re_pay_name=b.re_pay_name for xml path(''))  from (select * from gg t2 where not  \r\n"
	//		+ "			   EXISTS (select 1 from gg t3 where t2.loan=t3.loan and t2.balance=t3.balance and t2.re_pay_name=t3.re_pay_name and (total_amt<=cast(loan as numeric(20,2))+ 1 and total_amt>=cast(loan as numeric(20,2))-1 )  GROUP BY loan,balance,re_pay_name HAVING count(1)>1 )  \r\n"
	//		+ "			   ) a )t1      on t1.loan=BOs.loan and t1.balance=bos.balance and t1.re_pay_name=bos.re_pay_name  \r\n"
	//		+ "			   ) hj  ";

	//修改后
	public static final String export_data3 = "  with ps as(      select  *  from (      select   TRA_DAY  \r\n"
			+ "			   , BORROW      , LOAN      , BALANCE      , ABSTRACT      , RE_PAY_NAME  \r\n"
			+ "			   , RE_PAY_AC      , TRA_TYPE      , STACEY_YIN      , NEED_CHECK      , ACCOUNT  \r\n"
			+ "			   , PO      ,SPHIL_YU  ,id\r\n"
			+ "			   , PAYER ,CREATED_ON,TOTAL_AMT,DOCUMENT_NUMBER,PARTNER  from (  \r\n"
			+ "			   select * from BLANK_ORDER b      left JOIN BACK_ORDER a on   \r\n"
			+ "			   ( a.created_on > =(select convert(varchar(20),dateadd(month,-1,b.tra_day),120))   \r\n"
			+ "			    and SUBSTRING(a.created_on, 0, 10) <=b.tra_day and b.re_pay_name=a.description and a.payt='PPD' and (risk_class='YS1' OR risk_class='YSN') )   \r\n"
			+ "			   )t where len(loan)>0 )tt where CAST(REPLACE(ISNULL(TOTAL_AMT, '0'), ',', '') AS NUMERIC(20, 2)) <= CAST(REPLACE(ISNULL(loan, '0'), ',', '') AS numeric(20, 2)) + 1 ),  \r\n"
			+ " gge AS (SELECT *,total =cast(REPLACE(ISNULL(TOTAL_AMT, '0'), ',', '') as NUMERIC(20,2)),pos = CAST(DOCUMENT_NUMBER AS varchar(8000)) \r\n"
			+ " FROM ps \r\n"
			+ " UNION ALL \r\n"
			+ " SELECT b.TRA_DAY, b.BORROW, b.LOAN, b.BALANCE, b.ABSTRACT \r\n"
			+ "			, b.RE_PAY_NAME, b.RE_PAY_AC, b.TRA_TYPE, b.STACEY_YIN, b.NEED_CHECK \r\n"
			+ "			, b.ACCOUNT, b.PO, b.SPHIL_YU,b.id, b.PAYER \r\n"
			+ " , b.CREATED_ON, b.TOTAL_AMT \r\n"
			+ "			, b.DOCUMENT_NUMBER, b.PARTNER, \r\n"
			+ " CAST((a.total+ cast(REPLACE(ISNULL(b.TOTAL_AMT, '0'), ',', '') as NUMERIC(20,2))) AS numeric(20, 2)),a.pos + ' ' + b.DOCUMENT_NUMBER \r\n"
			+ "FROM gge a \r\n"
			+ "JOIN ps b on a.DOCUMENT_NUMBER<b.DOCUMENT_NUMBER \r\n"
			+ "AND a.id=b.ID), \r\n"
			+ "gg AS( \r\n"
			+ "       SELECT * \r\n"
			+ "       FROM gge \r\n"
			+ "       WHERE  (((total_amt <= CAST(REPLACE(ISNULL(loan, '0'), ',', '') AS numeric(20, 2)) + 1 \r\n"
			+ " AND total_amt >= CAST(REPLACE(ISNULL(loan, '0'), ',', '') AS numeric(20, 2)) - 1)AND total=total_amt) \r\n"
			+ " OR (total <= CAST(REPLACE(ISNULL(loan, '0'), ',', '') AS numeric(20, 2)) + 1 \r\n"
			+ " AND total >= CAST(REPLACE(ISNULL(loan, '0'), ',', '') AS numeric(20, 2)) - 1)) \r\n"
			+ "       ), \r\n"
			+ "			   hh as (select   TRA_DAY      , BORROW      , LOAN      , BALANCE      , ABSTRACT  \r\n"
			+ "			   , RE_PAY_NAME      , RE_PAY_AC      , TRA_TYPE      , STACEY_YIN      , NEED_CHECK  \r\n"
			+ "			   , ACCOUNT      , PO      ,SPHIL_YU  \r\n"
			+ "			    ,PARTNER PAYER, sum(cast(t.total_amt as numeric(20,2))) over( partition by t.loan,t.balance,t.re_pay_name) SUMS ,CREATED_ON,TOTAL_AMT,DOCUMENT_NUMBER  from (  \r\n"
			+ "			   select * from BLANK_ORDER b      inner JOIN BACK_ORDER a on   \r\n"
			+ "			   ( a.created_on > =(select convert(varchar(20),dateadd(month,-1,b.tra_day),120))   \r\n"
			+ "			    and SUBSTRING(a.created_on, 0, 10) <=b.tra_day and b.re_pay_name=a.description and a.payt='PPD' and (risk_class='YS1' OR risk_class='YSN'))   \r\n"
			+ "			   )t)       \r\n" + "select * into BLANK_ORDER_TEMP from   \r\n" + "\r\n"
			+ " (      select DISTINCT  bos.TRA_DAY      , bos.BORROW  \r\n"
			+ "			   , bos.LOAN      , bos.BALANCE      , bos.ABSTRACT      , bos.RE_PAY_NAME  \r\n"
			+ "			   , bos.RE_PAY_AC      , bos.TRA_TYPE      , bos.STACEY_YIN      , bos.NEED_CHECK  \r\n"
			+ "			   , bos.ACCOUNT      ,bos.SPHIL_YU,t1.PO,hh.PAYER ,bos.ID from BLANK_ORDER  bos        \r\n"
			+ "			   left join hh on hh.loan=BOs.loan and hh.balance=bos.balance and hh.re_pay_name=bos.re_pay_name  \r\n"
			+ "			   LEFT JOIN (      select TRA_DAY      , BORROW      , LOAN      , BALANCE  \r\n"
			+ "			   , ABSTRACT      , RE_PAY_NAME      , RE_PAY_AC      , TRA_TYPE      , STACEY_YIN  \r\n"
			+ "			   , NEED_CHECK      , ACCOUNT      ,SPHIL_YU      ,PARTNER PAYER,PO=(    \r\n"
			+ "			   select pos from gg as b where a.loan=b.loan and a.balance=b.balance and a.re_pay_name=b.re_pay_name)  from (select * from gg t2 where not  \r\n"
			+ "			   EXISTS (select 1 from gg t3 where t2.loan=t3.loan AND t2.balance = t3.balance AND t2.re_pay_name = t3.re_pay_name  GROUP BY loan,balance,re_pay_name HAVING count(1)>1 )  \r\n"
			+ "			   ) a )t1      on t1.loan=BOs.loan and t1.balance=bos.balance and t1.re_pay_name=bos.re_pay_name  \r\n"
			+ "			   ) hj  ";

	public static final String export_data = "\r\n" + "select  * from (\r\n" + "\r\n" + "SELECT DISTINCT\r\n"
			+ "	a.TRA_DAY,\r\n" + "	a.BORROW,\r\n" + "	a.LOAN,\r\n" + "	a.BALANCE,\r\n" + "	a.ABSTRACT,\r\n"
			+ "	a.RE_PAY_NAME,\r\n" + "	a.RE_PAY_AC,\r\n" + "	a.TRA_TYPE,\r\n" + "	a.STACEY_YIN,\r\n"
			+ "	a.NEED_CHECK,\r\n" + "	a.ACCOUNT,\r\n" + "	a.SPHIL_YU,\r\n" + "	a.PO,\r\n" + "	CASE\r\n"
			+ "WHEN b.counts > 1 THEN\r\n" + "	''\r\n" + "ELSE\r\n" + "	a.PAYER\r\n" + "END PAYER,\r\n"
			+ " a.ID\r\n" + "FROM\r\n" + "	BLANK_ORDER_TEMP a\r\n" + "LEFT JOIN (\r\n" + "	SELECT\r\n"
			+ "		COUNT (1) counts,\r\n" + "		TRA_DAY,\r\n" + "		BORROW,\r\n" + "		LOAN,\r\n"
			+ "		BALANCE,\r\n" + "		ABSTRACT,\r\n" + "		RE_PAY_NAME,\r\n" + "		RE_PAY_AC\r\n"
			+ "	FROM\r\n" + "		BLANK_ORDER_TEMP\r\n" + "	GROUP BY\r\n" + "		TRA_DAY,\r\n"
			+ "		BORROW,\r\n" + "		LOAN,\r\n" + "		BALANCE,\r\n" + "		ABSTRACT,\r\n"
			+ "		RE_PAY_NAME,\r\n" + "		RE_PAY_AC,\r\n" + "		TRA_TYPE,\r\n" + "		STACEY_YIN,\r\n"
			+ "		NEED_CHECK,\r\n" + "		ACCOUNT,\r\n" + "		SPHIL_YU,\r\n" + "		PO\r\n"
			+ ") b ON a.LOAN = b.LOAN\r\n" + "AND a.BALANCE = b.BALANCE\r\n"
			+ "AND a.RE_PAY_NAME = b.RE_PAY_NAME) hgh\r\n" + "order by id+0";
	public static final String truncate_sql = "truncate table back_order;truncate table BLANK_ORDER;truncate table SAP_MAPPING;";
	public static final String drop_sql = "drop table BLANK_ORDER_TEMP;drop table BLANK_ORDER_TEMP2;";

	public static final String hander_mapping_sql = "update back_order  set description = ( select top 1 b.name from sap_mapping b where partner=b.payer )\r\n"
			+ "where partner in (select payer from sap_mapping );update BLANK_ORDER set PO='',PAYER='';";

	
	public static final String create_back_order_temp2="select * into BLANK_ORDER_TEMP2 from (\r\n" + 
			"\r\n" + 
			"select  * from (    SELECT DISTINCT\r\n" + 
			"			 	a.TRA_DAY,  	a.BORROW,  	a.LOAN,  	a.BALANCE,  	a.ABSTRACT,\r\n" + 
			"			 	a.RE_PAY_NAME,  	a.RE_PAY_AC,  	a.TRA_TYPE,  	a.STACEY_YIN,\r\n" + 
			"			 	a.NEED_CHECK,  	a.ACCOUNT,  	a.SPHIL_YU,  	a.PO,  	CASE\r\n" + 
			"			 WHEN b.counts > 1 THEN  	''  ELSE  	a.PAYER  END PAYER,\r\n" + 
			"			  a.ID  FROM  	BLANK_ORDER_TEMP a  LEFT JOIN (  	SELECT\r\n" + 
			"			 		COUNT (1) counts,  		TRA_DAY,  		BORROW,  		LOAN,\r\n" + 
			"			 		BALANCE,  		ABSTRACT,  		RE_PAY_NAME,  		RE_PAY_AC\r\n" + 
			"			 	FROM  		BLANK_ORDER_TEMP  	GROUP BY  		TRA_DAY,\r\n" + 
			"			 		BORROW,  		LOAN,  		BALANCE,  		ABSTRACT,\r\n" + 
			"			 		RE_PAY_NAME,  		RE_PAY_AC,  		TRA_TYPE,  		STACEY_YIN,\r\n" + 
			"			 		NEED_CHECK,  		ACCOUNT,  		SPHIL_YU,  		PO\r\n" + 
			"			 ) b ON a.LOAN = b.LOAN  AND a.BALANCE = b.BALANCE\r\n" + 
			"			 AND a.RE_PAY_NAME = b.RE_PAY_NAME) hgh  \r\n" + 
			")hhh";
	
	
	public static final String get_blank_order="select DISTINCT b.* from BLANK_ORDER_TEMP2 b \r\n" + 
			"left join BACK_ORDER a on ( a.created_on > =(select convert(varchar(20),dateadd(month,-1,b.tra_day),120)) \r\n" + 
			"			  and SUBSTRING(a.created_on, 0, 10) <=b.tra_day and b.re_pay_name=a.description and a.payt='PPD' and risk_class='YS1' )\r\n" + 
			"where len(isnull(b.payer,''))!=0 and (b.po is null or len(b.po)=0) and \r\n" + 
			"CAST(REPLACE(ISNULL(a.total_amt, '0'), ',', '') AS NUMERIC(20, 2)) NOT BETWEEN CAST(REPLACE(ISNULL(b.loan, '0'), ',', '') AS numeric(20, 2)) - 1 AND CAST(REPLACE(ISNULL(b.loan, '0'), ',', '') AS numeric(20, 2)) + 1";
}
