package com.xiaoxintech.xiaoxinplayer.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoxintech.xiaoxinplayer.MusicService.PlayEvent;
import com.xiaoxintech.xiaoxinplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlay extends Fragment {
    PlayEvent playEvent = PlayEvent.getDefault();
    private ImageView play;
    private ImageView play_mode;
    private ImageView play_fav;
    private int play_index = 0;
    private boolean fav_status = false;
    private static android.widget.Toolbar toolbar;
    private static TextView music_duration ;
    private static SeekBar playerSeekBar;
    private static TextView music_current_pos;
    private static String musicDuration = "";
    private static int musicMaxDuration = 0;
    private static int currentPos = 0;

    public FragmentPlay() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false);
    }


}
