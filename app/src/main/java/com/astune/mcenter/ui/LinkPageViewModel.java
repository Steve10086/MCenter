package com.astune.mcenter.ui;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.astune.mcenter.object.Link.Link;
import com.astune.mcenter.object.Room.MCenterDB;
import com.astune.mcenter.ui.customered.HookedViewModel;
import com.astune.mcenter.utils.enums.LinkType;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class LinkPageViewModel extends HookedViewModel {
    private final MutableLiveData<List<? extends Link>> linkList = new MutableLiveData<>(new ArrayList<>());
    private final MCenterDB db;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private int id;

    public LinkPageViewModel(){
        super();
        Log.i("MainViewModel", "Started");
        db = MCenterDB.Companion.getDB();
    }

    public void setId(int id) {
        this.id = id;
    }

    public LiveData<List<? extends Link>> getLinkList(){
        return linkList;
    }

    public void refreshLinkTable() {
        disposable.add(Completable.fromAction(() -> {
            List<Link> list = new ArrayList<>();
            for (LinkType t : LinkType.values()) {
                if (!t.equals(LinkType.NEW_LINK)) list.addAll(db.getResponseLinkDao(t).getByDevice(id));
            }
            linkList.postValue(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            Log.i("LinkViewModel", "Links got");
            linkList.setValue(linkList.getValue());
        }));
    }

    public void deleteLink(@NotNull Link link){
        disposable.add(db.getResponseLinkDao(link.getType()).delete(link).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(()->{
            Log.i("LinkViewModel", "Link deleted");
            refreshLinkTable();
        }));
    }

    public void finish(){
        disposable.clear();
    }
}