package com.netease.nim.demo.main.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.netease.nim.demo.R;
import com.netease.nim.demo.chatroom.fragment.ChatRoomsFragment;
import com.netease.nim.demo.login.HttpUtil;
import com.netease.nim.demo.login.JsonTools;
import com.netease.nim.demo.main.activity.MainActivity;
import com.netease.nim.demo.main.model.MainTab;
import com.netease.nim.demo.main.model.User;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.contact.core.item.ContactItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 聊天室主TAB页
 * Created by huangjun on 2015/12/11.
 */
public class MapFragment extends MainTabFragment {
    private MapView mMapView;
    private Handler Getuhandler;
    private Handler Getphandler;
    private static List<User> users;
    private Bitmap[] bm;
    private String url=HttpUtil.BASE_URL;
    private BaiduMap mBaidumap;

    public MapFragment() {
        setContainerId(MainTab.CHAT_ROOM.fragmentId);
    }

    @Override
    protected void onInit() {
        // 采用静态集成，这里不需要做什么了
        mMapView = (MapView) getView().findViewById(R.id.bmapView);
        mBaidumap = mMapView.getMap();
        //设置地图默认中心点
        LatLng cenpt = new LatLng(30.558597, 103.999578);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaidumap.setMapStatus(mMapStatusUpdate);

//************************************************
        GuThread guThread=new GuThread();
        guThread.start();
        Getuhandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                String jsonstring = data.getString("json");
                users = JsonTools.getUsers("users", jsonstring);
                String[] photoName=new String[users.size()];
                for(int i=0;i<users.size();i++){

                    photoName[i]=users.get(i).getPhotopath();
                    Log.i("number23",users.get(i).getUsername()+"");
                    Log.i("ladsfa",users.get(i).getPhotopath()+"");

                }
                ManyPic(photoName);
            }
        };
        Getphandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {


                for(int i=0;i<bm.length;i++){

                    if(bm[i]==null) {
                        bm[i] = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_def2);
                        bm[i]=large(bm[i]);
                    }
                    bm[i]=small(bm[i]);
                    bm[i]=makeRoundCorner(bm[i]);
                }
                addMarker(users,bm);
                initClickListener();
            }

        };

    }


    public void addMarker(List<User> tempusers,Bitmap[] tempBitmap){

        for(int i=0;i<tempusers.size();i++) {

            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(tempBitmap[i]);
            LatLng point = new LatLng(tempusers.get(i).getLatitude(), tempusers.get(i).getLongitude());

            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
//在地图上添加Marker，并显示
            Marker marker = (Marker) mBaidumap.addOverlay(option);

            Bundle bundle = new Bundle();
            bundle.putSerializable("user", users.get(i));
            marker.setExtraInfo(bundle);
        }


    }
    public static User getUser(String name){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getUsername()==name){
                return users.get(i);
            }
        }
        return null;
    }

    private static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.2f,0.2f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }

    private static Bitmap large(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.55f,0.55f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }

    public void initClickListener(){
        BaiduMap.OnMarkerClickListener mMarkerlis = new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                User user=(User)marker.getExtraInfo().get("user");
                Log.i("this",user.getUsername());
                NimUIKit.getContactEventListener().onItemClick(getActivity(),user.getUsername());

                return true;
            }
        };
        mBaidumap.setOnMarkerClickListener(mMarkerlis);

    }

    private class GuThread extends Thread{
        @Override
        public  void run(){
            String json= HttpUtil.getJsonContent(HttpUtil.BASE_URL+"getUserServlet");
            Message msg = new Message();
            Bundle temp = new Bundle();
            temp.putString("json",json);
            msg.setData(temp);
            Getuhandler.sendMessage(msg);
        }
    }

    private void ManyPic(final String[] picName) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Message ms = new Message();
                try {
                    if (getBitmap(url, picName) != null) {
                        bm = getBitmap(url, picName);

                        ms.obj = bm;
                        Getphandler.sendMessage(ms);
                    } else {
                        bm = getBitmap(url, picName);
                        Log.i("else123","null");
                        Getphandler.sendMessage(ms);
                        ms.obj = "下载失败";
                        ms.sendToTarget();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static Bitmap[] getBitmap(String path,String[] picName) throws IOException {
        Bitmap[] b=new Bitmap[picName.length];

        for (int i = 0; i < picName.length; i++) {
            if(picName[i]!=null) {
                URL url = new URL(path + picName[i]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
                    InputStream inputStream = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    b[i] =bitmap;
                }
            }

        }

        return b;
    }

    public static Bitmap makeRoundCorner(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height/2;
        if (width > height) {
            left = (width - height)/2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width)/2;
            right = width;
            bottom = top + width;
            roundPx = width/2;
        }


        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


}
