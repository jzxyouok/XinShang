package com.news.sph.issue.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.sph.AppConfig;
import com.news.sph.R;
import com.news.sph.common.base.BaseTitleActivity;
import com.news.sph.common.http.CallBack;
import com.news.sph.common.http.CommonApiClient;
import com.news.sph.common.utils.ImageLoaderUtils;
import com.news.sph.common.utils.LogUtils;
import com.news.sph.issue.IssueUiGoto;
import com.news.sph.issue.dto.SettlementDTO;
import com.news.sph.issue.entity.SettlementResult;

import butterknife.Bind;

/**
 * 结算主页面
 */
public class SettlementActivity extends BaseTitleActivity {
    @Bind(R.id.set_ed)
    EditText mInBtn;
    @Bind(R.id.set_cb_wx)
    CheckBox mSetWx;
    @Bind(R.id.set_cb_zhi)
    CheckBox mSetZhi;
    @Bind(R.id.set_cb_hui)
    CheckBox mSetHui;
    @Bind(R.id.set_cb_agree)
    CheckBox mSetAgree;
    @Bind(R.id.set_tv)
    TextView mSetTv;
    @Bind(R.id.settle_title)
    TextView mTitle;
    @Bind(R.id.settle_term)
    TextView mTerm;
    @Bind(R.id.settle_img)
    ImageView mImg;
    @Bind(R.id.place_tv_nm)
    TextView mPlaceNm;
    @Bind(R.id.set_pay_Btn)
    Button mPayBtn;
    @Bind(R.id.palce_pay_wx)
    LinearLayout mPayWx;
    @Bind(R.id.palce_pay_alipay)
    LinearLayout mPayAlipay;
    @Bind(R.id.palce_pay_balance)
    LinearLayout mPayBalance;
    String mCaTerm;
    String mCaTitle;
    String mCaNum;

    @Override
    protected int getContentResId() {
        return R.layout.activity_settlement;
    }

    @Override
    public void initView() {
        setTitleText("结算");
        mCaTerm = getIntent().getBundleExtra("bundle").getString("mCaTerm");
        mCaTitle = getIntent().getBundleExtra("bundle").getString("mCaTitle");
        mCaNum = getIntent().getBundleExtra("bundle").getString("mCaNum");
        mTitle.setText(mCaTitle);
        mTerm.setText(mCaTerm);
        ImageLoaderUtils.displayImage(mCaNum,mImg);
        mSetAgree.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mPayBtn.setEnabled(true);
            } else {
                mPayBtn.setEnabled(false);
            }
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_tv:
                IssueUiGoto.special(this,AppConfig.Server_Html+"");
                break;
            case R.id.set_pay_Btn:
                settlement();//下单并开奖
                break;
            case R.id.palce_pay_wx:
                mSetWx.setChecked(true);
                mSetZhi.setChecked(false);
                mSetHui.setChecked(false);
                break;
            case R.id.palce_pay_alipay:
                mSetWx.setChecked(false);
                mSetZhi.setChecked(true);
                mSetHui.setChecked(false);
                break;
            case R.id.palce_pay_balance:
                mSetWx.setChecked(false);
                mSetZhi.setChecked(false);
                mSetHui.setChecked(true);
                break;
        }
        super.onClick(v);
    }

    private void settlement() {
        SettlementDTO gdto=new SettlementDTO();
        if(mSetWx.isChecked()){
            gdto.setType("2");
        }

        if(mSetZhi.isChecked()){
            gdto.setType("1");
        }
        if(mSetHui.isChecked()){
            gdto.setType("3");
        }

        gdto.setUserPhone("");
        gdto.setInfor_phone("");
        gdto.setBat_code("");
        gdto.setSna_code("");
        gdto.setRec_participate_count("");
        gdto.setBalance("");
        gdto.setSna_total_count("");
        gdto.setTerm("");
        gdto.setRec_code("");
        gdto.setSign(AppConfig.SIGN_1);
        CommonApiClient.settlement(this, gdto, new CallBack<SettlementResult>() {
            @Override
            public void onSuccess(SettlementResult result) {
                if(AppConfig.SUCCESS.equals(result.getStatus())){
                    LogUtils.e("结算成功");
                }

            }
        });

    }
}
