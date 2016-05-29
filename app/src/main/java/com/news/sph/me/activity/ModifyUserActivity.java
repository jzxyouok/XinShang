package com.news.sph.me.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.news.sph.AppConfig;
import com.news.sph.AppContext;
import com.news.sph.R;
import com.news.sph.common.base.BaseTitleActivity;
import com.news.sph.common.http.CallBack;
import com.news.sph.common.http.CommonApiClient;
import com.news.sph.common.utils.LogUtils;
import com.news.sph.me.dto.ModifyDTO;
import com.news.sph.me.entity.ModifyResult;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改用户信息主页面
 */
public class ModifyUserActivity extends BaseTitleActivity {
    @Bind(R.id.modify_et)
    EditText mModifyEt;
    @Bind(R.id.modify_Btn)
    Button mModifyBtn;
    @Bind(R.id.modify_img)
    ImageView mModifyImg;
    private String mNewName, mMembername,mMobile;


    @Override
    protected int getContentResId() {
        return R.layout.activity_me_modifyuser;
    }

    @Override
    public void initView() {
        setTitleText("修改用户名");
        mMembername = AppContext.getInstance().getUser().getmUserName();
        mMobile = AppContext.getInstance().getUser().getmUserMobile();
        mModifyEt.setText(mMembername);
        mModifyImg.setOnClickListener(this);
        mModifyBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }


    private void modify() {
        mNewName = mModifyEt.getText().toString();
        ModifyDTO mdto = new ModifyDTO();
        mdto.setMembername(mNewName);
        mdto.setMembermob(mMobile);
        mdto.setSign(AppConfig.SIGN_1);
        CommonApiClient.modifyUser(this, mdto, new CallBack<ModifyResult>() {
            @Override
            public void onSuccess(ModifyResult result) {
                if (AppConfig.SUCCESS.equals(result.getStatus())) {
                    LogUtils.e("修改用户名成功");
                    AppContext.getInstance().getUser().setmUserName(result.getData().get(0).getMembername());
                    finish();

                }

            }
        });
    }



    @OnClick(R.id.modify_Btn)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_Btn:
                modify();//修改用户名
                break;
            case R.id.modify_img:
                mModifyEt.setText("");
                break;
            default:
                break;
        }
        super.onClick(v);
    }
}
