package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.adapter.CreateGameGroupAdapter;

public class CreateGameGroupActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private CreateGameGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_group);

        ((TextView) findViewById(R.id.tv_title)).setText(R.string.create_game_group);

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CreateGameGroupAdapter();
        recycler.setAdapter(adapter);
    }
}
