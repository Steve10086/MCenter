package com.astune.mcenter.object;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class HookedFragment extends Fragment {
    private Hook[] hooks;

    protected Activity parent;

    protected HookedViewModel viewModel;

    public HookedFragment(){
        super();
    }

    public HookedFragment(Hook[] hooks) {
        super();
        this.hooks = hooks;
    }


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.parent = requireActivity();

        try {
            viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                    .get((Class<? extends HookedViewModel>) Class.forName(getArguments().getString("viewModelClass")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        viewModel.setHooks(hooks);

        for (Hook hook: viewModel.onCreateHook){
            try {
                if(hook.getParameters() == null) hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
            } catch(Exception e){
                Log.e("onCreateHook", e.getMessage());
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        try {
            for (Hook hook: viewModel.onStartHook){
                if(hook.getParameters() == null) hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
            }
        }catch(Exception e){
            Log.e("onStartHook", e.getMessage());
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        try {
            for (Hook hook: viewModel.onPauseHook){
                if(hook.getParameters() == null) hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
            }
        }catch(Exception e){
            Log.e("onPauseHook", e.getMessage());
        }
    }
}
