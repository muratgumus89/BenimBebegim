package com.example.murat.benimbebegim.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class SpinnerNavItem {

	private String title;
	private Drawable icon;
	
	public SpinnerNavItem(String title,Drawable icon){
		this.title = title;
		this.icon = icon;
	}
	
	public String getTitle(){
		return this.title;		
	}
	
	public Drawable getIcon(){
		return this.icon;
	}
}
