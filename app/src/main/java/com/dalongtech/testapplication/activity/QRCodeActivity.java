package com.dalongtech.testapplication.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.ImageUtil;
import com.dalongtech.testapplication.utils.ShotUtil;
import com.dalongtech.testapplication.utils.StringUtil;
import com.dalongtech.testapplication.utils.ToastUtil;
import com.dalongtech.testapplication.utils.ViewUtil;
import com.example.lib.QRCodeUtil.QRCodeUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class QRCodeActivity extends SimpleActivity {

    @BindView(R.id.et_qr_code_text)
    EditText et_qr_code_text;
    @BindView(R.id.iv_qr_code)
    ImageView iv_qr_code;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_qrcode;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_create_qr_code)
    public void createQRCode() {
        if(StringUtil.isBlank(ViewUtil.getText(et_qr_code_text))) {
            ToastUtil.show("请输入字符串");
            return;
        }
        iv_qr_code.setImageBitmap(QRCodeUtil.createQRCodeBitmap(et_qr_code_text.getText().toString(), 400));
    }
    @OnClick(R.id.btn_add_album)
    public void addAlbum() {
        new RxPermissions(this).request("android.permission.WRITE_EXTERNAL_STORAGE")
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if(aBoolean) {
                            Bitmap bitmap = ShotUtil.getViewBitmap(iv_qr_code);
                            String storePath = ImageUtil.saveImageToAlbum(mContext, bitmap);
                            if(StringUtil.isNotBlank(storePath)) {
                                ToastUtil.show("已添加到相册，路径是: " + storePath);
                            } else {
                                ToastUtil.show("添加失败");
                            }
                        }
                    }
                });
    }
}
