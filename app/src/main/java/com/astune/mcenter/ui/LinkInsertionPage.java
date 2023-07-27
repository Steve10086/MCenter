package com.astune.mcenter.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.FragmentLinkInsertionPageBinding;
import com.astune.mcenter.object.Room.MCenterDB;
import com.astune.mcenter.object.Room.WebLink;
import com.astune.mcenter.ui.customered.LinkFragment;
import com.astune.mcenter.utils.enums.LinkType;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Arrays;

public class LinkInsertionPage extends LinkFragment {

    private Activity parent;
    private FragmentLinkInsertionPageBinding layout;
    private InputMethodManager inputMethodManager;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private LinkInsertionPageViewModel viewModel;

    public LinkInsertionPage(){super();}

    public LinkInsertionPage(int touchX, int touchY) {
        super(touchX, touchY);
    }

    public static LinkInsertionPage newInstance() {
        return new LinkInsertionPage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        parent = getActivity();
        inputMethodManager = (InputMethodManager) parent.getSystemService(Activity.INPUT_METHOD_SERVICE);
        viewModel = new ViewModelProvider(this).get(LinkInsertionPageViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = FragmentLinkInsertionPageBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        parent.findViewById(R.id.exist_create_page_btn).setOnClickListener(c -> {//exist page without save
            this.getParentFragmentManager().popBackStack();
        });

        parent.findViewById(R.id.link_insertion_background).setOnClickListener(c -> {//hide keyboard when clicking outside the editText
            if (null != parent.getCurrentFocus())
                inputMethodManager.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken(), 0);
        });

        layout.linkSaveBtn.setOnClickListener(c -> {//saving event
            insertWebLink();
            getParentFragmentManager().popBackStack();
        });

        ArrayAdapter<String> linkType = new ArrayAdapter<>
                (
                getContext(),
                android.R.layout.simple_spinner_item,
                Arrays.stream(LinkType.getNames())
                        .filter(name -> !name.equals(LinkType.NEW_LINK.getName()))
                        .toArray(String[]::new)
                );

        layout.linkTypeSpinner.setAdapter(linkType);
    }

    @Override
    public void onPause(){
        super.onPause();
        disposable.clear();
    }

    private void insertWebLink(){
        layout.linkSaveBtn.setEnabled(false);
        if (!"".equals(layout.webLinkPort.getText().toString()) && !"".equals(layout.linkName.getText().toString())) {
            assert MCenterDB.Companion.getDB() != null;

            disposable.add(viewModel.saveEvent(
                    new WebLink(0,
                            getArguments().getInt("id"),
                            layout.linkName.getText().toString(),
                            layout.webLinkPort.getText().toString())
                            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {//finished event
                                layout.webLinkPort.setText("");
                                layout.linkName.setText("");
                                layout.linkSaveBtn.setEnabled(true);
                                Toast.makeText(parent.getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();}
                            , throwable -> Log.e("Room", throwable.getMessage())
                    )
            );

        } else {
            layout.linkSaveBtn.setEnabled(true);
            Toast.makeText(parent.getApplicationContext(), "empty name or port", Toast.LENGTH_SHORT).show();
        }
    }
}