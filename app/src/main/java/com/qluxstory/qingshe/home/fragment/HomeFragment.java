package com.qluxstory.qingshe.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bigkoo.pickerview.OptionsPopupWindow;
import com.qluxstory.ptrrecyclerview.BaseRecyclerAdapter;
import com.qluxstory.ptrrecyclerview.BaseRecyclerViewHolder;
import com.qluxstory.ptrrecyclerview.BaseSimpleRecyclerAdapter;
import com.qluxstory.qingshe.AppConfig;
import com.qluxstory.qingshe.MainActivity;
import com.qluxstory.qingshe.R;
import com.qluxstory.qingshe.common.base.BasePullScrollViewFragment;
import com.qluxstory.qingshe.common.base.SimplePage;
import com.qluxstory.qingshe.common.bean.ViewFlowBean;
import com.qluxstory.qingshe.common.dto.BaseDTO;
import com.qluxstory.qingshe.common.http.CallBack;
import com.qluxstory.qingshe.common.http.CommonApiClient;
import com.qluxstory.qingshe.common.utils.ImageLoaderUtils;
import com.qluxstory.qingshe.common.utils.LogUtils;
import com.qluxstory.qingshe.common.utils.UIHelper;
import com.qluxstory.qingshe.common.widget.FullyLinearLayoutManager;
import com.qluxstory.qingshe.common.widget.ViewFlowLayout;
import com.qluxstory.qingshe.home.HomeUiGoto;
import com.qluxstory.qingshe.home.adapter.HorizontalScrollViewAdapter;
import com.qluxstory.qingshe.home.entity.HomeAdcerEntity;
import com.qluxstory.qingshe.home.entity.HomeAdcerResult;
import com.qluxstory.qingshe.home.entity.HomeRecomendResult;
import com.qluxstory.qingshe.home.entity.HomeRecommendEntity;
import com.qluxstory.qingshe.home.entity.HomeSpecialEntity;
import com.qluxstory.qingshe.home.entity.HomeSpecialResult;
import com.qluxstory.qingshe.home.widget.MyHorizontalScrollView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 首页的fragment
 */
public class HomeFragment extends BasePullScrollViewFragment {
    @Bind(R.id.vf_layout)
    ViewFlowLayout mVfLayout;
    @Bind(R.id.hsv)
    MyHorizontalScrollView mHsv;
    @Bind(R.id.special_list)
    RecyclerView mSpecialList;
    @Bind(R.id.id_gallery)
    LinearLayout mIdGallery;

    HorizontalScrollViewAdapter mHsvAdapter;
    BaseSimpleRecyclerAdapter mSpecialListAdapter;
    int reqCount;
    @Bind(R.id.img_lf)
    ImageView mImgLf;
    @Bind(R.id.img_rg)
    ImageView mImgRg;
    @Bind(R.id.home_img1)
    ImageView mImgOrder;
    @Bind(R.id.home_img2)
    ImageView mImgNew;
    @Bind(R.id.home_img3)
    ImageView mImgHelp;
    @Bind(R.id.home_img_dian)
    ImageView mHomeImgDian;
    List<HomeRecommendEntity> entity;


