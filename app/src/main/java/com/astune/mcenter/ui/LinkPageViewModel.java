package com.astune.mcenter.ui;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.astune.mcenter.object.Link.Link;
import com.astune.mcenter.object.Room.MCenterDB;
import com.astune.mcenter.object.Room.WebLink;
import com.astune.mcenter.ui.customered.HookedViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;



public class LinkPageViewModel extends HookedViewModel {
    private final MutableLiveData<List<? extends Link>> linkList = new MutableLiveData<>(new ArrayList<>());
    private final MCenterDB db;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public LinkPageViewModel(){
        super();
        Log.i("MainViewModel", "Started");
        db = MCenterDB.Companion.getDB();
    }

    public LiveData<List<? extends Link>> getLinkList(){
        return linkList;
    }

    public void refreshLinkTable(int id){
        disposable.add(Completable.fromAction(() ->{
            linkList.postValue(db.webLinkDao().getByDevice(id));
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(()->{
            Log.i("LinkViewModel", "Links got");
        }));
    }

    public void  deleteLink(Link link){
        db.getResponseLinkDao(link.getType()).delete(link);
    }
}