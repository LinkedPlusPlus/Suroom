package com.yoonhs3434.suroom.GroupRoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GroupAlbum extends Fragment {

    int groupId;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_album, container, false);

        // 장고에서 이미지 불러와야 함.
        int img[] = null;
        groupId = MySetting.getGroupId();

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

    private class SendImage extends AsyncTask<String, Void, Void> {

        String REQUEST_METHOD = "POST";
        HttpURLConnection conn = null;

        @Override
        protected Void doInBackground(String... strings) {
            String path = strings[0];

            try {
                FileInputStream fileInputStream = new FileInputStream(new File(path));

                URL url = new URL(MySetting.getMyUrl() + "group/album/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(REQUEST_METHOD);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setUseCaches(false);

                // path : ex) "/sdcard/aaa.jpg"
                Bitmap img = BitmapFactory.decodeFile(path);

                JSONObject data = new JSONObject();
                data.accumulate("group_id", MySetting.getGroupId());
                data.accumulate("user_id", MySetting.getMyId());
                data.accumulate("image", getStringFromBitmap(img));

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
                if(conn != null)
                    conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

        private String getStringFromBitmap(Bitmap bitmapPicture) {
            /*
             * This functions converts Bitmap picture to a string which can be
             * JSONified.
             * */
            final int COMPRESSION_QUALITY = 100;
            String encodedImage;
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                    byteArrayBitmapStream);
            byte[] b = byteArrayBitmapStream.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
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