package com.astune.mcenter.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.databinding.FragmentWebLinkPageBinding;
import com.astune.mcenter.ui.customered.LinkFragment;

public class WebLinkPage extends LinkFragment {

    private WebLinkPageViewModel viewModel;

    private FragmentWebLinkPageBinding layout;

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
        initClient();
        initListener();

        layout.webWebview.loadUrl(getArguments().getString("address"));
        layout.webUrlText.setText(getArguments().getString("address"));
    }

    private void initSetting(){
        WebSettings settings = layout.webWebview.getSettings();

        settings.setPluginState(WebSettings.PluginState.ON);

        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDisplayZoomControls(false);

        settings.setBuiltInZoomControls(true);
    }

    public void initClient(){
        layout.webWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                layout.webLoadProgress.setProgress(newProgress);
                if (newProgress == 100) {
                    layout.webLoadProgress.postDelayed(() -> layout.webLoadProgress.setVisibility(View.INVISIBLE), 100);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                layout.webUrlText.setText(title);
            }
        });
    }

    private void initListener(){
        layout.webWebview.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.KEYCODE_BACK && layout.webWebview.canGoBack()){
                layout.webWebview.goBack();
                return true;
            }
            return false;
        });

        layout.webBackBtn.setOnClickListener(c ->{
            layout.webWebview.goBack();
        });

        layout.webForwardBtn.setOnClickListener(c ->{
            layout.webWebview.goForward();
        });
    }

}