package activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.mypoetry.R;

import java.util.regex.Pattern;

import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.User;
import utils.EncryptUtil;
import utils.MyHttpRequestUtil;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.BottomMenuView;
import zuo.biao.library.util.JSON;


public class LoginActivity extends BaseActivity implements View.OnClickListener,
        OnBottomDragListener, BottomMenuView.OnBottomMenuItemClickListener,
        OnHttpResponseListener {

    private LoginActivity context;

    private static final String REGEX_PHONE_NUM = "[1]\\d{10}";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";//6-20位字母+数字
    private EditText edt_phoneNum, edt_password;
    private Button bt_login, bt_register;
    private String nullStr = "";
    private User user;

    private ProgressDialog waitingDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            waitingDialog.cancel();
            if (user != null) { //登录成功
                //(loginRes);
                Intent intent = new Intent();
                intent.putExtra("user", JSON.toJSONString(user));//TODO
                setResult(RESULT_OK, intent); //此处的intent可以用A传过来intent，或者使用新的intent
                finish();
            } else {
                toast(R.string.login_fail);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        context = this;

        intent = getIntent();
//        userId = intent.getLongExtra(INTENT_ID, userId);
//        if (userId <= 0) {
//            finishWithError("用户不存在！");
//            return;
//        }

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }


    public boolean isValid(String phoneNum) {
        if (phoneNum == null || nullStr.equals(phoneNum)) {
            return false;
        }
        return Pattern.matches(REGEX_PHONE_NUM, phoneNum);
    }

    public boolean isValidPassword(String password) {
        if (password == null || nullStr.equals(password)) {
            return false;
        }
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    private void toast(int strId) {
        Toast.makeText(LoginActivity.this, strId, Toast.LENGTH_SHORT).show();
    }

    private void toast(String str) {
        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDragBottom(boolean rightToLeft) {

    }

    @Override
    public void initView() {
        edt_phoneNum = (EditText) findViewById(R.id.edt_phone_num);
        edt_password = (EditText) findViewById(R.id.edt_password);
        bt_login = (Button) findViewById(R.id.btn_login);
        bt_register = (Button) findViewById(R.id.btn_register);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        edt_phoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {    //失去焦点
                    String text = ((EditText) v).getText().toString();
                    if (!nullStr.equals(text) && !isValid(text)) {
                        toast(R.string.phone_num_novalid);
                    }
                }
            }
        });
        edt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {    //失去焦点
                    String text = ((EditText) v).getText().toString();
                    if (!nullStr.equals(text) && !isValidPassword(text)) {
                        toast(R.string.password_novalid);
                    }
                }
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = edt_phoneNum.getText().toString();
                if (isValid(phoneNum)) {
                    String password = edt_password.getText().toString();
                    if (isValidPassword(password)) {
                        password = EncryptUtil.encrypt(password);
                        waitingDialog = new ProgressDialog(LoginActivity.this);
                        waitingDialog.setTitle("登录中");
                        waitingDialog.setMessage("请稍候...");
                        waitingDialog.setIndeterminate(true);
                        waitingDialog.setCancelable(false);//不可取消
                        waitingDialog.show();

                        // TODO login
                        MyHttpRequestUtil.login(phoneNum, password, 0, new OnHttpResponseListenerImpl(context));
                    } else {
                        toast(R.string.password_novalid);
                    }
                } else {
                    toast(R.string.phone_num_novalid);
                }
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBottomMenuItemClick(int intentCode) {

    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        if (requestCode != resultCode) {
            // 提示错误信息
        }
        user = (JSON.parseObject(resultData, User.class));
        if (user != null) {
            handler.sendEmptyMessage(0);
            return;
        }
        toast(R.string.login_fail);
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {

    }
}
