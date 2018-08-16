package info.androidhive.recyclerviewsearch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class passwordcard extends AppCompatActivity {

    private WebView mWebView;
    ProgressDialog mProgress;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(true);
        }
        setContentView(R.layout.activity_passwordcard);
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //Check if network or internet is available
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            // Open your website with WebView if network or internet connection available
            //Toast.makeText(MainActivity.this, "Network Available", Toast.LENGTH_LONG).show();
            mProgress = ProgressDialog.show(this, "Loading...", "Please wait...");
            //Here is the template path from assets folder
            mWebView.loadUrl("file:///android_asset/passwordcard.html");
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            // Force links and redirects to open in the WebView instead of in a browser
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    //show webview
                    findViewById(R.id.activity_main_webview).setVisibility(View.VISIBLE);
                    //Remove the progress dialog after the website loaded completely
                    if(mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                }
            });
            //Hid the TextView from main activity with id NoNetworkText when Internet connection or network available.
            findViewById(R.id.NoNetworkText).setVisibility(View.GONE);
        }
        else
        {
            // Do this if network or internet connection is not available



            // Open your website with WebView if network or internet connection available
            //Toast.makeText(MainActivity.this, "Network Available", Toast.LENGTH_LONG).show();
            mProgress = ProgressDialog.show(this, "Loading...", "Please wait...");
            //Here is the template path from assets folder
            mWebView.loadUrl("file:///android_asset/passwordcard.html");
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            // Force links and redirects to open in the WebView instead of in a browser
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    //show webview
                    findViewById(R.id.activity_main_webview).setVisibility(View.VISIBLE);
                    //Remove the progress dialog after the website loaded completely
                    if(mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                }
            });
            //Hid the TextView from main activity with id NoNetworkText when Internet connection or network available.
            findViewById(R.id.NoNetworkText).setVisibility(View.GONE);

        }
    }


}
