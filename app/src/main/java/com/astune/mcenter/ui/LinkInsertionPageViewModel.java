package com.astune.mcenter.ui;

import androidx.lifecycle.ViewModel;
import com.astune.mcenter.object.Link.Link;
import com.astune.mcenter.object.Room.MCenterDB;
import io.reactivex.rxjava3.core.Completable;

public class LinkInsertionPageViewModel extends ViewModel {
    public Completable saveEvent(Link link){
        return MCenterDB.Companion.getDB().getResponseLinkDao(link.getType()).insert(link);
    }
}