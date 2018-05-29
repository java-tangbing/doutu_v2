package com.pufei.gxdt.module.maker.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 贴图
 */

public class ImageStickerFragment extends BaseFragment  {

    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private ShowBitmapCallback callback;
    private ShowGifCallback gifCallback;
    private Activity activity;

    public static ImageStickerFragment newInstance() {
        return new ImageStickerFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        this.activity = activity;
        if(activity != null) {
            callback = (ShowBitmapCallback)activity;
            gifCallback = (ShowGifCallback) activity;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_image_sticker;
    }

    @OnClick({R.id.ll_take_photo, R.id.ll_open_album, R.id.ll_template})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_take_photo:
                Acp.getInstance(getActivity())
                        .request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA).build(),
                                new AcpListener() {
                                    @Override
                                    public void onGranted() {
                                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        ToastUtils.showShort(getActivity(),"请求权限失败,请手动开启！");
                                    }
                                });
                break;
            case R.id.ll_open_album:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, ""), PICK_REQUEST);
                break;
            case R.id.ll_template:
                StickerBottomFragment stickerBottomFragment = new StickerBottomFragment();
                stickerBottomFragment.show(getActivity().getSupportFragmentManager(),"dialog");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    callback.showBitmap(photo);
                    break;
                case PICK_REQUEST:
                    try {
                        Uri uri = data.getData();
                        String path = getRealFilePath(activity,uri);
                        if(path != null) {
                            if(path.contains(".gif") || path.contains(".GIF")) {
                                gifCallback.showGif(new File(path));
                            }else {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                                callback.showBitmap(bitmap);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public interface ShowGifCallback {
        void showGif(File gif);
    }

    public interface ShowBitmapCallback {
        void showBitmap(Bitmap bitmap);
    }

}
