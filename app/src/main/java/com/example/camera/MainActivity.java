package com.example.camera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Environment.getExternalStoragePublicDirectory;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imageViewImagemCaputrada)
    ImageView imageViewCapturada;
    private String caminhoArquivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    @OnClick(R.id.buttonCapturar) void abrirCamera(){
        capturar();
    }

    private void capturar() {
        Intent tirarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tirarFoto.resolveActivity(getPackageManager()) != null) {
            File fotoFile = null;
            fotoFile = createPhotoFile();

            if (fotoFile != null ){
                caminhoArquivo = fotoFile.getAbsolutePath();
                Uri fotoUri = FileProvider.getUriForFile(MainActivity.this, "com.example.camera.fileprovider", fotoFile);
                tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(tirarFoto, 1);
            }
        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.e("CriarFoto: ", e.getMessage());
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(caminhoArquivo);
                imageViewCapturada.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViewCapturada.setImageBitmap(bitmap);
            }
        }
    }
}
