package com.az.cdms_mobile.Domain.Mappers;

import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;
import com.az.data_client.api.Models.RequestApi;
import com.az.data_client.api.Models.TestApi;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.Tests.DataTest;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


public  class VmRequestMapper {
    public static List<Request> Map(List<DataRequest> responce){

        List<Request> result = new ArrayList<>();
        if (responce == null)
            return result;
        for (DataRequest r : responce){
            Request request = new Request();
            request.Id = r.Id;
            request.FluidDate = new DateTime(r.FluidDate);
            request.FluidType = r.FluidType;
            request.Number = r.Number;
            request.ReadyDate = new DateTime(r.ReadyDate);
            request.Tests = new ArrayList<>();
            if (r.Tests != null)
                for (DataTest t :r.Tests){
                    Test test = new Test();
                    test.Fluid = t.Fluid;
                    test.FluidDate = new DateTime(t.FluidDate);
                    test.UpperLimit = t.UpperLimit;
                    test.LowerLimit = t.LowerLimit;
                    test.Name = t.Name;
                    test.ReadyDate = new DateTime(t.ReadyDate);
                    test.Unit = t.Unit;
                    test.Id = t.Id;
                    test.Value = t.Value;
                    //fluid type??

                    request.Tests.add(test);


                }
                result.add(request);

        }
        return  result;
    }
    private static DateTime parseDate(String dateStr){
        if (dateStr == null || dateStr == "")
            return null;
        DateTime result = DateTime.parse(dateStr);
        if (result.getMillis()<0)
            return null;
        return result;
    }


}
