package activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.lenovo.mypoetry.R;

import fragment.MyUploadRecordFragment;
import service.AudioService;
import zuo.biao.library.base.BaseActivity;

public class UserUploadActivity
        extends BaseActivity implements MyAudioService {
    private static final String TAG = "UserUploadActivity";

    private MyUploadRecordFragment myUploadRecordFragment;

    private AudioService audioService;
    private Intent intentPlay;
    private void initAudioService() {
        intentPlay = new Intent(getActivity(), AudioService.class);
        getActivity().bindService(intentPlay, conn, Context.BIND_AUTO_CREATE);
    }
    public AudioService getAudioService() {
        return audioService;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            audioService = ((AudioService.MyBinder) serviceBinder).getService();
            Log.i(TAG, "audioService" + audioService.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;
            Log.w(TAG, "audioService Disconnected");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initView();
        initData();
        initEvent();
        initAudioService();
    }

    @Override
    public void initView() {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        myUploadRecordFragment = new MyUploadRecordFragment();
        transaction.replace(R.id.myupload_fragment, myUploadRecordFragment);//
        transaction.commit();
    }

    @Override
    public void initData() {
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(conn);
        getActivity().stopService(intentPlay);
    }

}
