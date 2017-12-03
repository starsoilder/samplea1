package com.jiubang.ggheart.components.sidemenuadvert.tools;

import android.graphics.drawable.Drawable;

/**
 * @author zhangxi
 * @data 2013-10-16
 */
public abstract class SideToolsInfo {
	
	protected String mTitle; //Widget Title
	protected String mToolsPkgName; //Widget包名
	protected Drawable mIcon;
	protected int mDownloadtype; //1：ftp下载，2：电子市场下载，3：打开浏览器
	protected boolean mIsSelect;
	private boolean mAddMore;
	
	public boolean isAddMore() {
		return mAddMore;
	}

	public void setAddMore(boolean mAddMore) {
		this.mAddMore = mAddMore;
	}
	
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
	public String getToolsPkgName() {
		return mToolsPkgName;
	}

	public void setToolsPkgName(String mToolsPkgName) {
		this.mToolsPkgName = mToolsPkgName;
	}

	public Drawable getIcon() {
		return mIcon;
	}

	public void setIcon(Drawable icon) {
		mIcon = icon;
	}

	public int getDownloadtype() {
		return mDownloadtype;
	}

	public void setDownloadtype(int downloadtype) {
		this.mDownloadtype = downloadtype;
	}

	public boolean isSelect() {
		return mIsSelect;
	}

	public void setSelect(boolean isSelect) {
		this.mIsSelect = isSelect;
	}


}
