package activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import application.MyApplication;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.UserSession;
import utils.InputParamUtil;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.BottomMenuView;
import zuo.biao.library.util.JSON;


public class LoginActivity extends BaseActivity implements View.OnClickListener,
        OnBottomDragListener, BottomMenuView.OnBottomMenuItemClickListener,
        OnHttpResponseListener {

    private LoginActivity context;

    private MaterialEditText edt_phoneNum;
    private MaterialEditText edt_password;
    private Button bt_login;
    private TextView bt_register;
    private ProgressDialog waitingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        context = this;

        intent = getIntent();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDragBottom(boolean rightToLeft) {

    }

    @Override
    public void initView() {
        edt_phoneNum = findViewById(R.id.edt_phone_num);
        edt_password = findViewById(R.id.edt_password);
        bt_login = findViewById(R.id.btn_login);
        bt_register = findViewById(R.id.btn_register);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        edt_phoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }
                //失去焦点
                String text = ((EditText) v).getText().toString();
                if (InputParamUtil.noInput(text)) {
                    showShortToast("请输入手机号");
                    return;
                }
                if (InputParamUtil.isValid(text) == false) {
                    showShortToast(R.string.phone_num_novalid);
                }
            }
        });
        edt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {    //失去焦点
                    String text = ((EditText) v).getText().toString();
                    if (InputParamUtil.noInput(text)) {
                        showShortToast("请输入密码");
                    }
                    if (false == InputParamUtil.isValidPassword(text)) {
                        showShortToast(R.string.password_novalid);
                    }
                }
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = edt_phoneNum.getText().toString();
                if (InputParamUtil.isValid(phoneNum)) {
                    String password = edt_password.getText().toString();
                    if (InputParamUtil.isValidPassword(password)) {

                        waitingDialog = new ProgressDialog(LoginActivity.this);
                        waitingDialog.setTitle("登录中");
                        waitingDialog.setMessage("请稍候...");
                        waitingDialog.setIndeterminate(true);
                        waitingDialog.setCancelable(false);//不可取消
                        waitingDialog.show();

                        // TODO login
                        RequestDataUtil.doLogin(phoneNum, password, new OnHttpResponseListenerImpl(context));
                    } else {
                        showShortToast(R.string.password_novalid);
                    }
                } else {
                    showShortToast(R.string.phone_num_novalid);
                }
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                toActivity(intent);
            }
        });
    }

    @Override
    public void onBottomMenuItemClick(int intentCode) {

    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        waitingDialog.cancel();

        if (resultData == null) {
            showShortToast(resultMsg);
            return;
        }

        UserSession userSession = JSON.parseObject(resultData, UserSession.class);
        if (userSession == null || userSession.checkCorrect() == false) {
            showShortToast(resultMsg);
            return;
        }

        MyApplication.getInstance().setUser(userSession);

        //登录成功
        Intent intent = new Intent();
        setResult(RESULT_OK, intent); //此处的intent可以用A传过来intent，或者使用新的intent
        finish();
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {
        waitingDialog.cancel();
        showShortToast(e.getMessage());
    }
}
