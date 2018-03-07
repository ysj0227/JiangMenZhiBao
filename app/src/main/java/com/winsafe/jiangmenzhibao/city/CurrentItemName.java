package com.winsafe.jiangmenzhibao.city;

public class CurrentItemName {

	public static int currentItemPosition(String provinceName) {
		int position = 0;
		if (provinceName.contains("北京")) {
			position=0;
		}else if(provinceName.contains("天津")){
			position=1;
		}else if(provinceName.contains("河北")){
			position=2;
		}else if(provinceName.contains("山西")){
			position=3;
		}else if(provinceName.contains("内蒙古自治区(西)")){
			position=4;
		}else if(provinceName.contains("内蒙古自治区(东)")){
			position=5;
		}else if(provinceName.contains("辽宁")){
			position=6;
		}else if(provinceName.contains("吉林")){
			position=7;
		}else if(provinceName.contains("黑龙江")){
			position=8;
		}else if(provinceName.contains("上海")){
			position=9;
		}else if(provinceName.contains("江苏")){
			position=10;
		}else if(provinceName.contains("浙江")){
			position=11;
		}else if(provinceName.contains("安徽")){
			position=12;
		}else if(provinceName.contains("福建")){
			position=13;
		}else if(provinceName.contains("江西")){
			position=14;
		}else if(provinceName.contains("山东")){
			position=15;
		}else if(provinceName.contains("河南")){
			position=16;
		}else if(provinceName.contains("湖北")){
			position=17;
		}else if(provinceName.contains("湖南")){
			position=18;
		}else if(provinceName.contains("广东")){
			position=19;
		}else if(provinceName.contains("广西壮族")){
			position=20;
		}else if(provinceName.contains("海南")){
			position=21;
		}else if(provinceName.contains("重庆")){
			position=22;
		}else if(provinceName.contains("四川")){
			position=23;
		}else if(provinceName.contains("贵州")){
			position=24;
		}else if(provinceName.contains("云南")){
			position=25;
		}else if(provinceName.contains("西藏自治区")){
			position=26;
		}else if(provinceName.contains("陕西")){
			position=27;
		}else if(provinceName.contains("甘肃")){
			position=28;
		}else if(provinceName.contains("青海")){
			position=29;
		}else if(provinceName.contains("宁夏")){
			position=30;
		}else if(provinceName.contains("新疆")){
			position=31;
		}
		return position;

	}

}
