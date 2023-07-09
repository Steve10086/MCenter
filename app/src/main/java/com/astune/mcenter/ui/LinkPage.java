package com.astune.mcenter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.astune.mcenter.databinding.FragmentLinkPageBinding;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.ui.customered.HookedFragment;

public class LinkPage extends HookedFragment {

    private LinkPageViewModel mViewModel;

    private FragmentLinkPageBinding layout;

    public static LinkPage newInstance() {
        return new LinkPage();
    }

    public LinkPage(){super();}

    public LinkPage(Hook[] hooks){super(hooks);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = FragmentLinkPageBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewModel = (LinkPageViewModel) viewModel;

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        layout = null;
    }

}