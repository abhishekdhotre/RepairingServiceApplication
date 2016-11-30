package edu.uta.utarepairingservices;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUpload extends AppCompatActivity {

    private static final int SELECTED_PICTURE=1;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    Button btnFind, btnUpload;
    ImageView imageView;
    private Bitmap bitmap;
    private String encoded_string, image_name;
    private File file;
    private Uri file_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        btnFind = (Button) findViewById(R.id.button1);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imageView);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirectoryPath = pictureDirectory.getPath();

                Uri data = Uri.EMPTY.parse(pictureDirectoryPath);

                photoPickerIntent.setDataAndType(data, "image/*");

                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri ImageUri = data.getData();

                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(ImageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

                new EncodeImage().execute();
            }
        }
    }

    private class EncodeImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }
    }

    private void makeRequest() {
    }
}
