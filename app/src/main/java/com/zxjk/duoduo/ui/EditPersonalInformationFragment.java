package com.zxjk.duoduo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.liang.permission.annotation.Permission;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.weight.TitleBar;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.annotation.Nullable;
import butterknife.OnClick;

import static com.zxjk.duoduo.utils.PermissionUtils.verifyStoragePermissions;
import static java.lang.System.currentTimeMillis;

/**
 * @author Administrator
 * @// TODO: 2019\3\18 0018 个人实名界面
 */
public class EditPersonalInformationFragment extends BaseFragment implements View.OnClickListener {


    /**
     * 这是关于标题栏的控件
     */
    TitleBar titleBar;
    /**
     * 这是关于点击完成按钮的控件
     */
    TextView commitBtn;
    /**
     * 这是关于点击图片，选择头像的按钮
     */
    ImageView imageSearchBtn;
    /**
     * 这里是输入昵称的输入框
     */
    EditText editNickName;
    /**
     * 这里是输入地区的输入框
     */
    EditText editArea;






    // 声明File对象
    private File imageFile = null;

    private String path = "";


    public static EditPersonalInformationFragment newInstance() {
        EditPersonalInformationFragment fragment = new EditPersonalInformationFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_personal_information, null);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initView(View view) {
        titleBar = view.findViewById(R.id.m_edit_information_title_bar);
        commitBtn = view.findViewById(R.id.m_edit_information_btn);
        imageSearchBtn = view.findViewById(R.id.m_edit_information_header_icon);
        editArea=view.findViewById(R.id.m_edit_information_name_edit);
        editNickName=view.findViewById(R.id.m_edit_information_area_edit);

        imageSearchBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Permission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @OnClick(R.id.m_edit_information_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_edit_information_btn:
                if (editNickName.getText().toString().isEmpty()) {
                    ToastUtils.showShort("昵称不能为空");
                    return;
                }
                if (editArea.getText().toString().isEmpty()){
                    ToastUtils.showShort("地区不能为空");
                    return;
                }


                break;
            case R.id.m_edit_information_header_icon:

                selectPicture();
                break;
            default:
                break;

        }


    }

    /**
     * 打开本地选择图片
     */
    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    /**
     * 把用户选择好的图片显示在ImageView上
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if (resultCode == Activity.RESULT_OK) {
            //获取选中文件的定位符
            Uri uri = data.getData();
            //使用content的接口
            ContentResolver cr = getActivity().getContentResolver();
            try {

                //获取图片
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                imageSearchBtn.setImageBitmap(bitmap);
                //根目录下存储
                verifyStoragePermissions(getActivity());


//
//                ImageUtils.compressByQuality (bitmap,20);
//                ImageUtils.save(bitmap, filePath, Bitmap.CompressFormat.JPEG,true);
//               String imageCommitPath=FileUtils.getFileByPath(filePath).getPath();
                LogUtils.d("TAG"+data.getData().getPath());
                OssUtils.uploadFile(data.getData().getPath(), new OssUtils.OssCallBack() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort("图片上传成功");
                    }
                    @Override
                    public void onError() {
                        ToastUtils.showShort("图片上传失败");
                    }
                });
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
