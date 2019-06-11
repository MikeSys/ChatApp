package com.sinfo.chat

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v7.widget.RecyclerView.ViewHolder;

public class myAdapter extends RecyclerView.Adapter<ViewHolder>{

    private ArrayList<ModelloDati> data_list;
    private ArrayList<ModelloDati> selected_items_list = new ArrayList<>();
    private Context context;
    private Uri uri;
    private static final int tlTEXT = 0;
    private static final int tlIMAGE = 1;
    private static final int tlVIDEO = 2;
    private static final int tlAUDIO = 3;
    private static final int tlDATE = 4;
    private boolean multiSelect = false;
    private ActionMode actionMode;
    private int counter = 0;
    private boolean player_paused = false;

    private MediaPlayer player;
    private boolean set_media_Player = false;
    private int actual_position;



    //ActionMode------------------------------------------------------------------
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {


        @Override
        public boolean onCreateActionMode(final ActionMode actionMode, Menu menu) {
            multiSelect = true;
            actionMode.getMenuInflater().inflate(R.menu.action_bar_menu, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {


            return false;
        }


        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {


            switch (menuItem.getItemId()){
                case R.id.delete:

                    for(ModelloDati oggetto : selected_items_list)
                    {
                        if(oggetto.getTYPE() == tlTEXT)  //IGNORE COMMENT!**** da caambiare quando verra implementato su altre view
                            data_list.remove(data_list.indexOf(oggetto)-1);
                        data_list.remove(oggetto);


                    }

                    actionMode.finish();
                    return true;


                case R.id.share:
                    Toast.makeText(context, "Condividi", Toast.LENGTH_SHORT).show();
                    return true;


                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            multiSelect = false;
            selected_items_list.clear();
            notifyDataSetChanged();
            counter = 0;

        }
    };



   //ViewHolder-----------------------------------------------------------

    public class TextViewHolder extends ViewHolder {
        // each data item is just a string in this case
        private TextView titolo;
        private TextView ora;
        private RelativeLayout selezione_layout_testo;


        public TextViewHolder(View v) {
            super(v);
            titolo = itemView.findViewById(R.id.testo);
            selezione_layout_testo = itemView.findViewById(R.id.layout_selezione_testo);
            ora = itemView.findViewById(R.id.ora);

        }

        void selectItem(ModelloDati item) {
            if (multiSelect) {
                if (selected_items_list.contains(item)) {
                    selected_items_list.remove(item);
                    counter--;
                    if(selected_items_list.size()==0)
                    {
                        if (actionMode != null ){
                            actionMode.finish();
                        }
                    }
                    selezione_layout_testo.setBackgroundColor(context.getResources().getColor(R.color.transparent));

                } else {
                    selected_items_list.add(item);
                    counter++;
                    selezione_layout_testo.setBackgroundColor(context.getResources().getColor(R.color.Colore_selezione));
                }
            }
        }

        void update(final ModelloDati value) {
            if (selected_items_list.contains(value)) {
                selezione_layout_testo.setBackgroundColor(context.getResources().getColor(R.color.Colore_selezione));

            } else {
                selezione_layout_testo.setBackgroundColor(context.getResources().getColor(R.color.transparent));


            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    actionMode = ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallback);
                    selectItem(value);
                    if(counter != 0) {
                        String counter_string = String.valueOf(selected_items_list.size());
                        actionMode.setTitle(counter_string);
                    }
                    else {
                        actionMode.setTitle("");
                    }

                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(value);
                    if(counter != 0) {
                        String counter_string = String.valueOf(selected_items_list.size());
                        actionMode.setTitle(counter_string);
                    }
                    else {
                        if(actionMode != null)
                        actionMode.setTitle("");
                    }

                }
            });
        }
    }

