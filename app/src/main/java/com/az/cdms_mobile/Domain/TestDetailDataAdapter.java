package com.az.cdms_mobile.Domain;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;
import com.az.cdms_mobile.R;
import com.az.cdms_mobile.databinding.CardLayoutBinding;
import com.az.cdms_mobile.databinding.TestDetailLayoutBinding;

import java.util.List;

public class TestDetailDataAdapter extends RecyclerView.Adapter<TestDetailDataAdapter.ViewHolder> {
    private Context context;
    List<Test> tests;
    private LayoutInflater layoutInflater;
    private TestClickListener listener;
    public interface TestClickListener {
        void onItemClick(Test t);
    }
    public TestDetailDataAdapter(List<Test> tests, Context context) {
        this.tests = tests;
        this.context = context;
        this.listener = (TestClickListener) context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {



        private final TestDetailLayoutBinding binding;

        public ViewHolder(final TestDetailLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

    }

    @Override
    public TestDetailDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TestDetailLayoutBinding binding =  DataBindingUtil.inflate(layoutInflater, R.layout.test_detail_layout, parent, false);

        return new TestDetailDataAdapter.ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(TestDetailDataAdapter.ViewHolder holder, int position) {
        final Test e = tests.get(position);
        holder.binding.setTest(e);
        holder.binding.testDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }
}
