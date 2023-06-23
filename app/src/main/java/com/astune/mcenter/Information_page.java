package com.astune.mcenter;

import android.util.Log;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astune.mcenter.utils.Hook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Information_page extends Fragment {

    private InformationPageViewModel mViewModel;

    private List<Hook> onCreateHook = new ArrayList<>();

    private List<Hook> onPauseHook = new ArrayList<>();

    private List<Hook> onResumeHook = new ArrayList<>();

    private List<Hook> onStartHook = new ArrayList<>();

    public Information_page(Hook[] hooks) {
        for (Hook hook: hooks){
            switch (hook.getStateId()){
                case 0:
                    onCreateHook.add(hook);
                case 1:
                    onPauseHook.add(hook);
                case 2:
                    onResumeHook.add(hook);
                case 3:
                    onStartHook.add(hook);
            }

        }
    }

    public Information_page() {}

    public static Information_page newInstance() {
        return new Information_page();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(InformationPageViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_information_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ConstraintLayout background = requireActivity().findViewById(R.id.info_background);
        background.setOnClickListener(c->{
            try {
                for (Hook hook:onStartHook){
                    if(hook.getParameters() == null)  hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
                }
            }catch(Exception e){
                Log.e("onPauseHook", e.toString());
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        try {
            for (Hook hook:onPauseHook){
                if(hook.getParameters() == null)  hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
            }
        }catch(Exception e){
            Log.e("onPauseHook", e.getMessage());
        }
    }

}