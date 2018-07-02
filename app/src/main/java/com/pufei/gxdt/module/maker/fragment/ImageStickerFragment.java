package com.pufei.gxdt.module.maker.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.utils.ImageUtils;
import com.pufei.gxdt.utils.ToastUtils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
                        .request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).build(),
                                new AcpListener() {
                                    @Override
                                    public void onGranted() {
                                        takePhotoPath = App.path1 + "/" + System.currentTimeMillis() + ".png";
                                        File file = new File(takePhotoPath);
                                        Uri imageUri = null;
                                        if (Build.VERSION.SDK_INT >= 24) {
                                            imageUri = FileProvider.getUriForFile(getActivity(), "com.pufei.gxdt.fileProvider", file);
                                        } else {
                                            imageUri = Uri.fromFile(file);
                                        }
                                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        ToastUtils.showShort(getActivity(), "请求权限失败,请手动开启！");
                                    }
                                });
                break;
            case R.id.ll_open_album:
                Acp.getInstance(getActivity())
                        .request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(),
                                new AcpListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onGranted() {
                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            return;
                                        }
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, ""), PICK_REQUEST);

                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        ToastUtils.showShort(getActivity(), "请求权限失败,请手动开启！");
                                    }
                                });

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
                    File picPath = new File(takePhotoPath);
                    if(picPath.exists()) {
                        Uri uri = Uri.fromFile(picPath);
                        beginCrop(uri);
                    }else {
                        Log.e("camera","文件不存在!");
                    }
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    callback.showBitmap(photo, saveBitmap(photo));
                    break;
                case PICK_REQUEST:
                    String path1 = "";
                    Uri uri = data.getData();
                    if (uri.toString().contains("provider")) {
                        path1 = ImageUtils.getFilePathByUri(activity, uri);
                    } else {
                        path1 = getRealFilePath(activity, uri);
                    }
                    if (path1 != null) {
                        if (path1.contains(".gif") || path1.contains(".GIF")) {
                            gifCallback.showGif(new File(path1), path1);
                        } else {
                            beginCrop(data.getData());
//                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
//                                callback.showBitmap(bitmap, path1);
                        }
                    }
//                    Log.e("album path", path1);
                    break;
                case Crop.REQUEST_CROP:
//                    Log.e("fdsfd","裁剪");
                    handleCrop(resultCode,data);
                    break;
            }
        }else {
            Log.e("onActivityResult",resultCode+" " + requestCode);
        }
    }

    private void beginCrop(Uri source) {
//        Log.e("camera","拍照");
        Uri destination = Uri.fromFile(new File(App.path1+"/",System.currentTimeMillis()+".png"));
        Crop.of(source, destination).asSquare().start(getActivity(),this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            String path1 = "";
            Uri uri = Crop.getOutput(result);
            if (uri.toString().contains("provider")) {
                path1 = ImageUtils.getFilePathByUri(activity, uri);
            } else {
                path1 = getRealFilePath(activity, uri);
            }
//            Log.e("fdsf",path1+" ");
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                callback.showBitmap(bitmap, path1);
            } catch (IOException e) {
//                e.printStackTrace();
                Log.e("ioException",e.getMessage()+" ");
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            ToastUtils.showShort(getActivity(), Crop.getError(result).getMessage());
        }
    }

    private String saveBitmap(Bitmap bitmap) {
        File file = new File(takePhotoPath);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
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
     *
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
    public void materialResult(MaterialBean response, int type) {

    }

    @Override
    public void requestErrResult(String msg) {

    }

    @Override
    public void requestSuccess(String msg) {

    }

    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if (presenter == null) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(activity != null) {
            activity = null;
        }
        if(callback != null) {
            callback = null;
        }

        if(gifCallback != null) {
            gifCallback = null;
        }
    }
}
