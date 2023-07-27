package com.astune.mcenter.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.ActivityMainBinding;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Room.MCenterDB;
import com.astune.mcenter.utils.PopupMenuUtil;
import com.astune.mcenter.utils.enums.ActivityState;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;

    protected FragmentManager pageManager;
    private Fragment informationPage;

    private ActivityMainBinding layout;

    private int touchX = 0;
    private int touchY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        layout = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(layout.getRoot());

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel.class);

        viewModel.refreshDeviceList();

        pageManager = getSupportFragmentManager();

        viewModel.deviceList.observe(this, devices -> {
            layout.cardList.setCard(devices);
            Log.i("Room", devices.toString());
        });

        layout.mainTitleBar.createBtn.setOnClickListener(c->{
            try {
                createBtnClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        //information_page btn
        layout.mainTitleBar.userInfoBtn.setOnClickListener(c -> {
            try {
                infoPageClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        });

        layout.cardList.setOnclickListener((device) ->{
            Log.i("CardList", device.toString() + "clicked");
            try {
                deviceCardClicked(device);
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return null;
        });

        layout.informationContainer.setOnTouchListener((view, motionEvent) -> {
            touchX = (int) motionEvent.getX();
            touchY = (int) motionEvent.getY();
            return false;
        });

        layout.cardList.setOnLongClickListener((Function1<Device, Unit>) device -> {
            PopupMenu menu = new PopupMenu(this, layout.mainTitleBar.title);
            menu.getMenuInflater().inflate(R.menu.card_list_popup_menu, menu.getMenu());
            PopupMenuUtil.showMenuOnPosition(menu, touchX, touchY);
            menu.setOnMenuItemClickListener(item ->
            {
                Log.i("CardList", item.getTitle().toString().equals("delete") ?  "delete" : "edit");
                if (item.getTitle().toString().equals(getResources().getString(R.string.delete))){
                    viewModel.deleteDevice(device);
                }
                return false;
            });
            return null;
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        viewModel.updateOnline();
    }

    @Override
    public void onPause(){
        super.onPause();
        viewModel.finish();
    }

    public void deviceCardClicked(Device device) throws NoSuchMethodException, ClassNotFoundException {
        viewModel.title.setValue(device.getName());
        Fragment linkPage = new LinkPage(
                new Hook[]{
                        new Hook(this.getClass().getDeclaredMethod("linkPageExisted"),
                                this,
                                ActivityState.ON_PAUSE),
                        new Hook(this.getClass().getDeclaredMethod("linkPageEntered"),
                                this,
                                ActivityState.ON_START)
                }
        );
        Bundle bundle = new Bundle(device.toBundle());
        bundle.putString("viewModelClass", LinkPageViewModel.class.getName());
        linkPage.setArguments(bundle);
        FragmentTransaction transaction = pageManager.beginTransaction();
        transaction.replace(R.id.information_container, linkPage);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //add information page into main stage
    public void infoPageClicked() throws NoSuchMethodException {
        Log.i("Main", "infoClicked");
        if (informationPage == null){
            informationPage = new InformationPage(
                    new Hook[]{
                            new Hook(this.getClass().getDeclaredMethod("infoPageExisted"),
                                    this,
                                    ActivityState.ON_PAUSE),
                            new Hook(this.getClass().getDeclaredMethod("infoPageEntered"),
                                    this,
                                    ActivityState.ON_START)
                    }
                    );
            Bundle bundle = new Bundle();
            bundle.putString("viewModelClass", InformationPageViewModel.class.getName());
            informationPage.setArguments(bundle);
        }
        FragmentTransaction transaction = pageManager.beginTransaction();
        transaction.replace(R.id.information_container, informationPage);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createBtnClicked() throws NoSuchMethodException {
        AlertDialog.Builder passwordDialog = new AlertDialog.Builder(this);
        View view = View.inflate(passwordDialog.getContext(), R.layout.device_creation_dialog, null);
        passwordDialog
                .setPositiveButton("save", (dialogInterface, i) -> {

                    String name = ((EditText)view.findViewById(R.id.name_editor)).getText().toString();
                    String address = ((EditText)view.findViewById(R.id.address_editor)).getText().toString();

                    if (!"".equals(name) && !"".equals(address))
                    {
                        assert MCenterDB.Companion.getDB() != null;

                        viewModel.insertDevice(new Device(0, name, address, null), getApplicationContext());
                    } else {
                        Toast.makeText(getApplicationContext(), "empty name or ip", Toast.LENGTH_SHORT).show();
                    }
                })
                .setTitle("New Device")
                .setView(view)
                .create().show();
    }

    public void linkPageEntered(){
        Log.i("MainAct", "Info entered");
        layout.mainTitleBar.userInfoBtn.setVisibility(View.INVISIBLE);
        layout.mainTitleBar.createBtn.setVisibility(View.INVISIBLE);
        layout.cardList.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_list_slide_out));
    }

    public void linkPageExisted(){
        Log.i("MainAct", "Link Existed");

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.card_list_slide_in);
        animation.setInterpolator(new DecelerateInterpolator());
        onAnimationEnd(animation);

        layout.cardList.startAnimation(animation);
    }

    public void infoPageEntered(){
        Log.i("MainAct", "Info entered");
        layout.mainTitleBar.userInfoBtn.setVisibility(View.INVISIBLE);
        layout.mainTitleBar.createBtn.setVisibility(View.INVISIBLE);
        layout.cardList.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_list_slide_half_out));
    }

    //info Page exist animation, hooked into onPause()
    public void infoPageExisted() {
        Log.i("MainAct", "Info existed");
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.card_list_slide_half_in);
        animation.setInterpolator(new DecelerateInterpolator());
        onAnimationEnd(animation);

        layout.cardList.startAnimation(animation);
    }

    // set button visible after the animation ends, preventing bug with the fragment transaction
    private void onAnimationEnd(Animation animation){
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.mainTitleBar.userInfoBtn.setVisibility(View.VISIBLE);
                layout.mainTitleBar.createBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}