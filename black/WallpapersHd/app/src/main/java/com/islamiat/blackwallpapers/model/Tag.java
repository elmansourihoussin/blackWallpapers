package com.islamiat.blackwallpapers.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class Tag extends SugarRecord{

	public long id;
	public String name;
	public String alias;
	public String imageUrl;

	public Tag() {
	}

	public Tag(String name, String alias, String imageUrl) {
		this.name = name;
		this.alias = alias;
		this.imageUrl = imageUrl;
	}

	public static List<Tag> getAllTags(){
		List<Tag> tagList = new ArrayList<>();

		tagList.add(new Tag("Animal","animal","https://c1.staticflickr.com/3/2892/33729786056_5668dc5276_z.jpg"));
		tagList.add(new Tag("Beach","beach","https://c2.staticflickr.com/4/3849/33770635165_0f614365b5_z.jpg"));
		tagList.add(new Tag("Bird","bird","https://c1.staticflickr.com/3/2844/32927914814_72f8c65dcc_z.jpg"));
		tagList.add(new Tag("Black and White","blackandwhite","https://c2.staticflickr.com/4/3764/33385918820_bc56f2f2c1_z.jpg"));
		tagList.add(new Tag("Boat","boat","https://c2.staticflickr.com/4/3700/33386677930_13933dabb5_z.jpg"));
		tagList.add(new Tag("Building","building","https://c1.staticflickr.com/3/2891/32957505383_e8c2d7c410_z.jpg"));
		tagList.add(new Tag("Christmas","christmas","https://c2.staticflickr.com/4/3741/33770625435_b3b64f2acb_z.jpg"));
		tagList.add(new Tag("City","city","https://c1.staticflickr.com/3/2910/33613948072_58b351b6c8_z.jpg"));
		tagList.add(new Tag("Clouds","clouds","https://c2.staticflickr.com/4/3836/32957495193_7d40b264e6_z.jpg"));
		tagList.add(new Tag("Colorful","colorful","https://c2.staticflickr.com/4/3953/33770620855_62de801be2_z.jpg"));
		tagList.add(new Tag("Design","design","https://c1.staticflickr.com/3/2887/33729722296_678c0c2333_z.jpg"));
		tagList.add(new Tag("Flowers","flowers","https://c1.staticflickr.com/3/2923/33770604885_9cddfbcab6_z.jpg"));
		tagList.add(new Tag("Green","green","https://c2.staticflickr.com/4/3894/33729705006_81f534bf81_z.jpg"));
		tagList.add(new Tag("Landscape","landscape","https://c2.staticflickr.com/4/3854/33770588865_c1f35a28e7_z.jpg"));
		tagList.add(new Tag("Light","light","https://c1.staticflickr.com/3/2914/33641281421_f225897d6c_z.jpg"));
		tagList.add(new Tag("Mountain","mountain","https://c1.staticflickr.com/3/2817/33770573085_d31d674c79_z.jpg"));
		tagList.add(new Tag("Nature","nature","https://c2.staticflickr.com/4/3794/32927397274_b77651178a_z.jpg"));
		tagList.add(new Tag("Night","night","https://c2.staticflickr.com/4/3955/33641265941_1147c0a6e4_z.jpg"));
		tagList.add(new Tag("Outdoor","outdoor","https://c2.staticflickr.com/4/3852/33615742722_408c945408_z.jpg"));
		tagList.add(new Tag("Park","park","https://c2.staticflickr.com/4/3841/33613886602_f27eb90d15_z.jpg"));
		tagList.add(new Tag("Pattern","pattern","https://c2.staticflickr.com/4/3951/33615724962_9688d5c04d_z.jpg"));
		tagList.add(new Tag("People","people","https://c1.staticflickr.com/3/2911/33641259171_4dce7776b6_z.jpg"));
		tagList.add(new Tag("Photography","photography","https://c1.staticflickr.com/3/2895/33613873202_ae3d1826c4_z.jpg"));
		tagList.add(new Tag("Plant","plant","https://c1.staticflickr.com/3/2859/33772452435_9f5f1b19f3_z.jpg"));
		tagList.add(new Tag("Red","red","https://c1.staticflickr.com/3/2846/33385852270_9f6393c8d0_z.jpg"));
		tagList.add(new Tag("River","river","https://c2.staticflickr.com/4/3941/32959332913_d98bbb8dc7_z.jpg"));
		tagList.add(new Tag("Road","road","https://c1.staticflickr.com/3/2814/33614015162_77a9069320_z.jpg"));
		tagList.add(new Tag("Sea","sea","https://c2.staticflickr.com/4/3951/33641222861_6a93ccea31_z.jpg"));
		tagList.add(new Tag("Sky","sky","https://c1.staticflickr.com/3/2866/32927354664_8d1ee6b98e_z.jpg"));
		tagList.add(new Tag("Snow","snow","https://c2.staticflickr.com/4/3946/33641219331_e048a5c00f_z.jpg"));
		tagList.add(new Tag("Spring","spring","https://c1.staticflickr.com/3/2929/33613847502_660078a21e_z.jpg"));
		tagList.add(new Tag("Street","street","https://c1.staticflickr.com/3/2906/33385843010_9578550a5a_z.jpg"));
		tagList.add(new Tag("Summer","summer","https://c1.staticflickr.com/3/2884/32957401653_41a14d22b7_z.jpg"));
		tagList.add(new Tag("Sun","sun","https://c1.staticflickr.com/3/2933/33613832822_73458202fe_z.jpg"));
		tagList.add(new Tag("Sunrise","sunrise","https://c2.staticflickr.com/4/3724/32929263654_bcd7136469_z.jpg"));
		tagList.add(new Tag("Sunset","sunset","https://c1.staticflickr.com/3/2812/32957399633_62da2e779d_z.jpg"));
		tagList.add(new Tag("Travel","travel","https://c2.staticflickr.com/4/3838/33385834070_853616bf2f_z.jpg"));
		tagList.add(new Tag("Tree","tree","https://c1.staticflickr.com/3/2839/32957398083_fa6f93c99a_z.jpg"));
		tagList.add(new Tag("Urban","urban","https://c2.staticflickr.com/4/3950/33387645760_a7d42f41cf_z.jpg"));
		tagList.add(new Tag("Water","Water","https://c2.staticflickr.com/4/3955/33641187201_10908047a8_z.jpg"));
		tagList.add(new Tag("Waterfall","waterfall","https://c2.staticflickr.com/4/3791/33613817032_cd42bdcf52_z.jpg"));
		tagList.add(new Tag("Winter","winter","https://c2.staticflickr.com/4/3839/33641180981_0355af4680_z.jpg"));

		return tagList;
	}
}
