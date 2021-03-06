package com.ewcms.hl7v2.segment;

import java.util.Date;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;

/**
 *
 * @author wu_zhijun
 */
public class QRDSegment {
	
	//患者卡号
	private String practiceNo;
	
	public QRDSegment(String practiceNo){
		this.practiceNo = practiceNo;
	}
	
	public void setQrdSegment(AbstractSegment qrd) throws HL7Exception{
		if (qrd instanceof ca.uhn.hl7v2.model.v21.segment.QRD){
			setQrd_V21((ca.uhn.hl7v2.model.v21.segment.QRD)qrd);
		}else if (qrd instanceof ca.uhn.hl7v2.model.v22.segment.QRD){
			setQrd_V22((ca.uhn.hl7v2.model.v22.segment.QRD)qrd);
		}else if (qrd instanceof ca.uhn.hl7v2.model.v23.segment.QRD){
			setQrd_V23((ca.uhn.hl7v2.model.v23.segment.QRD)qrd);
		}else if (qrd instanceof ca.uhn.hl7v2.model.v231.segment.QRD){
			setQrd_V231((ca.uhn.hl7v2.model.v231.segment.QRD)qrd);
		}else if (qrd instanceof ca.uhn.hl7v2.model.v24.segment.QRD){
			setQrd_V24((ca.uhn.hl7v2.model.v24.segment.QRD)qrd);
		}else if (qrd instanceof ca.uhn.hl7v2.model.v25.segment.QRD){
			setQrd_V25((ca.uhn.hl7v2.model.v25.segment.QRD)qrd);
		}else if (qrd instanceof ca.uhn.hl7v2.model.v251.segment.QRD){
			setQrd_V251((ca.uhn.hl7v2.model.v251.segment.QRD)qrd);
		}else if (qrd instanceof ca.uhn.hl7v2.model.v26.segment.QRD){
			setQrd_V26((ca.uhn.hl7v2.model.v26.segment.QRD)qrd);
		}
	}
	
	/****************************************************************v2.1版本*********************************************************/
	private void setQrd_V21(ca.uhn.hl7v2.model.v21.segment.QRD qrd) throws HL7Exception{
		qrd.getQrd1_QUERYDATETIME().setValue(new Date());
		qrd.getQrd2_QUERYFORMATCODE().setValue("R");
		qrd.getQrd3_QUERYPRIORITY().setValue("I");
		qrd.getQrd8_WHOSUBJECTFILTER(0).setValue(getPracticeNo());
	}
	
	/****************************************************************v2.2版本*********************************************************/
	private void setQrd_V22(ca.uhn.hl7v2.model.v22.segment.QRD qrd) throws HL7Exception{
		qrd.getQrd1_QueryDateTime().getTs1_TimeOfAnEvent().setValue(new Date());
		qrd.getQrd2_QueryFormatCode().setValue("R");
		qrd.getQrd3_QueryPriority().setValue("I");
		qrd.getQrd8_WhoSubjectFilter(0).setValue(getPracticeNo());
	}
	
	/****************************************************************v2.3版本*********************************************************/
	private void setQrd_V23(ca.uhn.hl7v2.model.v23.segment.QRD qrd) throws HL7Exception{
		qrd.getQrd1_QueryDateTime().getTs1_TimeOfAnEvent().setValue(new Date());
		qrd.getQrd2_QueryFormatCode().setValue("R");
		qrd.getQrd3_QueryPriority().setValue("I");
		qrd.getQrd8_WhoSubjectFilter(0).getXcn1_IDNumber().setValue(getPracticeNo());
	}
	
	/****************************************************************v2.3.1版本*********************************************************/
	private void setQrd_V231(ca.uhn.hl7v2.model.v231.segment.QRD qrd) throws HL7Exception{
		qrd.getQrd1_QueryDateTime().getTs1_TimeOfAnEvent().setValue(new Date());
		qrd.getQrd2_QueryFormatCode().setValue("R");
		qrd.getQrd3_QueryPriority().setValue("I");
		qrd.getQrd8_WhoSubjectFilter(0).getXcn1_IDNumber().setValue(getPracticeNo());
	}
	
	/****************************************************************v2.4版本*********************************************************/
	private void setQrd_V24(ca.uhn.hl7v2.model.v24.segment.QRD qrd) throws HL7Exception{
		qrd.getQrd1_QueryDateTime().getTs1_TimeOfAnEvent().setValue(new Date());
		qrd.getQrd2_QueryFormatCode().setValue("R");
		qrd.getQrd3_QueryPriority().setValue("I");
		qrd.getQrd8_WhoSubjectFilter(0).getXcn1_IDNumber().setValue(getPracticeNo());
	}
	
	/****************************************************************v2.5版本*********************************************************/
	private void setQrd_V25(ca.uhn.hl7v2.model.v25.segment.QRD qrd) throws HL7Exception{
		qrd.getQrd1_QueryDateTime().getTs1_Time().setValue(new Date());
		qrd.getQrd2_QueryFormatCode().setValue("R");
		qrd.getQrd3_QueryPriority().setValue("I");
		qrd.getQrd8_WhoSubjectFilter(0).getXcn1_IDNumber().setValue(getPracticeNo());
	}

	/****************************************************************v2.5.1版本*********************************************************/
	private void setQrd_V251(ca.uhn.hl7v2.model.v251.segment.QRD qrd) throws HL7Exception{
		qrd.getQrd1_QueryDateTime().getTs1_Time().setValue(new Date());
		qrd.getQrd2_QueryFormatCode().setValue("R");
		qrd.getQrd3_QueryPriority().setValue("I");
		qrd.getQrd8_WhoSubjectFilter(0).getXcn1_IDNumber().setValue(getPracticeNo());
	}

	/****************************************************************v2.6版本*********************************************************/
	private void setQrd_V26(ca.uhn.hl7v2.model.v26.segment.QRD qrd) throws HL7Exception{
		qrd.getQrd1_QueryDateTime().setValue(new Date());
		qrd.getQrd2_QueryFormatCode().setValue("R");
		qrd.getQrd3_QueryPriority().setValue("I");
		qrd.getQrd8_WhoSubjectFilter(0).getXcn1_IDNumber().setValue(getPracticeNo());
	}

	public String getPracticeNo() {
		return practiceNo;
	}

	public void setPracticeNo(String practiceNo) {
		this.practiceNo = practiceNo;
	}
}
