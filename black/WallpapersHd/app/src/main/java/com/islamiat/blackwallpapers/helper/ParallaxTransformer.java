package com.islamiat.blackwallpapers.helper;


import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

import com.islamiat.blackwallpapers.R;

public class ParallaxTransformer implements PageTransformer {


	@Override
	public void transformPage(View page, float position) {

		if (position > -1 && position < 1) {
			page.setAlpha(1);

			float width = (float)page.getWidth();
			//page.setTranslationX(position);
			//page.findViewById(R.id.image_view).setTranslationX(-position * (float)(width / 2));  // Parallax
			page.findViewById(R.id.image_view).setTranslationX(-position * width);
			//page.findViewById(R.id.layout_tools).setTranslationX(-position * width);


		} else {
			page.setAlpha(0);
		}


	}


}
