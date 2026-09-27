package com.sandip945235.najara;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class PlayerActivity extends AppCompatActivity {

    WebView webView;
    FrameLayout fullscreenContainer;
    ImageButton btnRotate, backBtn, btnDownload;
    TextView playerTitle;
    ProgressBar progressBar;
    View customView;
    WebChromeClient.CustomViewCallback customCallback;
    String videoPageUrl;
    String directDownloadLink;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_player);

        webView = findViewById(R.id.webView);
        fullscreenContainer = findViewById(R.id.fullscreenContainer);
        btnRotate = findViewById(R.id.btnRotate);
        backBtn = findViewById(R.id.backBtn);
        btnDownload = findViewById(R.id.btnDownload);
        playerTitle = findViewById(R.id.playerTitle);
        progressBar = findViewById(R.id.progressBar);

        videoPageUrl = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        directDownloadLink = getIntent().getStringExtra("downloadLink");
        playerTitle.setText(title);

        if (videoPageUrl == null || videoPageUrl.isEmpty()) {
            Toast.makeText(this, "Video URL खाली है", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);
        s.setSupportZoom(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setSupportMultipleWindows(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        String chromeUA = "Mozilla/5.0 (Linux; Android 10; SM-G975F) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/120.0.0.0 Mobile Safari/537.36";
        s.setUserAgentString(chromeUA);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        final Map<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://www.google.com/");
        headers.put("Accept-Language", "en-US,en;q=0.9");

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                startDownload(url, userAgent, contentDisposition, mimeType);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString(), headers);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (customView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                customView = view;
                customCallback = callback;
                fullscreenContainer.addView(view);
                fullscreenContainer.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                hideSystemUI();
            }

            @Override
            public void onHideCustomView() {
                if (customView == null) return;
                fullscreenContainer.removeView(customView);
                fullscreenContainer.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                customView = null;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                showSystemUI();
                if (customCallback != null) customCallback.onCustomViewHidden();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) progressBar.setVisibility(View.GONE);
            }
        });

        loadVideoUrl(videoPageUrl, headers);

        backBtn.setOnClickListener(v -> {
            if (customView != null) {
                webView.getSettings().setLoadWithOverviewMode(true);
                return;
            }
            if (webView.canGoBack()) webView.goBack();
            else finish();
        });

        btnDownload.setOnClickListener(v -> {
            if (directDownloadLink != null && !directDownloadLink.isEmpty()) {
                startDownload(directDownloadLink, "", "", "video/mp4");
            } else {
                Toast.makeText(this, "Download link नहीं है",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnRotate.setOnClickListener(v -> {
            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                hideSystemUI();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                showSystemUI();
            }
        });
    }

    private void loadVideoUrl(String url, Map<String, String> headers) {
        if (url.contains("youtube.com/watch?v=") || url.contains("youtu.be/")) {
            String vid = extractYouTubeId(url);
            webView.loadUrl(
                    "https://www.youtube.com/embed/" + vid +
                            "?autoplay=1&playsinline=1&rel=0", headers);
        } else if (url.endsWith(".mp4") || url.endsWith(".m3u8")
                || url.endsWith(".webm") || url.endsWith(".mkv")) {
            String html = "<html><head><meta name='viewport' " +
                    "content='width=device-width,initial-scale=1'></head>" +
                    "<body style='margin:0;background:#000;overflow:hidden'>" +
                    "<video width='100%' height='100%' controls autoplay " +
                    "playsinline preload='auto' " +
                    "style='position:fixed;top:0;left:0'>" +
                    "<source src='" + url + "' type='video/mp4'>" +
                    "</video></body></html>";
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        } else {
            webView.loadUrl(url, headers);
        }
    }

    private void startDownload(String url, String userAgent,
                               String contentDisposition, String mimeType) {
        try {
            String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
            if (fileName == null || fileName.isEmpty()) {
                fileName = "video_" + System.currentTimeMillis() + ".mp4";
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setMimeType(mimeType != null ? mimeType : "video/mp4");
            if (userAgent != null && !userAgent.isEmpty()) {
                request.addRequestHeader("User-Agent", userAgent);
            }
            request.addRequestHeader("Cookie",
                    CookieManager.getInstance().getCookie(url));
            request.addRequestHeader("Referer", "https://www.google.com/");
            request.setDescription("Downloading video...");
            request.setTitle(fileName);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS, fileName);

            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (dm != null) {
                dm.enqueue(request);
                Toast.makeText(this, "Download शुरू 📥", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Download fail: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private String extractYouTubeId(String url) {
        if (url.contains("v=")) {
            String id = url.substring(url.indexOf("v=") + 2);
            int amp = id.indexOf('&');
            if (amp > 0) id = id.substring(0, amp);
            return id;
        } else if (url.contains("youtu.be/")) {
            String id = url.substring(url.indexOf("youtu.be/") + 9);
            int q = id.indexOf('?');
            if (q > 0) id = id.substring(0, q);
            return id;
        }
        return "";
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void onBackPressed() {
        if (customView != null) {
            webView.getSettings().setLoadWithOverviewMode(true);
            return;
        }
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            showSystemUI();
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.destroy();
            webView = null;
        }
    }
                                   }
