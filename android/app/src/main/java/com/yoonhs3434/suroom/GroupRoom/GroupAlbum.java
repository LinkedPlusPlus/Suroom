package com.yoonhs3434.suroom.GroupRoom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

public class GroupAlbum extends Fragment {

    private final int GALLERY_CODE=1112;

    int groupId;
    TextView testText;
    Button testButton;
    Context mContext;

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_album, container, false);

        mContext = getActivity().getBaseContext();
        // 장고에서 이미지 불러와야 함.
        int img[] = {R.drawable.loading};
        groupId = MySetting.getGroupId();

        testText = view.findViewById(R.id.testText);
        testButton = view.findViewById(R.id.testButton);

        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/test/";
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGallery();
            }
        });

        MyAdapter adapter = new MyAdapter(getActivity().getApplicationContext(), R.layout.gallery_image, img);

        GridView gridView = (GridView) view.findViewById(R.id.albumGridView);
        gridView.setAdapter(adapter);

        /*
        아이템 클릭 리스너
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                textView.setText("position : " + position);
            }
        });
        */


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(mContext, "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();

        if(requestCode == GALLERY_CODE)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    SendImage post = new SendImage();
                    post.execute(image_bitmap);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void selectGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }


    private class SendImage extends AsyncTask<Bitmap, Void, Void> {

        String REQUEST_METHOD = "POST";
        HttpURLConnection conn = null;

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {

            try {
                URL url = new URL(MySetting.getMyUrl() + "group/album/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(REQUEST_METHOD);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setUseCaches(false);

                JSONObject data = new JSONObject();
                data.accumulate("group_id", MySetting.getGroupId());
                data.accumulate("user_id", MySetting.getMyId());
                data.accumulate("image", bitmapToByteArray(bitmaps[0]));

                OutputStream os = conn.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(mContext, "이미지 업로드 완료", Toast.LENGTH_LONG);
        }
    }

    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }
}

class MyAdapter extends BaseAdapter{
    Context context;
    int layout;
    int img[];
    LayoutInflater inflater;

    public MyAdapter(Context context, int layout, int[] img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if(img == null)
            return 0;
        return img.length;
    }

    @Override
    public Object getItem(int i) {
        return img[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inflater.inflate(layout, null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.albumImage);
        iv.setImageResource(img[i]);
        return convertView;
    }
}