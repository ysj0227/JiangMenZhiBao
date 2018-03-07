package com.winsafe.jiangmenzhibao.entity;

import java.util.List;

public class logisticsBean {


	/**
	 * returnCode : 0
	 * returnMsg : 查询数据成功！
	 * flowList : [{"outWarehouse":"省销商一默认仓库","auditTime":"20170712144801","inWarehouse":"的方法发给个默认仓库"},{"outWarehouse":"公司默认仓库","auditTime":"20170712131959","inWarehouse":"省销商一默认仓库"}]
	 */

	private String returnCode;
	private String returnMsg;
	private List<FlowListBean> flowList;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public List<FlowListBean> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<FlowListBean> flowList) {
		this.flowList = flowList;
	}

	public static class FlowListBean {
		/**
		 * outWarehouse : 省销商一默认仓库
		 * auditTime : 20170712144801
		 * inWarehouse : 的方法发给个默认仓库
		 */

		private String outWarehouse;
		private String auditTime;
		private String inWarehouse;

		public String getOutWarehouse() {
			return outWarehouse;
		}

		public void setOutWarehouse(String outWarehouse) {
			this.outWarehouse = outWarehouse;
		}

		public String getAuditTime() {
			return auditTime;
		}

		public void setAuditTime(String auditTime) {
			this.auditTime = auditTime;
		}

		public String getInWarehouse() {
			return inWarehouse;
		}

		public void setInWarehouse(String inWarehouse) {
			this.inWarehouse = inWarehouse;
		}
	}
}
