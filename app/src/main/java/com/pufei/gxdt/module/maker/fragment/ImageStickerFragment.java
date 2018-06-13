package com.pufei.gxdt.module.maker.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.MainActivity;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.utils.ImageUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 贴图
 */

public class ImageStickerFragment extends BaseMvpFragment<EditImagePresenter> implements EditImageView {

    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private ShowBitmapCallback callback;
    private ShowGifCallback gifCallback;
    private Activity activity;
    private String takePhotoPath;


    public static ImageStickerFragment newInstance() {
        return new ImageStickerFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        this.activity = activity;
        if (activity != null) {
            callback = (ShowBitmapCallback) activity;
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
                                        takePhotoPath = App.path1 + "/" + System.currentTimeMillis() + ".png";
//                                        File file = new File(takePhotoPath);
//                                        if (Build.VERSION.SDK_INT >= 24) {
//                                            imageUri = FileProvider.getUriForFile(getActivity(), "com.pufei.gxdt.fileProvider", file);
//                                        } else {
//                                            imageUri = Uri.fromFile(file);
//                                        }
                                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        ToastUtils.showShort(getActivity(), "请求权限失败,请手动开启！");
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
                stickerBottomFragment.show(getChildFragmentManager(), "dialog");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    callback.showBitmap(photo, saveBitmap(photo));
                    break;
                case PICK_REQUEST:
                    try {
                        String path1 = "";
                        Uri uri = data.getData();
                        if(uri.toString().contains("provider")) {
                            path1 = ImageUtils.getFilePathByUri(activity,uri);
                        }else {
                            path1 = getRealFilePath(activity,uri);
                        }
                        if (path1 != null) {
                            if (path1.contains(".gif") || path1.contains(".GIF")) {
                                gifCallback.showGif(new File(path1), path1);
                            } else {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                                callback.showBitmap(bitmap, path1);
                            }
                        }
                        Log.e("album path",path1 +" " + uri);
                    } catch (IOException e) {
                        Log.e("Album",e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private String saveBitmap(Bitmap bitmap) {
        File file = new File(takePhotoPath);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            return takePhotoPath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 此方式存在版本适配问题
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void upLoadImageResult(ModifyResultBean response) {

    }

    @Override
    public void downloadImageResult(String base64, int type) {

    }

    @Override
    public void downloadGifResult(String path) {

    }

    @Override
    public void recommentTextResult(RecommendTextBean response) {

    }

    @Override
    public void materialResult(MaterialBean response,int type) {

    }

    @Override
    public void requestErrResult(String msg) {

    }

    @Override
    public void requestSuccess(String msg) {

    }

    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if(presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }

    public interface ShowGifCallback {
        void showGif(File gif, String path);
    }

    public interface ShowBitmapCallback {
        void showBitmap(Bitmap bitmap, String path);
    }

}
