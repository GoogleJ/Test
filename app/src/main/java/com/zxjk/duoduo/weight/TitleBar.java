package com.zxjk.duoduo.weight;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxjk.duoduo.R;


/**
 * @author Administrator
 * @// TODO: 2019\3\18 0018 标题布局自定义 
 */
public class TitleBar extends LinearLayout {
    ImageView mLeftImage;
    RelativeLayout mLeftLayout;

    ImageView mRightImage;
    RelativeLayout mRightLayout;


    LinearLayout mRoot;
    TextView mRightText;
    TextView mLeftText;
    TextView mTitle;
    TextView mSonTitle;


    RelativeLayout mCenterLayout;

    ImageView mCenterImage;


    View mDividerLine;




    private static final String TAG = "TitleBar";

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        View inflate = LayoutInflater.from(context)
                .inflate(R.layout.view_title_bar, this);
        initView(inflate);
        parseStyle(context, attrs);
        //设置字体加粗
        TextPaint paint = mTitle.getPaint();
        paint.setFakeBoldText(true);
    }

    private void initView(View v) {
        mLeftImage =  v.findViewById(R.id.left_image);
        mLeftLayout =  v.findViewById(R.id.left_layout);
        mRightImage =  v.findViewById(R.id.right_image);
        mRightLayout =  v.findViewById(R.id.right_layout);

        mRoot =  v.findViewById(R.id.root);
        mRightText =  v.findViewById(R.id.right_text);
        mLeftText =  v.findViewById(R.id.left_text);
        mTitle =  v.findViewById(R.id.title);
        mSonTitle =  v.findViewById(R.id.son_title);
        mCenterLayout =  v.findViewById(R.id.center_layout);
        mCenterImage =  v.findViewById(R.id.center_image);
        mDividerLine =  v.findViewById(R.id.view_divider);
    }

    private void parseStyle(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
            int textColor = ta.getColor(R.styleable. TitleBar_titleBarTextColor, context.getResources().getColor(R.color.black));

            setTextStyle(ta, mTitle,
                    R.styleable.TitleBar_titleBarTitle,
                    R.styleable.TitleBar_titleBarTitleTextColor,
                    textColor);
            setTextStyle(ta, mSonTitle,
                    R.styleable.TitleBar_titleBarsonTitle,
                    R.styleable.TitleBar_titleBarsonTitleTextColor,
                    textColor);



            setTextStyle(ta, mLeftText,
                    R.styleable.TitleBar_titleBarLeftText,
                    R.styleable.TitleBar_titleBarLeftTextColor,
                    textColor);

            setTextStyle(ta, mRightText,
                    R.styleable.TitleBar_titleBarRightText,
                    R.styleable.TitleBar_titleBarRightTextColor,
                    textColor);

            setIconStyle(ta, mLeftImage, R.styleable.TitleBar_titleBarLeftImage);
            setIconStyle(ta, mCenterImage, R.styleable.TitleBar_titleBarCenterImage);
            setIconStyle(ta, mRightImage, R.styleable.TitleBar_titleBarRightImage);


            Drawable background = ta.getDrawable(R.styleable.TitleBar_titleBarBackground);
            if (null != background) {
                mRoot.setBackground(background);
            }
            if (!TextUtils.isEmpty(mRightText.getText())) {
                mRightText.setVisibility(VISIBLE);
                mRightImage.setVisibility(GONE);
            }
            if (!TextUtils.isEmpty(mLeftText.getText())) {
                mLeftText.setVisibility(VISIBLE);
            }
            ta.recycle();
        }
    }

    private void setTextString(TextView view, String str) {
        if (TextUtils.isEmpty(str)) {
            view.setVisibility(INVISIBLE);
        } else {
            view.setVisibility(VISIBLE);
        }
        view.setText(str);
    }

    private void setTextStyle(TypedArray ta, TextView view, int textStyleId, int colorStyleId, int defaultColor) {
        if (ta == null) {
            return;
        }
        view.setText(ta.getString(textStyleId));
        view.setTextColor(ta.getColor(colorStyleId, defaultColor));
    }

    private void setIconStyle(TypedArray ta, ImageView view, int styleId) {
        if (ta == null) {
            return;
        }
        Drawable drawable = ta.getDrawable(styleId);
        if (null != drawable) {
            view.setImageDrawable(drawable);
        }
    }

    public View getDividerLine() {
        return mDividerLine;
    }


    public TextView getTitleView() {
        return mTitle;
    }

    public TextView getmLeftText() {
        return mLeftText;
    }

    public TextView getSonTitleView() {
        return mSonTitle;
    }


    public ImageView getLeftImageView() {
        return mLeftImage;
    }

    public ImageView getmCenterImage() {
        return mCenterImage;
    }

    public ImageView getRightImageView() {
        return mRightImage;
    }

    public View getLeftLayoutView() {
        return mLeftLayout;
    }

    public View getCenterLayoutView() {
        return mCenterLayout;
    }

    public View getRightLayoutView() {
        return mRightLayout;
    }


    public void setLeftImageResource(int resId) {
        mLeftImage.setImageResource(resId);
    }

    public void setCenterImageResource(int resId) {
        mCenterImage.setImageResource(resId);
    }

    public void setRightImageResource(int resId) {
        mRightImage.setImageResource(resId);
    }

    public void setLeftLayoutClickListener(OnClickListener listener) {
        mLeftLayout.setOnClickListener(listener);
    }
    public void setLeftBack(final Activity activity) {
        mLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    public void setCenterLayoutClickListener(OnClickListener listener) {
        mCenterLayout.setOnClickListener(listener);
    }

    public void setRightLayoutClickListener(OnClickListener listener) {
        mRightLayout.setOnClickListener(listener);
    }

    public void setAllLayoutClickLister(OnClickListener listener) {
        mRoot.setOnClickListener(listener);
    }



    public void hideToolbar(View view) {
        mRoot.animate()
                .y(-mRoot.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
        view.animate()
                .y(0)
                .setInterpolator(new AccelerateInterpolator(2));
    }

    public void showToolbar(View view) {
        view.animate()
                .y(mRoot.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
        mRoot.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    public static void hideToolbar(View title,View content) {
        title.animate()
                .y(-title.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
        content.animate()
                .y(0)
                .setInterpolator(new AccelerateInterpolator(2));
    }

    public static void showToolbar(View title,View content) {
        title.animate()
                .y(0)
                .setInterpolator(new AccelerateInterpolator(2));
        content.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    /*其他toolbar的设置*/
    public void setLeftLayoutVisibility(int visibility) {
        mLeftLayout.setVisibility(visibility);
    }

    public void setRightLayoutVisibility(int visibility) {
        mRightLayout.setVisibility(visibility);
    }

    public void setRightImageVisibility(int visibility) {
        mRightImage.setVisibility(visibility);
    }

    public void setLeftImageVisibility(int visibility) {
        mLeftImage.setVisibility(visibility);
    }

    public void setCenterImageVisibility(int visibility) {
        mCenterImage.setVisibility(visibility);
    }

    public void setRightTextVisibility(int visibility) {
        mRightText.setVisibility(visibility);
    }

    public void setLeftTextVisibility(int visibility) {
        mLeftText.setVisibility(visibility);
    }


    public void setDividerLineVisibility(int visibility) {
        mDividerLine.setVisibility(visibility);
    }

    public void setRightText(String title) {
        setTextString(mRightText, title);
    }

    public void setLeftText(String title) {
        setTextString(mLeftText, title);
    }

    public void setRightTitleColor(int color) {
        mRightText.setTextColor(color);
    }

    public void setLeftTitleColor(int color) {
        mLeftText.setTextColor(color);
    }

    public void setTitle(String title) {
        setTextString(mTitle, title);
    }

    public void setSonTitle(String title) {
        setTextString(mSonTitle, title);
    }



    @Override
    public void setBackgroundColor(int color) {
        mRoot.setBackgroundColor(color);
    }


    public void setTitleColor(int color) {
        mTitle.setTextColor(color);
    }

    public void setSonTitleColor(int color) {
        mSonTitle.setTextColor(color);
    }




    public TextView getRightTitle() {
        return mRightText;
    }

    public RelativeLayout getLeftLayout() {
        return mLeftLayout;
    }

    public RelativeLayout getRightLayout() {
        return mRightLayout;
    }




}
