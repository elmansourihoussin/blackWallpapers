package com.islamiat.blackwallpapers.helper;


import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class AccordionTransformer implements PageTransformer {


	@Override
	public void transformPage(View page, float position) {

		if (position > -1 && position < 1) {
			page.setAlpha(1);

			float width = (float)page.getWidth();
			page.setTranslationX(-width * position);


			if (position < 0) {
				page.setPivotX(0);
				page.setScaleX(1.0F + position);
			} else {
				page.setPivotX((float)page.getWidth());
				page.setScaleX(1.0F - position);
			}

		} else {

			page.setAlpha(0);
		}


	}


}
