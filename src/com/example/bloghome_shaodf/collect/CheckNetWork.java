package com.example.bloghome_shaodf.collect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class CheckNetWork {
	// 检查网络连接是否正常，正常为true，异常为false
	public static boolean checkNetworkInfo(final Context context) {
		boolean isNetWork = false;
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();

		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			isNetWork = true;
		else if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			isNetWork = true;
		else
			isNetWork = false;
		if (!isNetWork) {
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setTitle("亲，木有网络 ");
			alert.setMessage("有网走遍天下，没网寸步难行");
			alert.setPositiveButton("返回", null);
			alert.setNegativeButton("设置网络",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							context.startActivity(new Intent(
									// 进入无线网络配置界面
									android.provider.Settings.ACTION_WIFI_SETTINGS));
						}

					});
			alert.create().show();
		}

		return isNetWork;

	}

}
