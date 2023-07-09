package com.astune.mcenter.ui.customered;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.utils.enums.ActivityState;

import java.util.List;
import java.util.NoSuchElementException;

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

    public void doOnStateHooks(int state){
        List<Hook> doHook;
        switch (state){
            case ActivityState.ON_CREATE:
                doHook = viewModel.onCreateHook;
                break;
            case ActivityState.ON_START:
                doHook = viewModel.onStartHook;
                break;
            case ActivityState.ON_PAUSE:
                doHook = viewModel.onPauseHook;
                break;
            case ActivityState.ON_RESUME:
                doHook = viewModel.onResumeHook;
                break;
            case ActivityState.ON_STOP:
                doHook = viewModel.onStopHook;
                break;
            default: throw new NoSuchElementException("unknown activity state");
        }
        try {
            for (Hook hook: doHook){
                if(hook.getParameters() == null) hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
            }
        }catch(Exception e){
            Log.e("hookedFragment", e.getMessage());
        }
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

        doOnStateHooks(ActivityState.ON_CREATE);

    }

    @Override
    public void onStart(){
        super.onStart();
        doOnStateHooks(ActivityState.ON_START);
    }

    @Override
    public void onPause(){
        super.onPause();
        doOnStateHooks(ActivityState.ON_PAUSE);
    }

    @Override
    public void onStop(){
        super.onStop();
        doOnStateHooks(ActivityState.ON_STOP);
    }
}
