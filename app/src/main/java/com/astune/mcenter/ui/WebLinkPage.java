package com.astune.mcenter.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.FragmentWebLinkPageBinding;
import com.astune.mcenter.ui.customered.LinkFragment;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A linkFragment containing a webView that can act as a browser
 */
public class WebLinkPage extends LinkFragment {

    private WebLinkPageViewModel viewModel;

    private FragmentWebLinkPageBinding layout;

    private OnBackPressedCallback onBackPressedCallback;

    private final AtomicBoolean isReloading = new AtomicBoolean(false);

    public WebLinkPage() {super();}

    public WebLinkPage(int x, int y) {super(x, y);}

    public static WebLinkPage newInstance() {
        return new WebLinkPage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = FragmentWebLinkPageBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onStart(){
        super.onStart();
        viewModel = new ViewModelProvider(this).get(WebLinkPageViewModel.class);

        initSetting();
        initListener();
        initClient();

        //set address
        layout.webWebview.loadUrl(getArguments().getString("address"));
        layout.webUrlText.setText(getArguments().getString("address"));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        layout = null;
    }

    // init webSetting
    private void initSetting(){
        WebSettings settings = layout.webWebview.getSettings();

        settings.setPluginState(WebSettings.PluginState.ON);

        //appearance
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);

        //accessibility
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDisplayZoomControls(false);
    }

    //init chrome and view client
    public void initClient(){
        layout.webWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {// bind the loading progressBar
                layout.webLoadProgress.setProgress(newProgress);
                if (newProgress == 100) {
                    layout.webLoadProgress.postDelayed(() -> layout.webLoadProgress.setVisibility(View.INVISIBLE), 100);
                    isReloading.set(false);
                    layout.webReloadBtn.setForeground(getResources().getDrawable(R.drawable.reload_icon));
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {// bind the web title
                super.onReceivedTitle(view, title);
                layout.webUrlText.setText(title);
            }
        });

        layout.webWebview.setWebViewClient(new WebViewClient(){
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload){// disable the interdiction to back when website cannot goback()
                super.doUpdateVisitedHistory(view, url, isReload);
                onBackPressedCallback.setEnabled(layout.webWebview.canGoBack());
            }
        });
    }

    //init chrome and view client
    private void initListener(){
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (layout.webWebview.canGoBack()) layout.webWebview.goBack();
            } //interdict back action to webview.goback()
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(onBackPressedCallback);

        //bind forward & back & reload btn
        layout.webBackBtn.setOnClickListener(c ->{
            layout.webWebview.goBack();
        });

        layout.webForwardBtn.setOnClickListener(c ->{
            layout.webWebview.goForward();
        });

        layout.webReloadBtn.setOnClickListener(c ->{
            // change the image between reload & cancel
            if (!isReloading.get()){
                isReloading.set(true);
                layout.webReloadBtn.setForeground(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
                layout.webWebview.reload();
            }else{
                isReloading.set(false);
                layout.webReloadBtn.setForeground(getResources().getDrawable(R.drawable.reload_icon));
                layout.webWebview.stopLoading();
            }

        });
    }

}