package it.sephiroth.android.library.mymodule.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.sephiroth.android.library.tooltip.Tooltip;

public class MainActivity3 extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static final int TOOLTIP_ID = 101;
    static final int LIST_POSITION = 5;
    private static final String TAG = "MainActivity3";
    RecyclerView mRecyclerView;
    DisplayMetrics displayMetrics;
    private Tooltip.TooltipView mCurrentTooltip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_activity3);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        List<String> array = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            array.add(String.format("Item %d", i));
        }

        displayMetrics = getResources().getDisplayMetrics();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(new MyAdapter(this, R.layout.custom_list_textview, array));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(
                    final RecyclerView recyclerView, final int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    }
                }
            });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_demo2) {
            startActivity(new Intent(this, MainActivity2.class));
        } else if (id == R.id.action_demo3) {
            startActivity(new Intent(this, MainActivity3.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        Log.d(TAG, "onItemClick: " + position);

        if (position == LIST_POSITION) {
            if (null != mCurrentTooltip) {
                mCurrentTooltip.hide();
                mCurrentTooltip = null;
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int mResId;
        private final List<String> mData;

        public MyAdapter(final Context context, final int resource, final List<String> objects) {
            super();
            setHasStableIds(true);
            mResId = resource;
            mData = objects;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            View view = LayoutInflater.from(MainActivity3.this).inflate(mResId, parent, false);
            final RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
            };
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (null != mCurrentTooltip) {
                        mCurrentTooltip.hide();
                        mCurrentTooltip = null;
                    } else {
                        showTooltip(holder);
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(mData.get(position));

            if (position == LIST_POSITION) {
                showTooltip(holder);
            }
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        private void showTooltip(final RecyclerView.ViewHolder holder) {
            if (null != mCurrentTooltip) {
                Log.w(TAG, "failed to show tooltip");
                return;
            }

            mCurrentTooltip = Tooltip.make(
                MainActivity3.this,
                new Tooltip.Builder(TOOLTIP_ID)
                    .maxWidth((int) (displayMetrics.widthPixels / 2))
                    .anchor(holder.itemView.findViewById(android.R.id.text1), Tooltip.Gravity.RIGHT)
                    .closePolicy(Tooltip.ClosePolicy.TOUCH_INSIDE_NO_CONSUME, 0)
                    .text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc lacinia sem non neque commodo consectetur")
                    .fitToScreen(false)
                    .fadeDuration(200)
                    .showDelay(50)
                    .withCallback(
                        new Tooltip.Callback() {
                            @Override
                            public void onTooltipClose(
                                final Tooltip.TooltipView v, final boolean fromUser, final boolean containsTouch) {
                                Log.w(
                                    TAG, "onTooltipClose: " + v + ", fromUser: " + fromUser + ", containsTouch: " + containsTouch);
                                mCurrentTooltip = null;
                            }

                            @Override
                            public void onTooltipFailed(Tooltip.TooltipView view) {
                            }

                            @Override
                            public void onTooltipShown(Tooltip.TooltipView view) {
                            }

                            @Override
                            public void onTooltipHidden(Tooltip.TooltipView view) {
                            }
                        })
                    .build()
            );
            mCurrentTooltip.show();
        }
    }
}
