package choo.edeline.foodrng;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String CHOSEN_ITEM = "item";
    DatabaseHelper mDatabaseHelper;

    private ImageView RNGenerator;
    private TextView item;
    private Button mViewList;
    private String selectedItem = "";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getExtras().getString("username");

        getSupportActionBar().setTitle("Food RNG");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewList = (Button)findViewById(R.id.btnViewList);
        mViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        item = (TextView)findViewById(R.id.tvRNG);
        mDatabaseHelper = new DatabaseHelper(this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.soundeffect);

        RNGenerator = (ImageView)findViewById(R.id.ivRNG);
        RNGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat
                        (RNGenerator, "rotation", 0f, 360f);
                rotateAnimation.setDuration(1000);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(rotateAnimation);
                animatorSet.start();
                mediaPlayer.start();

                Cursor data = mDatabaseHelper.getItemData(username);
                ArrayList<String> itemList = new ArrayList<>();
                while(data.moveToNext()) {
                    //get the value from the database in column 1
                    //then add it to the ArrayList
                    itemList.add(data.getString(0));
                }
                if (itemList.isEmpty() == true) {
                    selectedItem = "Oops! Your list is empty.";
                    item.setText(selectedItem);
                }
                else {
                    Collections.shuffle(itemList);
                    selectedItem = "Today's pick is: " + itemList.get(0);
                    item.setText(selectedItem);
                }
            }
        });

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getString(CHOSEN_ITEM, "");
            item.setText(selectedItem);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(CHOSEN_ITEM, selectedItem);
    }
}
