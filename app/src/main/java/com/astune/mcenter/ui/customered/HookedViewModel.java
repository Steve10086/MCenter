package com.astune.mcenter.ui.customered;

import androidx.lifecycle.ViewModel;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.utils.enums.ActivityState;

import java.util.ArrayList;
import java.util.List;

public class HookedViewModel extends ViewModel {
    protected List<Hook> onCreateHook = new ArrayList<>();

    protected List<Hook> onPauseHook = new ArrayList<>();

    protected List<Hook> onResumeHook = new ArrayList<>();

    protected List<Hook> onStartHook = new ArrayList<>();

    protected List<Hook> onStopHook = new ArrayList<>();

    public void setHooks(Hook[] hooks){
        if (hooks != null) {
            for (Hook hook : hooks) {
                switch (hook.getStateId()) {
                    case ActivityState.ON_CREATE:
                        onCreateHook.add(hook);
                        break;

                    case ActivityState.ON_START:
                        onStartHook.add(hook);
                        break;

                    case ActivityState.ON_PAUSE:
                        onPauseHook.add(hook);
                        break;

                    case ActivityState.ON_RESUME:
                        onResumeHook.add(hook);
                        break;

                    case ActivityState.ON_STOP:
                        onStopHook.add(hook);
                        break;
                }
            }

        }
    }
}