    @Override
    public void initView(View view) {
        super.initView(view);
        mHomeImgDian.setOnClickListener(this);
        mVfLayout.setOnItemClickListener(new ViewFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeUiGoto.special(getActivity(),AppConfig.URL_SPECIAL);

            }
        });
        mIdGallery.setOnClickListener(this);
        mHsvAdapter = new HorizontalScrollViewAdapter(getActivity());
        mImgOrder.setOnClickListener(this);
        mImgNew.setOnClickListener(this);
        mImgHelp.setOnClickListener(this);
        mImgLf.setOnClickListener(this);
        mImgRg.setOnClickListener(this);

        mSpecialList.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        mSpecialListAdapter=new BaseSimpleRecyclerAdapter<HomeSpecialEntity>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_special;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, HomeSpecialEntity homeSpecialEntity, int position) {
                ImageView iv=holder.getView(R.id.iv);
                ImageLoaderUtils.displayImage(homeSpecialEntity.getSpec_pic(),iv);
            }


        };
        mSpecialList.setAdapter(mSpecialListAdapter);
        mSpecialListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View itemView, Object itemBean, int position) {
                HomeUiGoto.special(getActivity(),AppConfig.URL_SPECIAL);
            }
        });
    }

    private void reqAdver() {
        BaseDTO dto = new BaseDTO();
        CommonApiClient.homeAdcer(this, dto, new CallBack<HomeAdcerResult>() {
            @Override
            public void onSuccess(HomeAdcerResult result) {
                checkPullRefreshComplete();
                if (AppConfig.SUCCESS.equals(result.getStatus())) {
                    LogUtils.e("首页广告成功");
                    List<HomeAdcerEntity> mAdDatas = result.getData();
                    if (mAdDatas != null && mAdDatas.size() != 0) {
                        ArrayList<ViewFlowBean> list = new ArrayList<>();
                        for (int i = 0; i < mAdDatas.size(); i++) {
                            ViewFlowBean bean = new ViewFlowBean();
                            bean.setImgUrl(AppConfig.BASE_URL + mAdDatas.get(i).getSpecPic());
                            list.add(bean);
                        }
                        mVfLayout.updateView(list);
                    }
                }
            }
        });
    }

    public void checkPullRefreshComplete() {
        reqCount++;
        if (reqCount == 3) {
            reqCount=0;
            refreshComplete();
        }
    }



    private void reqRecommend() {
        BaseDTO dto = new BaseDTO();
        CommonApiClient.homeRecommend(this, dto, new CallBack<HomeRecomendResult>() {
            @Override
            public void onSuccess(HomeRecomendResult result) {
                checkPullRefreshComplete();
                if (AppConfig.SUCCESS.equals(result.getStatus())) {
                    LogUtils.e("首页推荐成功");
                    entity = result.getData();
                    if(null==entity){
                        return;
                    }else {
                        mHsvAdapter.append(result.getData());
                        mHsv.initDatas(mHsvAdapter);
                    }


                }

            }
        });
    }


    private void reqSpecial() {
        BaseDTO dto = new BaseDTO();
        CommonApiClient.homeSpecial(this, dto, new CallBack<HomeSpecialResult>() {
            @Override
            public void onSuccess(HomeSpecialResult result) {
                checkPullRefreshComplete();
                if (AppConfig.SUCCESS.equals(result.getStatus())) {
                    LogUtils.e("首页专题成功");
                    mSpecialListAdapter.removeAll();
                    mSpecialListAdapter.append(result.getData());
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_lf:
                mHsv.smoothScrollBy(-mHsv.getScroolw(),0);
                break;
            case R.id.img_rg:
                mHsv.smoothScrollBy(mHsv.getScroolw(),0);
                break;
            case R.id.home_img1:
                HomeUiGoto.curing(getActivity());//专业养护
                break;
            case R.id.home_img2:
                HomeUiGoto.testOne(getActivity());//测试
                break;
            case R.id.home_img3:
                showPop();

//                HomeUiGoto.help(getActivity(),AppConfig.URL_TRANSACTION,"交易帮助 - 倾奢");
                break;
            case R.id.id_gallery:
                Bundle b = new Bundle();
                b.putSerializable("entity", (Serializable) entity);
                UIHelper.showFragment(getActivity(), SimplePage.PRODUCT_DETAILS,b);
                break;
            case R.id.home_img_dian:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("tag",1);
                getActivity().startActivity(intent);
                break;
        }
        super.onClick(v);
    }

    private ArrayList<String> tipList;
    private void showPop() {
        tipList = new ArrayList<>();
        tipList.add("nan");
        tipList.add("nv");
        OptionsPopupWindow tipPopup = new OptionsPopupWindow(getActivity());
        tipPopup.setPicker(tipList);//设置里面list
        tipPopup.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {//确定的点击监听
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if ("无".equals(tipList.get(options1))) {
                } else {
                }

            }
        });
        tipPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置窗体消失后，屏幕恢复亮度
            @Override
            public void onDismiss() {
                closePopupWindow();
            }
        });
        tipPopup.showAtLocation(mHsv, Gravity.BOTTOM, 0, 0);//显示的位置
        //弹窗后背景变暗
        openPopupWindow();
    }





    /**
     *  打开窗口 
     */
    private void openPopupWindow() {
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.7f;
        getActivity().getWindow().setAttributes(params);
    }

    /**
     *  关闭窗口 
     */
    private void closePopupWindow() {
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 1f;
        getActivity().getWindow().setAttributes(params);
    }


    @Override
    public void initData() {
        sendRequestData();
    }

    @Override
    protected void sendRequestData() {
        reqAdver();//首页广告
        reqRecommend();//首页推荐
        reqSpecial();//首页专题列表

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }


    @Override
    public boolean pulltoRefresh() {
        return true;
    }


}
