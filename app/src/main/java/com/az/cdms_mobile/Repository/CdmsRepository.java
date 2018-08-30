package com.az.cdms_mobile.Repository;

import com.az.cdms_mobile.Models.Fluid;
import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;
import com.az.data_client.domain.Models.UserInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;

public class CdmsRepository {
    public List<Request> requests;
    public UserInfo userInfo;

    public CdmsRepository() {
        requests = new ArrayList<>();

    }

    public Maybe<List<Test>> GetSimilarTests(final String fluid, final String name) {
        return  Maybe.create(new MaybeOnSubscribe<List<Test>>() {
            @Override
            public void subscribe(MaybeEmitter<List<Test>> emitter) throws Exception {
                try {
                    List<Test> result = new ArrayList<>();
                    for (Request r :requests){
                        for (Test t:r.Tests){
                            if (t.Name.equals(name) && t.Fluid.equals(fluid) && t.Value != null)
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

    public Request FindById(int id){
        for (Request r:requests) {
            if (r.Id == id)
                return r;
        }
            return null;
        }

}
