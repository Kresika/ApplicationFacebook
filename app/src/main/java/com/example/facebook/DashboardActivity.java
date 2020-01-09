package com.example.facebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebook.adapters.PostsAdapter;
import com.example.facebook.model.Login;
import com.example.facebook.model.Post;
import com.example.facebook.model.PostResponse;
import com.example.facebook.network.Retrofit;
import com.example.facebook.network.RetrofitApi;
import com.example.facebook.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    private Bitmap mBitmap;

    private List<Post> postList = new ArrayList<>();
    private List<Post> post = new ArrayList<>();
    private EditText captiontxt;
    private Button imgbtn, uploadbtn;
    private ImageView image, profilepic, logout;
    private RecyclerView postsview;
    SharedPreferences msharedpreferences;
    private TextView noposts;

    PostsAdapter postsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        askpermission();
        viewInit();
        getPosts();


    }

    private void getPosts(){
        String token = msharedpreferences.getString(Constants.TOKEN, null);


        RetrofitApi api =  Retrofit.retrofitInit().create(RetrofitApi.class);
        Call<List<Post>> postresponse = api.getPost(token);
        postresponse.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                post = response.body();
                if(post.isEmpty()){
                    noposts.setVisibility(View.VISIBLE);
                }else{
                    noposts.setVisibility(View.GONE);
                    for (Post postt : post) {

                        postList.add(postt);
                    }

                    postsAdapter = new PostsAdapter(getApplicationContext(), postList);
                    postsview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    postsview.setAdapter(postsAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void viewInit(){
        captiontxt = findViewById(R.id.posttxt);
        imgbtn = findViewById(R.id.imgbtn);
        uploadbtn = findViewById(R.id.upload);
        image = findViewById(R.id.imgpreview);
        profilepic = findViewById(R.id.profilepic);
        postsview = findViewById(R.id.postsview);
        logout = findViewById(R.id.logout);
        noposts = findViewById(R.id.noposts);

        String profileimg = Constants.IMAGE_URL + MainActivity.userlist.get(0).getImg();

        Picasso.get().load(profileimg).into(profilepic);
        msharedpreferences = getSharedPreferences("login", MODE_PRIVATE);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.userlist.clear();
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                finish();

            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBitmap != null)
                    multipartUpload();
                else {
                    Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void askpermission(){
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(permissionsToRequest.size() > 0){
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }





    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){

            String filePath = getImageFilePath(data);
            Log.d(TAG, "onActivityResult: " + filePath);
            if(requestCode == IMAGE_RESULT){
                if (filePath != null) {
                    image.setVisibility(View.VISIBLE);
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    image.setImageBitmap(mBitmap);
                }
            }
        }
    }

    private String getImageFromFilePath(Intent data){
        boolean isCamera = data == null || data.getData() == null;
        if(isCamera) return    getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }


    public Intent getPickImageChooserIntent(){
        Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntent = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent,0 );
        for(ResolveInfo res : listCam){
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName,res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if(outputFileUri != null){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            }
            allIntent.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntent.add(intent);
        }


        Intent mainIntent = allIntent.get(allIntent.size() - 1);
        for(Intent intent : allIntent){
            if(intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")){
                mainIntent = intent;
                break;
            }

        }
        allIntent.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select Source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntent.toArray(new Parcelable[allIntent.size()]));

        return chooserIntent;
    }





    private String getImageFilePath(Intent data){
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri){

        String [] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        picUri = savedInstanceState.getParcelable("pic_uri");
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void multipartUpload(){

        String caption = captiontxt.getText().toString();
        String token = msharedpreferences.getString(Constants.TOKEN, null);

//        Toast.makeText(DashboardActivity.this, token, Toast.LENGTH_SHORT).show();


        try {File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();



            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
            RequestBody postcaption = RequestBody.create(MediaType.parse("text/plain"), caption);

            RetrofitApi api =  Retrofit.retrofitInit().create(RetrofitApi.class);
            Call<List<PostResponse>> postresponse = api.addpost(token, postcaption,body);

            postresponse.enqueue(new Callback<List<PostResponse>>() {
                @Override
                public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                    Toast.makeText(DashboardActivity.this, response.body().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                    captiontxt.setText("");
                    image.setVisibility(View.GONE);

                    postList.clear();
                    getPosts();


                }

                @Override
                public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                    Toast.makeText(DashboardActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

                }
            });









        }catch (FileNotFoundException e){

        }catch (IOException e) {
            e.printStackTrace();
        }



    }


}
