package com.astune.mcenter.object;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HookedViewModel extends ViewModel {
    protected List<Hook> onCreateHook = new ArrayList<>();

    protected List<Hook> onPauseHook = new ArrayList<>();

    protected List<Hook> onResumeHook = new ArrayList<>();

    protected List<Hook> onStartHook = new ArrayList<>();

    public void setHooks(Hook[] hooks){
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
}
