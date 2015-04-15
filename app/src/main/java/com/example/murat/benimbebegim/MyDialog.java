package com.example.murat.benimbebegim;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

public class MyDialog {
	
	public static Dialog displayProgress(Activity activity) {
		Dialog dialog;
		View child = activity.getLayoutInflater().inflate(R.layout.custom_dialog, null);
		dialog = new Dialog(activity);
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(child);
		dialog.show();
		
		return dialog;
	}


}
