package com.example.monefy.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.monefy.DataBase;
import com.example.monefy.R;
import com.example.monefy.interfacee.ViewPagerHelp;
import com.example.monefy.view_pager_adapters.DaysAdapter;
import com.example.monefy.view_pager_adapters.MonthAdapter;
import com.example.monefy.view_pager_adapters.WeeksAdapter;
import com.example.monefy.view_pager_adapters.YearsAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Statistics extends Fragment {


    ViewPagerHelp viewPagerHelp;
    public String mode = ViewPagerHelp.ALL_MODE;
    DaysAdapter daysAdapter;
    WeeksAdapter weeksAdapter;
    MonthAdapter monthAdapter;
    YearsAdapter yearsAdapter;


    @SuppressLint({"WrongConstant", "SetTextI18n", "NewApi"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        RadioGroup modeMoney = root.findViewById(R.id.toggle);
        RadioGroup modeDays = root.findViewById(R.id.mode);
        ViewPager viewPager = root.findViewById(R.id.viewpager);
        TextView textView = root.findViewById(R.id.countpgs);
        Button next = root.findViewById(R.id.next);
        Button previous = root.findViewById(R.id.previous);
        BlockingQueue<String> blockingQueue = new PriorityBlockingQueue<>();
        ProgressBar progressBar = root.findViewById(R.id.progress_horizontal);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                daysAdapter = new DaysAdapter(getChildFragmentManager());
                weeksAdapter = new WeeksAdapter(getChildFragmentManager());
                monthAdapter = new MonthAdapter(getChildFragmentManager());
                yearsAdapter = new YearsAdapter(getChildFragmentManager());
                blockingQueue.add("ok");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    blockingQueue.take();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressBar.setVisibility(-1);
                            viewPager.setAdapter(daysAdapter);
                            viewPagerHelp = daysAdapter;
                            textView.setText(getActivity().getResources().getString(R.string.page) + (viewPager.getCurrentItem() + 1) + "/" + viewPager.getAdapter().getCount());
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        modeDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (DataBase.getInstance().getData().isEmpty()) {
                    return;
                }
                switch (checkedId) {

                    case R.id.day:
                        daysAdapter.mode = mode;
                        viewPager.setAdapter(daysAdapter);
                        viewPagerHelp = daysAdapter;
                        break;
                    case R.id.week:
                        weeksAdapter.mode = mode;
                        viewPager.setAdapter(weeksAdapter);
                        viewPagerHelp = weeksAdapter;
                        break;
                    case R.id.month:
                        monthAdapter.mode = mode;
                        viewPager.setAdapter(monthAdapter);
                        viewPagerHelp = monthAdapter;
                        break;
                    case R.id.year:
                        yearsAdapter.mode = mode;
                        viewPager.setAdapter(yearsAdapter);
                        viewPagerHelp = yearsAdapter;
                        break;
                }
            }
        });

        modeMoney.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.search:
                        mode = ViewPagerHelp.COST_MODE;
                        viewPagerHelp.changeMode(ViewPagerHelp.COST_MODE, true);
                        break;

                    case R.id.offer:
                        mode = ViewPagerHelp.PROFIT_MODE;
                        viewPagerHelp.changeMode(ViewPagerHelp.PROFIT_MODE, true);
                        break;
                    case R.id.allstat:
                        mode = ViewPagerHelp.ALL_MODE;
                        viewPagerHelp.changeMode(ViewPagerHelp.ALL_MODE, true);
                        break;
                }
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                textView.setText(getActivity().getResources().getString(R.string.page) + (viewPager.getCurrentItem() + 1) + "/" + viewPager.getAdapter().getCount());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });
        return root;
    }
}
