package com.az.cdms_mobile.Repository;

import com.az.cdms_mobile.Models.Fluid;
import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;

import java.util.List;

import io.reactivex.Maybe;

public interface IRepository {
    public List<Request> GetRequests();
    public Request GetById (int id);
    public Maybe<List<Test>> GetSimilarTests(Fluid fluid, String name);
}
