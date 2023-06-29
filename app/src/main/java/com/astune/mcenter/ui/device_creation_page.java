package com.astune.mcenter.ui;

import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astune.mcenter.R;
import com.astune.mcenter.object.HookedFragment;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Room.MCenterDB;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.security.PrivilegedAction;

public class device_creation_page extends HookedFragment {

    private DeviceCreationPageViewModel mViewModel;
    private EditText ip;

    private EditText name;
    private InputMethodManager inputMethodManager;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public device_creation_page(Hook[] hooks) {
        super(hooks);
    }

    public device_creation_page() {}

    public static device_creation_page newInstance() {
        return new device_creation_page(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeviceCreationPageViewModel.class);
        inputMethodManager = (InputMethodManager) parent.getSystemService(Activity.INPUT_METHOD_SERVICE);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_creation_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button saveBtn = parent.findViewById(R.id.newdevice_save_btn);

        ip = parent.findViewById(R.id.device_ip);
        name = parent.findViewById(R.id.device_name);

        parent.findViewById(R.id.exist_create_page_btn).setOnClickListener(c -> {//exist page without save
            this.getParentFragmentManager().popBackStack();
        });

        parent.findViewById(R.id.device_creation_background).setOnClickListener(c -> {//hide keyboard when clicking outside the editText
            if (null != parent.getCurrentFocus())
                inputMethodManager.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken(), 0);
        });

        saveBtn.setOnClickListener(c -> {//saving event
            saveBtn.setEnabled(false);
            Log.i("text", ip.getText().toString());
            if (!"".equals(ip.getText().toString()) && !"".equals(name.getText().toString())) {
                assert MCenterDB.Companion.getDB() != null;

                disposable.add(MCenterDB.Companion.getDB().deviceDao().insert(new Device(0, name.getText().toString(), ip.getText().toString(), null))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {//finished event
                            ip.setText("");
                            name.setText("");
                            saveBtn.setEnabled(true);
                            Toast.makeText(parent.getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();}
                                , throwable -> Log.e("Room", throwable.getMessage())
                        )
                );

            } else {
                Toast.makeText(parent.getApplicationContext(), "empty name or ip", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onPause(){
        super.onPause();
        disposable.clear();
    }
}