    public class ImageViewHolder extends ViewHolder {
        //ImageView mImage;
        private ImageView imageView;
        private TextView ora_layout_foto;
        private RelativeLayout selezione_layout_foto;


        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.immagine);
            ora_layout_foto = itemView.findViewById(R.id.ora_layout_Foto);
            selezione_layout_foto = itemView.findViewById(R.id.layout_selezione_foto);

        }

        void selectItem(ModelloDati item) {
            if (multiSelect) {
                if (selected_items_list.contains(item)) {
                    selected_items_list.remove(item);
                    counter--;
                    if(selected_items_list.size()==0)
                    {
                        if (actionMode != null ){
                            actionMode.finish();
                        }
                    }
                    selezione_layout_foto.setBackgroundColor(context.getResources().getColor(R.color.transparent));

                } else {
                    selected_items_list.add(item);
                    counter++;
                    selezione_layout_foto.setBackgroundColor(context.getResources().getColor(R.color.Colore_selezione));
                }
            }
        }

        void update(final ModelloDati value) {
            if (selected_items_list.contains(value)) {
                selezione_layout_foto.setBackgroundColor(context.getResources().getColor(R.color.Colore_selezione));

            } else {
                selezione_layout_foto.setBackgroundColor(context.getResources().getColor(R.color.transparent));


            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    actionMode = ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallback);
                    selectItem(value);
                    if(counter != 0) {
                        String counter_string = String.valueOf(selected_items_list.size());
                        actionMode.setTitle(counter_string);
                    }
                    else {
                        actionMode.setTitle("");
                    }

                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(value);
                    if(counter != 0) {
                        String counter_string = String.valueOf(selected_items_list.size());
                        actionMode.setTitle(counter_string);
                    }
                    else {
                        actionMode.setTitle("");
                    }

                }
            });
        }
    }

    public class VideoViewHolder extends ViewHolder {
        private ImageView videoView;
        private ImageView play_button;
        private TextView ora_layout_video;
        private RelativeLayout selezione_layout_video;

        public VideoViewHolder(View v) {
            super(v);
            videoView = itemView.findViewById(R.id.rec);
            ora_layout_video = itemView.findViewById(R.id.ora_layout_Video);
            selezione_layout_video = itemView.findViewById(R.id.layout_selezione_video);
            play_button = itemView.findViewById(R.id.play_button);

        }

        void selectItem(ModelloDati item) {
            if (multiSelect) {
                if (selected_items_list.contains(item)) {
                    selected_items_list.remove(item);
                    counter--;
                    if(selected_items_list.size()==0)
                    {
                        if (actionMode != null ){
                            actionMode.finish();
                        }
                    }
                    selezione_layout_video.setBackgroundColor(context.getResources().getColor(R.color.transparent));

                } else {
                    selected_items_list.add(item);
                    counter++;
                    selezione_layout_video.setBackgroundColor(context.getResources().getColor(R.color.Colore_selezione));
                }
            }
        }

        void update(final ModelloDati value) {
            if (selected_items_list.contains(value)) {
                selezione_layout_video.setBackgroundColor(context.getResources().getColor(R.color.Colore_selezione));

            } else {
                selezione_layout_video.setBackgroundColor(context.getResources().getColor(R.color.transparent));


            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    actionMode = ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallback);
                    selectItem(value);
                    if(counter != 0) {
                        String counter_string = String.valueOf(selected_items_list.size());
                        actionMode.setTitle(counter_string);
                    }
                    else {
                        if(actionMode != null)
                        actionMode.setTitle("");
                    }

                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(value);
                    if(counter != 0) {
                        String counter_string = String.valueOf(selected_items_list.size());
                        actionMode.setTitle(counter_string);
                    }
                    else {
                        actionMode.setTitle("");
                    }

                }
            });
        }


    }

    public class AudioViewHolder extends ViewHolder {
        private ImageButton play_pause;
        private SeekBar seekBar;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            play_pause = itemView.findViewById(R.id.audio_play_button);
            seekBar = itemView.findViewById(R.id.seekBar);


            play_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!set_media_Player) {
                        startPlaying(seekBar,play_pause);
                        set_media_Player = true;
                        play_pause.setImageDrawable(context.getDrawable(R.drawable.pause));
                    }
                    else if(player.isPlaying()){
                        player.pause();
                        player_paused = true;
                        play_pause.setImageDrawable(context.getDrawable(R.drawable.audio_play));
                    }
                    else if(!player.isPlaying()){
                        player.start();
                        player_paused = false;
                        play_pause.setImageDrawable(context.getDrawable(R.drawable.pause));
                    }

                }
            });


        }
    }

    public class DataLayoutHolder extends ViewHolder {
        private TextView data;


        public DataLayoutHolder(@NonNull View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.Data);
        }
    }








    //Adapter-------------------------------------------------------------

    public myAdapter(ArrayList<ModelloDati> lista, Context context) {
        this.data_list = lista;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == tlTEXT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_testo, parent, false);
            return new TextViewHolder(v);
        } else if (viewType == tlIMAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_foto, parent, false);
            return new ImageViewHolder(v);
        } else if (viewType == tlVIDEO) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_video, parent, false);
            return new VideoViewHolder(v);
        } else if (viewType == tlAUDIO) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_audio, parent, false);
            return new AudioViewHolder(v);
        } else if (viewType == tlDATE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_data, parent, false);
            return new DataLayoutHolder(v);


        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder myViewHolder, final int position) {
        final ModelloDati oggetto = data_list.get(myViewHolder.getAdapterPosition());
        actual_position = position;

        switch (myViewHolder.getItemViewType()) {

            case tlTEXT:
                final TextViewHolder textViewHolder = (TextViewHolder) myViewHolder;
                textViewHolder.update(oggetto);
                textViewHolder.titolo.setText(oggetto.getString());
                textViewHolder.ora.setText(aggiornaTempo(0));
                break;

            case tlIMAGE:
                final ImageViewHolder imageViewHolder = (ImageViewHolder) myViewHolder;
                imageViewHolder.update(oggetto);
                imageViewHolder.imageView.setImageBitmap(oggetto.getImmagini());
                imageViewHolder.ora_layout_foto.setText(aggiornaTempo(0));
                break;


            case tlVIDEO:
                final VideoViewHolder videoViewHolder = (VideoViewHolder) myViewHolder;
                videoViewHolder.update(oggetto);
                uri = oggetto.getUri();
                videoViewHolder.videoView.requestFocus();
                Glide.with(context).load(uri).into(videoViewHolder.videoView);
                videoViewHolder.ora_layout_video.setText(aggiornaTempo(0));
                if(!multiSelect) {
                    videoViewHolder.play_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                    Context context = v.getContext();
                                    Intent intent = new Intent(context, FullscreenVideo.class);
                                    intent.putExtra("VIDEO", uri.toString());
                                    context.startActivity(intent);
                        }
                    });
                }

                break;

            case tlAUDIO:
                final AudioViewHolder audioViewHolder = (AudioViewHolder) myViewHolder;
                audioViewHolder.play_pause.requestFocus();

                break;


            case tlDATE:
                String data = aggiornaTempo(1);
                DataLayoutHolder viewHolder2 = (DataLayoutHolder) myViewHolder;
                viewHolder2.data.setText(data);

                break;


            default:
                Log.e("FATAL", "Unable to bind data");


        }


    }

    @Override
    public int getItemViewType(int position) {

        switch (data_list.get(position).getTYPE()) {
            case tlTEXT:
                return tlTEXT;
            case tlIMAGE:
                return tlIMAGE;
            case tlVIDEO:
                return tlVIDEO;
            case tlAUDIO:
                return tlAUDIO;
            case tlDATE:
                return tlDATE;
        }
        return 0;

    }

    @Override
    public int getItemCount() {
        if (data_list == null)
            return 0;
        return data_list.size();
    }

    //-------------------------------------------------------------------------







    //-------------------------------------------------------------------------
    private String aggiornaTempo(int tipo) {


        if (tipo == 0) {
            Date CURRENT_TIME = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(CURRENT_TIME);
            return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " ";
        }
        if (tipo == 1) {
            Date CURRENT_TIME = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            int mese = calendar.get(Calendar.MONTH);
            calendar.setTime(CURRENT_TIME);
            return  "" + calendar.get(Calendar.DAY_OF_MONTH) + " " + ConvertiMese(mese);
        } else {
            return null;
        }
    }

    private String ConvertiMese(int mese) {
        if (mese == 1) return "Gennaio";
        if (mese == 2) return "febbraio";
        if (mese == 3) return "marzo";
        if (mese == 4) return "aprile";
        if (mese == 5) return "maggio";
        if (mese == 6) return "giugno";
        if (mese == 7) return "luglio";
        if (mese == 8) return "agosto";
        if (mese == 9) return "settembre";
        if (mese == 10) return "ottobre";
        if (mese == 11) return "novembre";
        if (mese == 12) return "dicembre";
        else return "Erroe";
    }

    private void startPlaying(final SeekBar progressBar, final ImageButton playPause) {
        player = new MediaPlayer();
        progressBar.setMax(player.getDuration());
        playCycle(progressBar);
        try {
            String filename;
            filename = data_list.get(actual_position).getString();
            player.setDataSource(filename);
            player.prepare();
            player.start();
        } catch (IOException e) {
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPause.setImageDrawable(context.getDrawable(R.drawable.audio_play));
            }
        });

    }


    public void playCycle(final SeekBar progressBar)
    {
       progressBar.setProgress(player.getCurrentPosition());

       if(player.isPlaying()){
           Runnable runnable = new Runnable() {
               @Override
               public void run() {
                   playCycle(progressBar);
               }
           };
           Handler handler = new Handler();
           handler.postDelayed(runnable,250);
       }
    }

    //-------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    void restorestate(Bundle in){
        data_list = (ArrayList<ModelloDati>) in.getSerializable("ADAPTER_ITEMS");
        selected_items_list = (ArrayList<ModelloDati>) in.getSerializable("ADAPTER_SELECTED_ITEMS");
        notifyDataSetChanged();
    }

    void saveState(Bundle out){
        out.putSerializable("ADAPTER_ITEMS",data_list);
        out.putSerializable("ADAPTER_SELECTED_ITEMS",selected_items_list);
    }

}






