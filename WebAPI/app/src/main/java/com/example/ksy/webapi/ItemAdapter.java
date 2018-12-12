package com.example.ksy.webapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private List<APIInfo> itemList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView play,title,content,duration,videoReview,createTime;
        ProgressBar progressBar;
        SeekBar seekBar;
        List<Bitmap> priviewImage;
        Bitmap coverImage;

        public ViewHolder(View view){
            super(view);
            cover = (ImageView)view.findViewById(R.id.picture);
            play = (TextView)view.findViewById(R.id.play);
            title = (TextView)view.findViewById(R.id.title);
            content = (TextView)view.findViewById(R.id.content);
            duration = (TextView)view.findViewById(R.id.duration);
            createTime = (TextView)view.findViewById(R.id.create_time);
            videoReview = (TextView)view.findViewById(R.id.video_review);
            progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
            seekBar = (SeekBar)view.findViewById(R.id.seekbar);
            priviewImage = new ArrayList<>();
            //seekBar.setEnabled(false);
            seekBar.setMax(0);
        }
    }

    public ItemAdapter(List<APIInfo> mitemList){
        this.itemList = mitemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= viewHolder.priviewImage.size() || !fromUser)
                    return;
                viewHolder.cover.setImageBitmap(viewHolder.priviewImage.get(progress));
                //Log.d("ItemAdapter", "seek bar changeed");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(0);
                if(viewHolder.coverImage != null)
                    viewHolder.cover.setImageBitmap(viewHolder.coverImage);
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        //Log.d("ItemAdapter",position+"");
        final APIInfo item = itemList.get(position);
        final ViewHolder viewHolder = holder;
        //holder.cover.setImageURI(Uri.parse(item.GetCoverURL()));
        holder.play.setText(String.valueOf(item.GetPlay()));
        holder.title.setText(item.GetTitle());
        holder.content.setText(item.GetContent());
        holder.duration.setText(item.GetDuration());
        holder.createTime.setText(item.GetCreateTime());
        holder.videoReview.setText(String.valueOf(item.GetVideoReview()));

        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> emitter)throws Exception{
                try{
                    URL url = new URL(item.GetCoverURL());
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    viewHolder.coverImage = BitmapFactory.decodeStream(inputStream);
                    emitter.onNext(viewHolder.coverImage);
                    inputStream.close();
                    connection.disconnect();
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Observer<Bitmap>() {
              @Override
              public void onSubscribe(Disposable d) {
              }

              @Override
              public void onNext(Bitmap bitmap) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                    //viewHolder.cover.setVisibility(View.VISIBLE);
                    viewHolder.cover.setImageBitmap(bitmap);
              }

              @Override
              public void onError(Throwable e) {
              }
              @Override
              public void onComplete() {
              }
          });

        Observable.create(new ObservableOnSubscribe<PriviewInfo>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<PriviewInfo> emitter)throws Exception{
                HttpURLConnection connection = null;
                try{
                    URL url = new URL("https://api.bilibili.com/pvideo?aid="+item.GetAid());
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    if(connection.getResponseCode() == 200){
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null){
                            response.append(line);
                        }
                        in.close();
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if(jsonObject.getInt("code") == 0){
                            Gson gson = new Gson();
                            PriviewInfo priviewInfo = gson.fromJson(jsonObject.getString("data"),PriviewInfo.class);
                            emitter.onNext(priviewInfo);
                        }else{
                            Log.d("ItemAdapter","该 aid 不正确");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }

        }).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext(new Consumer<PriviewInfo>() {
                @Override
                public void accept(PriviewInfo priviewInfo) throws Exception {
                    try{
                        URL url = new URL(priviewInfo.GetImage().get(0));
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        int pictureNum = priviewInfo.GetIndex().size()-2;
                        int xLen = priviewInfo.GetImageXLen();
                        int imageXSize = priviewInfo.GetImageXSize();
                        int imageYSize = priviewInfo.GetImageYSize();
                        int yLen = pictureNum / xLen +1;
                        if(bitmap == null){
                            return;
                        }
                        holder.priviewImage.clear();
                        for(int y = 0;y < yLen;y++){
                            for(int x = 0;x < xLen; x++){
                                if((y+1) * xLen + x >= pictureNum){
                                    break;
                                }
                                Bitmap pic = Bitmap.createBitmap(bitmap,x*imageXSize,y*imageYSize,imageXSize,imageYSize);
                                holder.priviewImage.add(pic);
                            }
                        }
                        connection.disconnect();
                        inputStream.close();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<PriviewInfo>() {
                  @Override
                  public void onSubscribe(Disposable d) {
                  }

                  @Override
                  public void onNext(final PriviewInfo priviewInfo) {
                      holder.seekBar.setMax(holder.priviewImage.size()-1);
                     // Log.d("ItemAdapter","position: " + position +" size: "+holder.priviewImage.size());
                      /*holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                         @Override
                         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                             if(progress >= holder.priviewImage.size() || !fromUser)
                                 return;
                             seekBar.setProgress(progress);
                             holder.cover.setImageBitmap(holder.priviewImage.get(progress));
                             Log.d("ItemAdapter","seek bar changeed");
                         }

                         @Override
                         public void onStartTrackingTouch(SeekBar seekBar) {

                         }

                         @Override
                         public void onStopTrackingTouch(SeekBar seekBar) {

                         }
                     });*/

                  }

                  @Override
                  public void onError(Throwable e) {

                  }

                  @Override
                  public void onComplete() {

                  }
          });
    }

    @Override
    public int getItemCount(){
        return itemList.size();
    }
}
