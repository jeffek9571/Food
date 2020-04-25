/**
 * Created by Jeez on 2018/10/23.
 */
package com.example.food;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    // 用來儲存row裡每個view的id，以免每次都要取一次
    private static class ViewHolder {
//        public ImageView thumbImageView;
        public TextView name;
        public TextView vicinty;
        public TextView rating;
        public TextView qq;
        public ImageView thumbnail;
    }

    // 類別的建構子
    public MainAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    // 輸入JSON資料
    public  void updateData(JSONArray jsonArray) {
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // 檢查view是否已存在，如果已存在就不用再取一次id
        if (convertView == null) {
            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.kkkkk, null);
            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.vicinty = (TextView) convertView.findViewById(R.id.vicinity);
            holder.rating = (TextView) convertView.findViewById(R.id.rating);
            holder.qq = (TextView) convertView.findViewById(R.id.qq);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        // 取得目前這個Row的JSON資料
        JSONObject jsonObject = (JSONObject) getItem(position);
        Boolean hasThumb = false;
        if (jsonObject.has("photos")) {

            JSONArray photos = jsonObject.optJSONArray("photos");

            if(photos.length()!=0) {
                JSONObject c = photos.optJSONObject(0);
                String photo_reference=c.optString("photo_reference");

                String url="https://maps.googleapis.com/maps/api/place/photo?photoreference="+photo_reference+"&sensor=false&maxheight=1200&maxwidth=1200&key=AIzaSyDWF5Al83s2a9P1JctqM3um9usNXEpVa2U";

                Log.v("qq","111 "+url);
                // 使用 Picasso 來載入網路上的圖片
                // 圖片載入前先用placeholder顯示預設圖片
                Picasso.get().load(url).fit().placeholder(R.drawable.noimage).noFade().into(holder.thumbnail);
                hasThumb = true;
            }
        }
        if(!hasThumb){ // 沒有縮圖的話放 disp logo
            holder.thumbnail.setImageResource(R.drawable.noimage);
        }

        // 從JSON資料取得標題和摘要
        String name="";
        String vicinty="";
        String rating="";
        String qq="";

        if (jsonObject.has("name")) {
            name = jsonObject.optString("name");
        }
        if (jsonObject.has("vicinity")) {
            vicinty = jsonObject.optString("vicinity");
        }
        if (jsonObject.has("rating")) {
            rating = jsonObject.optString("rating");
        }
        if (jsonObject.has("qq")) {
            qq = jsonObject.optString("qq");
        }



        // 將標題和摘要顯示在TextView上
        holder.name.setText(name);
        holder.vicinty.setText(vicinty);
        holder.rating.setText(rating);
        holder.qq.setText(qq);
        Log.v("qq",qq);


        return convertView;
    }

}
