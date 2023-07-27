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

    private HookedViewModel mViewModel;
    private final boolean[] disableHooks = new boolean[]{false, false, false, false, false};
    private boolean[] isNextHookDisabled = new boolean[]{false, false, false, false, false};

    public HookedFragment(){
        super();
    }

    public HookedFragment(Hook[] hooks) {
        super();
        this.hooks = hooks;
    }

    public<T extends HookedViewModel> T getViewModel(Class<T> clazz) {
        if (clazz.isInstance(mViewModel)) {
            return (T) mViewModel;
        } else {
            throw new IllegalArgumentException("HookedViewModel cannot be cast into " + clazz.getName());
        }
    }

    public void disableHook(int state){
        disableHooks[state] = true;
        isNextHookDisabled[state] = true;
    }

    public void disableNextHook(int state){
        disableHook(state);
        isNextHookDisabled[state] = false;
    }

    public void doOnStateHooks(int state){
        List<Hook> doHook;
        switch (state){
            case ActivityState.ON_CREATE:
                doHook = mViewModel.onCreateHook;
                break;
            case ActivityState.ON_START:
                doHook = mViewModel.onStartHook;
                break;
            case ActivityState.ON_PAUSE:
                doHook = mViewModel.onPauseHook;
                break;
            case ActivityState.ON_RESUME:
                doHook = mViewModel.onResumeHook;
                break;
            case ActivityState.ON_STOP:
                doHook = mViewModel.onStopHook;
                break;
            default: throw new NoSuchElementException("unknown activity state");
        }
        if (!disableHooks[state]){
            try {
                for (Hook hook: doHook){
                    if(hook.getParameters() == null) hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
                }
            }catch(Exception e){
                Log.e("hookedFragment", e.getMessage());
            }
        }else{
            if (!isNextHookDisabled[state]){
                disableHooks[state] = false;
            }
        }
    }


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.parent = requireActivity();

        try {
            mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                    .get((Class<? extends HookedViewModel>) Class.forName(getArguments().getString("viewModelClass")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        mViewModel.setHooks(hooks);

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

    public static Bundle setDefaultBundle(Class<com.astune.mcenter.ui.LinkInsertionPageViewModel> viewmodelClass){
        Bundle bundle = new Bundle();
        bundle.putString("viewModelClass", viewmodelClass.getName());
        return bundle;
    }
}
