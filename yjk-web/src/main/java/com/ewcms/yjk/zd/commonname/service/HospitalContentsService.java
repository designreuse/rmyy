package com.ewcms.yjk.zd.commonname.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ewcms.common.service.BaseService;
import com.ewcms.common.utils.EmptyUtil;
import com.ewcms.util.PinYin;
import com.ewcms.yjk.zd.commonname.entity.Administration;
import com.ewcms.yjk.zd.commonname.entity.CommonName;
import com.ewcms.yjk.zd.commonname.entity.CommonNameContents;
import com.ewcms.yjk.zd.commonname.entity.DrugCategoryEnum;
import com.ewcms.yjk.zd.commonname.entity.HospitalContents;
import com.ewcms.yjk.zd.commonname.repository.HospitalContentsRepository;
import com.google.common.collect.Lists;

/**
 * @author zhoudongchu
 */
@Service
public class HospitalContentsService extends BaseService<HospitalContents, Long> {
	@Autowired
	private CommonNameContentsService commonNameContentsService;
	@Autowired
	private CommonNameService commonNameService;
	@Autowired
	private AdministrationService administrationService;

	private HospitalContentsRepository getHospitalContentsRepository() {
		return (HospitalContentsRepository) baseRepository;
	}

	public List<HospitalContents> findByCommonIdAndDeletedFalse(Long commonId) {
		return getHospitalContentsRepository().findByCommonIdAndDeletedFalse(commonId);
	}

	/**
	 * 根据申报药品查找当前院药品目录在用医院药品集合
	 * 
	 * @param commonNameContentsId
	 * @return
	 */
	public List<HospitalContents> matchByCommonNameContentsId(Long commonNameContentsId) {
		CommonNameContents commonNameContentsvo = commonNameContentsService.findOne(commonNameContentsId);
		List<CommonName> commonNameList = commonNameService.findByNumberAndAdministrationIdAndDrugCategory(
				commonNameContentsvo.getCommon().getNumber(),
				commonNameContentsvo.getCommon().getAdministration().getId(),
				commonNameContentsvo.getCommon().getDrugCategory());
		List<HospitalContents> hospitalContentsList = new ArrayList<HospitalContents>();
		for (CommonName commonName : commonNameList) {
			hospitalContentsList.addAll(findByCommonIdAndDeletedFalse(commonName.getId()));
		}
		return hospitalContentsList;
	}

	@Override
	public HospitalContents save(HospitalContents m) {
		PinYin.initSpell(m);
		return super.save(m);
	}

	@Override
	public HospitalContents update(HospitalContents m) {
		PinYin.initSpell(m);
		m.setUpdateDate(new Date(Calendar.getInstance().getTime().getTime()));
		return super.update(m);
	}

	public List<Integer> importExcel(InputStream in) {
		List<Integer> noSave = Lists.newArrayList();

		try {
			POIFSFileSystem fs = new POIFSFileSystem(in);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			int records = sheet.getLastRowNum();

			// 获得列名，为第0行位置
			HSSFRow rows = sheet.getRow(0);
			int cols = rows.getLastCellNum() - 1;
			String columnNames[] = new String[cols + 1];

			for (int i = 0; i <= cols; i++) {
				columnNames[i] = rows.getCell(i).getStringCellValue().trim();
			}

			// 获得数据，数据从第1行开始
			for (int i = 1; i <= records; i++) {
				String extactCommonName = "", number = "";
				DrugCategoryEnum drugCategory = DrugCategoryEnum.H;
				Long administrationId = 1L;
				HospitalContents hospitalContents = new HospitalContents();
				rows = sheet.getRow(i);
				try {
					for (int j = 0; j <= cols; j++) {
						if (columnNames[j].equals("药品代码")) {
							try {
								hospitalContents.setDrugCode(rows.getCell(j).getStringCellValue().trim());
							} catch (Exception e) {
								Double drugCode = rows.getCell(j).getNumericCellValue();
								hospitalContents.setDrugCode(String.valueOf(drugCode.longValue()));
							}
						} else if (columnNames[j].equals("药品通用名")) {
							hospitalContents.setCommonName(rows.getCell(j).getStringCellValue().trim());
							PinYin.initSpell(hospitalContents);
						} else if (columnNames[j].equals("给药途径")) {
							try {
								Double administrationIdTemp = rows.getCell(j).getNumericCellValue();
								administrationId = administrationIdTemp.longValue();
							} catch (Exception e) {
								// administrationId = Long.valueOf(rows.getCell(j).getStringCellValue().trim());
							}
						} else if (columnNames[j].equals("提取通用名")) {
							extactCommonName = rows.getCell(j).getStringCellValue().trim();
						} else if (columnNames[j].equals("编号")) {
							try {
								Double numberTemp = rows.getCell(j).getNumericCellValue();
								number = String.valueOf(numberTemp.longValue());
							} catch (Exception e) {
								number = rows.getCell(j).getStringCellValue().trim();
							}
						} else if (columnNames[j].equals("剂型")) {
							hospitalContents.setPill(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("规格*数量")) {
							String specNumber = rows.getCell(j).getStringCellValue().trim();
							if (EmptyUtil.isStringNotEmpty(specNumber)) {
								String[] tmp = specNumber.split("\\*");
								if (tmp.length == 2) {
									hospitalContents.setSpecifications(tmp[0]);
									hospitalContents.setAmount(tmp[1]);
								}
							}
						} else if (columnNames[j].equals("生产企业")) {
							hospitalContents.setManufacturer(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("目录分类")) {
							hospitalContents.setContentCategory(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("备注")) {
							hospitalContents.setRemark(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("药品分类大类")) {
							hospitalContents.setDrugMajor(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("药品分类")) {
							hospitalContents.setDrugCategory(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("原备注")) {
							hospitalContents.setOldRemark(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("配送公司")) {
							hospitalContents.setDiscom(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("原类别")) {
							hospitalContents.setOriginalCategory(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("医保等信息")) {
							hospitalContents.setMedicalInfo(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("原质量层次")) {
							hospitalContents.setQualityLevel(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("类别")) {
							String drugCategoryValue = rows.getCell(j).getStringCellValue().trim();
							drugCategory = DrugCategoryEnum.valueOf(drugCategoryValue);
						}
					}
					List<CommonName> commonNameList = commonNameService
							.findByCommonNameAndNumberAndAdministrationIdAndDrugCategory(extactCommonName, number,
									administrationId, drugCategory);
					CommonName commonName = null;
					if (EmptyUtil.isCollectionEmpty(commonNameList)) {
						Administration administration = administrationService.findOne(administrationId);
						if (!EmptyUtil.isNull(administration)) {
							commonName = new CommonName();
							commonName.setCommonName(extactCommonName);
							commonName.setAdministration(administration);
							commonName.setDeleted(Boolean.FALSE);
							commonName.setDrugCategory(drugCategory);
							commonName.setEnabled(Boolean.TRUE);
							commonName.setNumber(number);
							commonNameService.saveAndFlush(commonName);
						} else {
							noSave.add(i + 1);
						}
					} else {
						commonName = commonNameList.get(0);
					}

					if (commonName != null) {
						hospitalContents.setCommon(commonName);
						super.saveAndFlush(hospitalContents);
					}
				} catch (Exception e) {
					noSave.add(i + 1);
				}
			}
		} catch (Exception e) {
		} finally {

		}
		return noSave;
	}

}
