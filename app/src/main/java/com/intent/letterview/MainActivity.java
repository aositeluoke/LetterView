package com.intent.letterview;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FragmentPagerAdapter mPagerAdaper;
    private LetterView letterView;
    private RecyclerView mRecyclerView;
    String word = "perfect";
    private String mKeyString = "abcdefghijklmnopqrstuvwxyz";
    private RecyclerView.Adapter mAdapter;
    private List<LetterDto> mData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);
        letterView = findViewById(R.id.letterView);
        findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letterView.del();
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerView.Adapter<ViewHolder>() {

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_letter, null, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                String letter = mData.get(position).getLetter() + "";
                holder.tvLetter.setText(letter);
                holder.itemView.setTag(letter);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String letter = view.getTag() + "";
                        letterView.append(letter.charAt(0));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mRecyclerView.setAdapter(mAdapter);
        letterView.initWord(word);
        char[] keyArray = mKeyString.toCharArray();
        for (char c : keyArray) {
            mData.add(new LetterDto(c));
        }
        mAdapter.notifyDataSetChanged();

    }



    public class LetterDto {
        public Character letter;

        public LetterDto(Character letter) {
            this.letter = letter;
        }

        public Character getLetter() {
            return letter;
        }

        public void setLetter(Character letter) {
            this.letter = letter;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLetter;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLetter = itemView.findViewById(R.id.tv_letter);
        }
    }
}
