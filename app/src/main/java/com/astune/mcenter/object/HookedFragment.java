package com.astune.mcenter.object;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class HookedFragment extends Fragment {

    protected Activity parent;
    protected List<Hook> onCreateHook = new ArrayList<>();

    protected List<Hook> onPauseHook = new ArrayList<>();

    protected List<Hook> onResumeHook = new ArrayList<>();

    protected List<Hook> onStartHook = new ArrayList<>();

    public HookedFragment(){}

    public HookedFragment(Hook[] hooks) {
        if (hooks != null) {
            for (Hook hook : hooks) {
                switch (hook.getStateId()) {
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.parent = requireActivity();
        for (Hook hook: onCreateHook){
            try {
                if(hook.getParameters() == null) hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
            } catch(Exception e){
                Log.e("onPauseHook", e.getMessage());
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        try {
            for (Hook hook:onPauseHook){
                if(hook.getParameters() == null) hook.getMethod().invoke(hook.getParent()); else hook.getMethod().invoke(hook.getParent(), hook.getParameters());
            }
        }catch(Exception e){
            Log.e("onPauseHook", e.getMessage());
        }
    }
}
