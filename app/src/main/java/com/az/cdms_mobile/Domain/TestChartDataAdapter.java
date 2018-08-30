package com.az.cdms_mobile.Domain;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;
import com.az.cdms_mobile.R;
import com.az.cdms_mobile.databinding.TestChartLayoutBinding;

import java.util.List;

public class TestChartDataAdapter extends RecyclerView.Adapter<TestChartDataAdapter.ViewHolder> {
    private Context context;
    List<Test> tests;
    private LayoutInflater layoutInflater;
    private TestChartDataAdapter.TestClickListener listener;
    public interface TestClickListener {
        void onItemClick(Request r);
    }
    public TestChartDataAdapter(List<Test> tests, Context context) {
        this.tests = tests;
        this.context = context;
        this.listener = (TestChartDataAdapter.TestClickListener) context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {



        private final TestChartLayoutBinding binding;

        public ViewHolder(final TestChartLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

    }

    @Override
    public TestChartDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TestChartLayoutBinding binding =  DataBindingUtil.inflate(layoutInflater, R.layout.test_chart_layout, parent, false);

        return new TestChartDataAdapter.ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(TestChartDataAdapter.ViewHolder holder, int position) {
        Test e = tests.get(position);
        holder.binding.setTest(e);
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }
}
