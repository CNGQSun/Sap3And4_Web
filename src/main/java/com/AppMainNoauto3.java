package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.Environment;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.FileCopyUtils;

import com.merck.utils.DateUtils;
import com.sap.model.SqlStr;
import com.sap.service.ImportData;
import com.sap.utils.CSVUtil;
import com.sap.utils.ExcelReaderUtil;
import com.sap.utils.MyCombine;

@SpringBootApplication
@ComponentScan
@MapperScan("com.*.dao")
public class AppMainNoauto3 {
	private static Logger log = LoggerFactory.getLogger(AppMainNoauto3.class);

	public static final Properties p = new Properties();

	public static void main(String[] args) {
		
		getParamDetails(args);
		ConfigurableApplicationContext run = SpringApplication.run(AppMainAll.class, args);
		log.info("springBoot容器已经启动");
		ImportData bean4 = (ImportData) run.getBean(ImportData.class);

		log.info("Sap4.4已经开始执行任务：开始时间：" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		while (true) {
			if (p.get("blank.path") != null) {
				break;
			}
			System.out.println("wait laod properties");
		}

		try {
			File blankOrderFile = null;
			String blankOrderPath = "";
			while (true) {
				blankOrderPath = p.getProperty("blank.path");
				blankOrderFile = new File(blankOrderPath);
				if (blankOrderFile.exists()) {
					break;
				} else {
					log.info("Sap4.4没有发现blank.xlsx文件");
					log.info("Sap4.4执行任务结束：结束时间：" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					return;
				}
			}

			/*
			 * // 触发python脚本，等待执行 log.info("Sap4.4开始执行bat文件" + DateUtils.format(new Date(),
			 * "yyyy-MM-dd HH:mm:ss")); String batPath = p.getProperty("bat.path"); String
			 * cmd = "cmd /c start " + batPath; Process pro =
			 * Runtime.getRuntime().exec(cmd); BufferedReader in = new BufferedReader(new
			 * InputStreamReader(pro.getInputStream())); String line = null; while ((line =
			 * in.readLine()) != null) { System.out.println(line); } in.close();
			 * pro.waitFor(); log.info("Sap4.4执行bat文件结束" + DateUtils.format(new Date(),
			 * "yyyy-MM-dd HH:mm:ss"));
			 */
			// 获取sap文件

			String backOrderPath = p.getProperty("sap.path");
			File backOrderFile = new File(backOrderPath);

			if (!backOrderFile.exists()) {
				log.info("Sap4.4没有发现Back_Order.txt文件");
				log.info("Sap4.4执行任务结束：结束时间：" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				return;
			}

			String mappingOrderPath = p.getProperty("mapping.path");
			File mappingFile = new File(mappingOrderPath);
			if (!mappingFile.exists()) {
				log.info("Sap4.4没有发现mapping.xlsx文件");
			}

			String exportPath = p.getProperty("export.path");
			final List<String> title = new ArrayList<String>();
			title.add("Document Number");
			title.add("Partner");
			// 清空表
			bean4.truncateTableData(SqlStr.truncate_sql);
			CSVUtil.readCsv(backOrderFile, "utf-8", title, bean4, "BACK_ORDER");
			ExcelReaderUtil.readExcel(bean4, blankOrderFile, "BLANK_ORDER");
			if (mappingFile.exists())
				ExcelReaderUtil.readExcel(bean4, mappingFile, "SAP_MAPPING");
			// 处理mapping
			bean4.truncateTableData(SqlStr.hander_mapping_sql);
			// 导出Excle数据
			bean4.truncateTableData(SqlStr.export_data3);

			// 将数据导入到temp2
			bean4.truncateTableData(SqlStr.create_back_order_temp2);

			// 获取存在问题的数据
			List<Map<String, Object>> selectData = bean4.selectData(SqlStr.get_blank_order);

			if (CollectionUtils.isNotEmpty(selectData)) {
				for (Map<String, Object> map : selectData) {
					String loan = (String) map.get("LOAN");
					String descrip = (String) map.get("RE_PAY_NAME");
					String payer = (String) map.get("PAYER");

					String sql2 = "select  * from  BACK_ORDER a  where a.description='" + descrip
							+ "' and  a.payt='PPD' and risk_class='YS1'";

					List<Map<String, Object>> selectData2 = bean4.selectData(sql2);
					if (CollectionUtils.isNotEmpty(selectData)) {
						int size = selectData2.size();
						if (size <= 2) {
							continue;
						}
						String[] index = new String[size];
						for (int i = 0; i < size; i++) {
							index[i] = i + "";
						}
						flg: for (int j = 2; j < size; j++) {
							MyCombine tp = new MyCombine();
							List<String> combine = tp.combine(index, j);
							for (String strindex : combine) {
								if (StringUtils.isNoneBlank(strindex)) {
									String[] split = strindex.split(" +");
									String pos = "";
									double s = 0;
									for (int i = 0; i < split.length; i++) {

										Map<String, Object> mapData = selectData2.get(Integer.valueOf(split[i]));
										String po = (String) mapData.get("DOCUMENT_NUMBER");
										String object = (String) mapData.get("TOTAL_AMT");
										if (StringUtils.isNoneBlank(object)) {
											double totalAmt = Double.valueOf(object);
											s = s + totalAmt;
										}

										pos = pos + " " + po;
									}
									if (s == Double.valueOf(loan)) {
										if(pos.startsWith(" ")) {
											pos=pos.substring(pos.indexOf(" ")+1);
										}
										String sql3 = "update BLANK_ORDER_TEMP2 set po='" + pos + "' where LOAN='"
												+ loan + "'  and RE_PAY_NAME='" + descrip + "'  and payer='" + payer
												+ "'";
										bean4.truncateTableData(sql3);
										
										break flg;
									}
								}

							}

						}

					}

				}

			}

			String sqlFinal = "select * from BLANK_ORDER_TEMP2 order by id+0";

			// bean4.exportDataExcle(SqlStr.export_data, exportPath, "excle");
			bean4.exportDataExcle(sqlFinal, exportPath, "excle");

			bean4.truncateTableData(SqlStr.drop_sql);
			String suf = DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
			boolean flgBank = false;
			boolean flgBack = false;
			boolean flgMapping = false;
			String BackUpPath = p.getProperty("backUp.path");
			File BackUpFile = new File(BackUpPath);
			if (!BackUpFile.exists()) {
				BackUpFile.mkdirs();
			}

			if (blankOrderFile != null && blankOrderFile.exists()) {
				String name = blankOrderFile.getName();
				String string = BackUpPath + name.replace(".xlsx", suf + ".xlsx");
				File fileCopy = new File(string);
				//flgBank = blankOrderFile.renameTo(fileCopy);
				log.info("Sap4.4备份移动bank.xlsx到" + fileCopy.getAbsolutePath() + (flgBank ? " 成功！ " : " 失败！ "));
			}

			if (backOrderFile != null && backOrderFile.exists()) {
				String name = backOrderFile.getName();
				String string = BackUpPath + name.replace(".txt", suf + ".txt");
				File fileCopy = new File(string);
				// flgBack = FileCopyUtils.copy(backOrderFile, fileCopy) > 0;
				//flgBack = backOrderFile.renameTo(fileCopy);
				log.info("Sap4.4备份back_order.txt到" + fileCopy.getAbsolutePath() + (flgBack ? " 成功！ " : " 失败！ "));
			}
			if (mappingFile != null && mappingFile.exists()) {
				String name = mappingFile.getName();
				String string = BackUpPath + name.replace(".xlsx", suf + ".xlsx");
				File filecopy = new File(string);
				//flgMapping = FileCopyUtils.copy(mappingFile, filecopy) > 0;
				log.info("Sap4.4备份mapping.xlsx到" + filecopy.getAbsolutePath() + (flgMapping ? " 成功！ " : " 失败！ "));
			}
			log.info("Sap4.4执行任务结束：结束时间：" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Sap4.4执行出异常：时间" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), e);
		}

		run.close();

	}

	public static void getParamDetails(String[] args) {
		try {
			String path = AppMainAll.class.getClassLoader().getResource("config.properties").getPath();
			if (args != null && args.length != 0 && StringUtils.isNoneBlank(args[0])) {
				path = args[0];
			}
			InputStream resourceAsStream = new FileInputStream(new File(path));
			p.load(resourceAsStream);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("参数启动异常", e);
		}
	}

}
