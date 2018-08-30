package com.az.cdms_mobile.Domain;

import android.content.Context;
import android.databinding.DataBindingUtil;

import com.az.cdms_mobile.Models.Test;
import com.az.cdms_mobile.databinding.CardLayoutBinding;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.R;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.Tests.DataTest;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RequestDataAdapter extends RecyclerView.Adapter<RequestDataAdapter.ViewHolder> {
    private Context context;
    List<Request> requests;
    private LayoutInflater layoutInflater;
    private RequestClickListener listener;
    public interface RequestClickListener {
        void onItemClick(Request r);
    }
    public RequestDataAdapter(List<Request> requests, Context context) {
        this.requests = requests;
        this.context = context;
        this.listener = (RequestClickListener) context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {



        private final CardLayoutBinding binding;

        public ViewHolder(final CardLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

    }

    @Override
    public RequestDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CardLayoutBinding binding =  DataBindingUtil.inflate(layoutInflater, R.layout.card_layout, parent, false);

        return new RequestDataAdapter.ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(RequestDataAdapter.ViewHolder holder,final int position) {
        Request e = requests.get(position);
        holder.binding.setRequest(e);

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(requests.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void Update (List<Request> newRequests){

        updateRequests(newRequests);

        notifyDataSetChanged();

    }

    private void updateRequests(List<Request> data) {


        for (Request r : data) {
            Request similarRequest = findRequest(r);

            if (similarRequest ==null){
                similarRequest = new Request();
                similarRequest.FluidDate = r.FluidDate;
                similarRequest.FluidType = r.FluidType;
                similarRequest.Id = r.Id;
                similarRequest.Tests = new ArrayList<>();
                requests.add(similarRequest);
            }

            for (Test t : r.Tests) {
                Test sourceTest = findTest(t);
                if (sourceTest != null) {
                    updateTest(t, sourceTest);
                    final Request sourceRequest = getTestRequest(sourceTest);
                    if (sourceRequest != similarRequest) {
                        sourceRequest.Tests.remove(sourceTest);
                        similarRequest.Tests.add(sourceTest);

                    } else {

                    }
                } else {
                    sourceTest = new Test();
                    updateTest(t, sourceTest);
                    similarRequest.Tests.add(sourceTest);

                }

            }


        }
        cleanEmptyRequests();

    }


    private void updateTest(Test t, Test sourceTest) {
        sourceTest.Fluid = t.Fluid;
        sourceTest.FluidDate = t.FluidDate;
        sourceTest.LowerLimit = t.LowerLimit;
        sourceTest.UpperLimit = t.UpperLimit;
        sourceTest.Name = t.Name;
        sourceTest.ReadyDate = t.ReadyDate;

        sourceTest.Unit = t.Unit;
        sourceTest.Value = t.Value;
        sourceTest.Id = t.Id;
    }

    private Request findRequest(Request request) {
        for (Request r : requests) {
            if (requestsEqual(request, r))
                return r;
        }
        return null;
    }

    private boolean requestsEqual(Request sourceRequest, Request r) {
        if (!r.FluidType.equals(sourceRequest.FluidType))
            return false;
        if (r.Id != sourceRequest.Id)
            return false;
        return true;
    }

    private Request getTestRequest(Test sourceTest) {
        for (Request r : requests) {
            for (Test test : r.Tests) {
                if (test.Id == sourceTest.Id)
                    return r;

            }
        }
        return null;
    }

    private Test findTest(Test t) {
        for (Request r : requests) {
            for (Test test : r.Tests) {
                if (test.Id == t.Id)
                    return test;

            }
        }
        return null;
    }
    private void cleanEmptyRequests() {
        for (int i=0;i<requests.size();i++){
            if (requests.get(i).Tests.size()==0) {
                requests.remove(i);
                i--;
            }
        }
    }
}
