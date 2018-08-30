package com.az.cdms_mobile.Repository;

import android.app.Activity;

import com.az.cdms_mobile.Models.Fluid;
import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class TestRepository implements IRepository {

    private List<Request> data;

    private static IRepository instance;

    @Override
    public List<Request> GetRequests() {
        return data;
    }

    public Request GetById(int id){
        for(Request r: data){
            if (r.Number == id)
                return r;
        }
        return  null;
    }

    @Override
    public Maybe<List<Test>> GetSimilarTests(final Fluid fluid, final String name) {
        return  Maybe.create(new MaybeOnSubscribe<List<Test>>() {
            @Override
            public void subscribe(MaybeEmitter<List<Test>> emitter) throws Exception {
                try {
                    List<Test> result = new ArrayList<>();
                    for (Request r :data){
                        for (Test t:r.Tests){
                            if (t.Name.equals(name) && t.Fluid == "" && t.Value != null)
                                result.add(t);

                        }
                    }

                    if(result != null && !result.isEmpty()) {
                        emitter.onSuccess(result);
                    } else {
                        emitter.onComplete();
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public static Maybe<List<Request>> GetAsync(final Activity context){
        return  Maybe.create(new MaybeOnSubscribe<List<Request>>() {
            @Override
            public void subscribe(MaybeEmitter<List<Request>> emitter) throws Exception {
                try {
                    List<Request> requests = Get(context).GetRequests();
                    if(requests != null && !requests.isEmpty()) {
                        emitter.onSuccess(requests);
                    } else {
                        emitter.onComplete();
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public static IRepository Get(Activity context){
        if (instance == null)
            instance = new TestRepository(context);
        return instance;
    }

    private TestRepository(Activity context){
        data = TestRepositoryHelper.Generate(context);

    }
}
