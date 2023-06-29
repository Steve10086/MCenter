package com.astune.mcenter.ui;

import android.graphics.Bitmap;
import androidx.lifecycle.ViewModel;

public class InformationPageViewModel extends ViewModel {
    private boolean isCreated;

    private Bitmap backPic;

    public Bitmap getBackPic() {
        return backPic;
    }

    public void setBackPic(Bitmap backPic) {
        this.backPic = backPic;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void Created(boolean recreated) {
        isCreated = recreated;
    }

    // TODO: Implement the ViewModel
}