package activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.mypoetry.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.UserSession;
import utils.Constant;
import utils.InputParamUtil;
import utils.ServerUrlUtil;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.JSON;

public class SignUpActivity extends BaseActivity implements OnHttpResponseListener,
        View.OnClickListener {
    // 短信等待时间 倒计时60s
    private static final int INIT_WAIT_TIME = 60;

    private SignUpActivity context;

    private TimerTask tt;
    private Timer tm;

    private EditText et_username;
    private EditText et_phonenum;
    private Button btn_check;
    private EditText et_checkecode;
    private Button btn_sure;
    private EditText passwrod;
    private EditText re_password;
    private Button do_register;

    private boolean phoneNumCheck = false;
    private boolean passwrodCheck = false;
    private int waitTime = INIT_WAIT_TIME;//倒计时60s
    public String country = "86";//这是中国区号 可以使用getSupportedCountries();获得国家区号
    private String phone;
    private String passwordStr;
    private ProgressDialog waitingDialog;
    private static final int CODE_REPEAT = 1; //重新发送

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                btn_check.setEnabled(true);
                btn_sure.setEnabled(true);
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                waitTime = INIT_WAIT_TIME;//时间重置
                btn_check.setText("重新获取验证码");
            } else if (msg.what == 10) {
                btn_sure.setEnabled(true);
            } else {
                btn_check.setText(waitTime + "重新获取验证码");
            }
        }
    };

    //短信回调
    EventHandler smsEventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    phoneNumCheck = true;
                    showShortToast("验证成功");
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功
                    showShortToast("请求验证码成功，请稍候");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//获取国家区号类表回调
                }
            } else {//错误（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考
                ((Throwable) data).printStackTrace();
                handler.sendEmptyMessage(10);
                showShortToast("验证码错误");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        setContentView(R.layout.user_register);

        et_username = findViewById(R.id.et_username);
        et_phonenum = findViewById(R.id.et_phonenum);
        btn_check = findViewById(R.id.btn_check);
        et_checkecode = findViewById(R.id.et_checkecode);
        btn_sure = findViewById(R.id.btn_sure);
        passwrod = findViewById(R.id.edt_password);
        re_password = findViewById(R.id.edt_re_password);
        do_register = findViewById(R.id.btn_do_register);
    }

    @Override
    public void initData() {
        context = this;
        initSms();
    }

    public void initSms() {
        SMSSDK.initSDK(this, Constant.APP_KEY, Constant.APP_SECRET);
        SMSSDK.registerEventHandler(smsEventHandler); //注册短信回调（记得销毁，避免泄露内存）
    }

    @Override
    public void initEvent() {
        btn_check.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        do_register.setOnClickListener(this);

        passwrod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }
                // 失去焦点
                String text = ((EditText) v).getText().toString();
                if (InputParamUtil.noInput(text)) {
                    showShortToast("请输入密码");
                    return;
                }
                if (InputParamUtil.isValidPassword(text) == false) {
                    showShortToast(R.string.password_novalid);
                } else {
                    passwordStr = text;
                }
            }
        });

        re_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {    //失去焦点
                    checkPassword();
                }
            }
        });
    }

    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(smsEventHandler);
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

        ServerUrlUtil.setUser(userSession);

        // 注册成功，自动登录成功
        Intent intent = new Intent();
        setResult(RESULT_OK, intent); //此处的intent可以用A传过来intent，或者使用新的intent
        finish();
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {
        waitingDialog.cancel();
        showShortToast(e.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check:
                phone = et_phonenum.getText().toString().trim().replaceAll("/s", "");
                if (InputParamUtil.noInput(phone)) {
                    showShortToast("请先输入手机号");
                    break;
                }
                if (InputParamUtil.isValid(phone) == false) {
                    showShortToast("手机号格式错误");
                    break;
                }
                alterWarning();
                break;
            case R.id.btn_sure:
                String code = et_checkecode.getText().toString().replaceAll("/s", "");
                if (!TextUtils.isEmpty(code) && code.length() == 4) {//判断验证码是否为空
                    btn_sure.setEnabled(false);
                    SMSSDK.submitVerificationCode(country, phone, code);//验证
                } else {//如果用户输入的内容为空，提醒用户
                    showShortToast("验证码格式错误，请输入四位数字验证码！");
                }
                break;
            case R.id.btn_do_register:
                if (phoneNumCheck == false) {
                    showShortToast("请完成手机短息验证！");
                    break;
                }

                String text = et_username.getText().toString().trim();
                if (InputParamUtil.noInput(text)) {
                    showShortToast("您未填写昵称！");
                    break;
                }
                // 防止 由于点击按钮时密码框没有失去焦点，导致passwrodCheck == false
                checkPassword();
                if (passwrodCheck) {
                    doRegister(text, phone, passwordStr);
                } else {
                    showShortToast("两次密码不一致！");
                }
                break;
        }
    }

    private void checkPassword() {
        String text = re_password.getText().toString();

        if (InputParamUtil.noInput(text)) {
            passwrodCheck = false;
            showShortToast("请输入重复确认密码");
            return;
        }

        if (InputParamUtil.isValidPassword(text) == false) {
            showShortToast(R.string.password_novalid);
        }

        passwrodCheck = passwordStr.equals(text);
        if (passwrodCheck == false) {
            showShortToast("密码不一致！");
        }
    }

    private void doRegister(String text, String phone, String pswd) {
        waitingDialog = new ProgressDialog(this);
        waitingDialog.setTitle("注册中");
        waitingDialog.setMessage("请稍候...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);//不可取消
        waitingDialog.show();

        ServerUrlUtil.doRegister(text, phone, pswd, new OnHttpResponseListenerImpl(context));
    }

    // 弹窗确认下发
    private void alterWarning() {
        // 通过sdk发送短信验证
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("将会发送验证码到" + phone); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 关闭dialog
                // 通过sdk发送短信验证（请求获取短信验证码，在监听（smsEventHandler）中返回）
                SMSSDK.getVerificationCode(country, phone);
                phoneNumCheck = false;
                // 做倒计时操作
                Toast.makeText(SignUpActivity.this, "请稍候", Toast.LENGTH_SHORT).show();
                btn_check.setEnabled(false);
                btn_sure.setEnabled(true);
                tm = new Timer();
                tt = new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(waitTime--);
                    }
                };
                tm.schedule(tt, 0, 1000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(SignUpActivity.this, "已取消", Toast.LENGTH_SHORT).show();
            }
        });
        // 参数都设置完成了，创建并显示出来
        builder.create().show();
    }
}
