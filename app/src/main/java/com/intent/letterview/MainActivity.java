package com.intent.letterview;

import android.os.Bundle;
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
    private LetterView mLetterView;
    private RecyclerView mRecyclerView;
    private  String mText = "perfect";
    private String mKeyString = "abcdefghijklmnopqrstuvwxyz";
    private RecyclerView.Adapter mAdapter;
    private List<Character> mData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLetterView = findViewById(R.id.letterView);
        findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLetterView.del();
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
                String letter = mData.get(position) + "";
                holder.tvLetter.setText(letter);
                holder.itemView.setTag(letter);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String letter = view.getTag() + "";
                        mLetterView.append(letter.charAt(0));
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
        mLetterView.initWord(mText);
        char[] keyArray = mKeyString.toCharArray();
        for (char c : keyArray) {
            mData.add(c);
        }
        mAdapter.notifyDataSetChanged();

    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLetter;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLetter = itemView.findViewById(R.id.tv_letter);
        }
    }
}
