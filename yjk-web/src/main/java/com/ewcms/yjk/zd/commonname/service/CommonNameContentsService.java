package com.ewcms.yjk.zd.commonname.service;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ewcms.common.service.BaseService;
import com.ewcms.common.utils.Collections3;
import com.ewcms.common.utils.EmptyUtil;
import com.ewcms.util.PinYin;
import com.ewcms.yjk.zd.commonname.entity.Administration;
import com.ewcms.yjk.zd.commonname.entity.CommonName;
import com.ewcms.yjk.zd.commonname.entity.CommonNameContents;
import com.ewcms.yjk.zd.commonname.entity.DrugCategoryEnum;
import com.ewcms.yjk.zd.commonname.repository.CommonNameContentsRepository;
import com.google.common.collect.Lists;

/**
 * @author zhoudongchu
 */
@Service
public class CommonNameContentsService extends BaseService<CommonNameContents, Long> {

	@Autowired
	private CommonNameService commonNameService;
	@Autowired
	private AdministrationService administrationService;

	private CommonNameContentsRepository getCommonNameContentsRepository() {
		return (CommonNameContentsRepository) baseRepository;
	}

	@Override
	public CommonNameContents save(CommonNameContents m) {
		PinYin.initSpell(m);
		return super.save(m);
	}

	@Override
	public CommonNameContents update(CommonNameContents m) {
		PinYin.initSpell(m);
		m.setUpdateDate(new Date(Calendar.getInstance().getTime().getTime()));
		return super.update(m);
	}

	public List<Administration> findAdministrationByCommonName(String commonName) {
		return getCommonNameContentsRepository().findAdministrationByCommonName(commonName);
	}

	public List<CommonNameContents> findByCommonCommonNameAndCommonAdministrationIdAndDeletedFalseOrderByUpdateDateDesc(
			String commonName, Long administrationId) {
		return getCommonNameContentsRepository()
				.findByCommonCommonNameAndCommonAdministrationIdAndDeletedFalseOrderByUpdateDateDesc(commonName,
						administrationId);
	}

	public List<CommonNameContents> findByCommonIdAndDeletedFalse(Long commonId) {
		return getCommonNameContentsRepository().findByCommonIdAndDeletedFalse(commonId);
	}
	
	public Page<CommonNameContents> findByCommonIdInAndDeletedFalse(List<Long> commonIds, Pageable pageable){
		return getCommonNameContentsRepository().findByCommonIdInAndDeletedFalse(commonIds, pageable);
	}

	/**
	 * 根据申报药品查找当前大目录匹配胡数据集合
	 * 
	 * @param commonNameContentsId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<CommonNameContents> matchByCommonNameContentsId(Long commonNameContentsId, Pageable pageable) {
		CommonNameContents commonNameContentsvo = findOne(commonNameContentsId);
		List<CommonName> commonNames = commonNameService.findByNumberAndAdministrationIdAndDrugCategory(
				commonNameContentsvo.getCommon().getNumber(),
				commonNameContentsvo.getCommon().getAdministration().getId(),
				commonNameContentsvo.getCommon().getDrugCategory());

		List<Long> commonNameIds = Collections3.extractToList(commonNames, "id");
		
		return findByCommonIdInAndDeletedFalse(commonNameIds, pageable);
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
				CommonNameContents commonNameContents = new CommonNameContents();
				rows = sheet.getRow(i);
				try {
					for (int j = 0; j <= cols; j++) {
						if (columnNames[j].equals("药品ID")) {
							try {
								commonNameContents.setDrugId(rows.getCell(j).getStringCellValue().trim());
							} catch (Exception e) {
								Double drugCode = rows.getCell(j).getNumericCellValue();
								commonNameContents.setDrugId(String.valueOf(drugCode.longValue()));
							}
						} else if (columnNames[j].equals("项目名称")) {
							commonNameContents.setProjectName(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("批次")) {
							commonNameContents.setBatch(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("通用名")) {
							commonNameContents.setCommonName(rows.getCell(j).getStringCellValue().trim());
							PinYin.initSpell(commonNameContents);
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
						} else if (columnNames[j].equals("抗菌药物")) {
							try {
								Double antibacterialsed = rows.getCell(j).getNumericCellValue();
								if (antibacterialsed != null && antibacterialsed.longValue() == 1L) {
									commonNameContents.setAntibacterialsed(true);
								}
							} catch (Exception e) {
							}
						} else if (columnNames[j].equals("序号")) {
							try {
								Double orderNoTemp = rows.getCell(j).getNumericCellValue();
								commonNameContents.setOrderNo(String.valueOf(orderNoTemp.longValue()));
							} catch (Exception e) {
								commonNameContents.setOrderNo(rows.getCell(j).getStringCellValue().trim());
							}
						} else if (columnNames[j].equals("医保目录编号")) {
							try {
								commonNameContents.setMedicalDirNo(rows.getCell(j).getStringCellValue().trim());
							} catch (Exception e) {
								Double medicalDirNoTemp = rows.getCell(j).getNumericCellValue();
								commonNameContents.setMedicalDirNo(String.valueOf(medicalDirNoTemp.longValue()));
							}
						} else if (columnNames[j].equals("医保目录药品名称")) {
							commonNameContents.setMedicalDirName(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("医保目录药品剂型")) {
							commonNameContents.setMedicalDirPill(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("医保类别")) {
							commonNameContents.setMedicalCategory(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("医保备注")) {
							commonNameContents.setMedicalRemark(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("类别")) {
							String drugCategoryValue = rows.getCell(j).getStringCellValue().trim();
							drugCategory = DrugCategoryEnum.valueOf(drugCategoryValue);
						} else if (columnNames[j].equals("剂型")) {
							commonNameContents.setPill(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("规格*数量")) {
							String specNumber = rows.getCell(j).getStringCellValue().trim();
							if (EmptyUtil.isStringNotEmpty(specNumber)) {
								String[] tmp = specNumber.split("\\*");
								if (tmp.length == 2) {
									commonNameContents.setSpecifications(tmp[0]);
									commonNameContents.setAmount(tmp[1]);
								}
							}
						} else if (columnNames[j].equals("商品名")) {
							commonNameContents.setProductName(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("包装单位")) {
							commonNameContents.setPackageUnit(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("生产企业")) {
							commonNameContents.setManufacturer(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("进口企业")) {
							commonNameContents.setImportEnterprise(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("采购价（元）")) {
							try {
								Double purchasePrice = rows.getCell(j).getNumericCellValue();
								if (purchasePrice == 0L) {
									commonNameContents.setPurchasePrice(null);
								} else {
									commonNameContents.setPurchasePrice(purchasePrice);
								}
							} catch (Exception e) {
								commonNameContents.setPurchasePrice(null);
							}
						} else if (columnNames[j].equals("包材")) {
							commonNameContents.setPackageMaterials(rows.getCell(j).getStringCellValue().trim());
						} else if (columnNames[j].equals("最小制剂单位")) {
							commonNameContents.setMinimalUnit(rows.getCell(j).getStringCellValue().trim());
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
						commonNameContents.setCommon(commonName);
						super.saveAndFlush(commonNameContents);
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
