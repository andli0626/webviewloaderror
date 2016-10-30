package com.lilin.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class Main extends Activity {

	String urlstr;
	
	WebView mWebView;
	View mErrorView;
	boolean mIsErrorPage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mWebView = (WebView) findViewById(R.id.webview);

		mWebView.getSettings().setJavaScriptEnabled(true);

		urlstr = "http://www.12345.ccc/";
		mWebView.loadUrl(urlstr);

		// 初始化错误页面
		initErrorPage();

		mWebView.setWebViewClient(new WebViewClient() {
			// 不允许通过外部浏览器打开URL
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("andli", "shouldOverrideUrlLoading");
				hideErrorPage();
				// return super.shouldOverrideUrlLoading(view, url);
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				Log.i("andli", "onPageFinished");
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.i("andli", "onPageStarted");
				super.onPageStarted(view, url, favicon);
			}

			// 处理URL加载错误
			// 并不能捕获404错误
			// 断网情况下可以捕获
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Log.i("andli", "errorCode:" + errorCode);
				Log.i("andli", "description:" + description);
				Log.i("andli", "failingUrl:" + failingUrl);
				showErrorPage();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

	}

	// 初始化错误页面
	protected void initErrorPage() {
		if (mErrorView == null) {
			mErrorView = View.inflate(this, R.layout.error, null);
			Button button = (Button) mErrorView.findViewById(R.id.reloadbutton);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// 重新加载webview
					mWebView.reload();
				}
			});
			mErrorView.setOnClickListener(null);
		}
	}

	// 显示错误页面
	protected void showErrorPage() {
		LinearLayout webParentView = (LinearLayout) mWebView.getParent();
		initErrorPage();
		while (webParentView.getChildCount() > 1) {
			webParentView.removeViewAt(0);
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		webParentView.addView(mErrorView, 0, lp);
		mIsErrorPage = true;
	}

	// 隐藏错误页面
	protected void hideErrorPage() {
		LinearLayout webParentView = (LinearLayout) mWebView.getParent();
		mIsErrorPage = false;
		while (webParentView.getChildCount() > 1) {
			webParentView.removeViewAt(0);
		}
	}
}