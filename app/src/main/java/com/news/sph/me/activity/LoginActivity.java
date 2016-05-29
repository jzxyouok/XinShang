package com.news.sph.me.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.sph.AppConfig;
import com.news.sph.AppContext;
import com.news.sph.R;
import com.news.sph.common.base.BaseActivity;
import com.news.sph.common.dto.BaseDTO;
import com.news.sph.common.entity.BaseEntity;
import com.news.sph.common.http.CallBack;
import com.news.sph.common.http.CommonApiClient;
import com.news.sph.common.utils.LogUtils;
import com.news.sph.common.utils.TimeCountDown;
import com.news.sph.me.MeUiGoto;
import com.news.sph.me.dto.LoginDTO;
import com.news.sph.me.entity.LoginEntity;
import com.news.sph.me.entity.LoginResult;
import com.news.sph.me.entity.User;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 登录主页面
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.img_btn_close)
    ImageView mImgBtnClose;
    @Bind(R.id.lg_imgbtn)
    ImageView mLgImgbtn;
    @Bind(R.id.login_et_num)
    EditText mLoginEtNum;
    @Bind(R.id.login_codeBtn)
    TextView mLoginCodeBtn;
    @Bind(R.id.login_pwdEt)
    EditText mLoginPwdEt;
    @Bind(R.id.setpwd_cb_agreement)
    CheckBox mSetpwdCbAgreement;
    @Bind(R.id.login_readProtocalBtn)
    TextView mLoginReadProtocalBtn;
    @Bind(R.id.login_loginBtn)
    Button mLoginLoginBtn;
    TimeCountDown mTcd;
    private String strPhoneNum;
    private String strPwd;
    private String mUrlAgreement;
    private String mAgreementTitle;

    User mUser;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        mUser=AppContext.getInstance().getUser();
        mSetpwdCbAgreement.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mLoginLoginBtn.setEnabled(true);
            } else {
                mLoginLoginBtn.setEnabled(false);
            }
        }
    };


    @Override
    public void initData() {
        mTcd = new TimeCountDown(mLoginCodeBtn, 60000, 1000, "重新获取", "s");

    }


    //获取验证码
    private void ObtainCode() {
        mTcd.start();
        BaseDTO bdto = new BaseDTO();
        bdto.setMembermob(strPhoneNum);
        bdto.setSign(AppConfig.SIGN_1);
        CommonApiClient.getVerify(this, bdto, new CallBack<BaseEntity>() {
            @Override
            public void onSuccess(BaseEntity result) {
                if (AppConfig.SUCCESS.equals(result.getStatus())) {
                    LogUtils.d("获取验证码请求成功");
                }
            }
        });
    }

    //登录
    private void Login() {
        LoginDTO ldto = new LoginDTO();
        ldto.setMemberverifycode(strPwd);
        ldto.setMembermob(strPhoneNum);
        ldto.setSign(AppConfig.SIGN_1);
        CommonApiClient.login(this, ldto, new CallBack<LoginResult>() {
            @Override
            public void onSuccess(LoginResult result) {
                if (AppConfig.SUCCESS.equals(result.getStatus())) {
                    LogUtils.e("登录成功");
                    getLoginResult(result.getData());
                    setResult(1001);
                    finish();
                }

            }
        });
    }

    private void getLoginResult(List<LoginEntity> data) {
        LoginEntity entity=data.get(0);
        mUser.setmUserMobile(entity.getMembermobile());
        AppContext.set("mobileNum",entity.getMembermobile());
        AppContext.set("isLogin",true);
        mUser.setmUserName(entity.getMembername());
        mUser.setmPictruePath(entity.getMemberHeadimg());
        mUser.setFlag(true);
    }


    @OnClick({R.id.img_btn_close, R.id.lg_imgbtn, R.id.login_codeBtn, R.id.login_readProtocalBtn, R.id.login_loginBtn})
    public void onClick(View view) {
        strPhoneNum = mLoginEtNum.getText().toString();
        strPwd = mLoginPwdEt.getText().toString();
        switch (view.getId()) {
            case R.id.img_btn_close:
                LoginActivity.this.finish();
                break;
            case R.id.lg_imgbtn:
                break;
            case R.id.login_codeBtn:
                ObtainCode();
                break;
            case R.id.login_readProtocalBtn:
                mUrlAgreement = AppConfig.URL_AGREEMENT;
                mAgreementTitle = "用户注册协议";
                MeUiGoto.registration(this, mUrlAgreement, mAgreementTitle);//用户注册协议
                break;
            case R.id.login_loginBtn:
                Login();
                break;
        }
    }
}
