package com.az.data_client.api.Mappers;

import com.az.data_client.api.Models.RequestApi;
import com.az.data_client.api.Models.TestApi;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.Tests.DataTest;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.Tests.DataTest;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;



public  class RequestMapper {
    public static List<DataRequest> Map(List<RequestApi> responce){

        List<DataRequest> result = new ArrayList<>();
        if (responce == null)
            return result;
        for (RequestApi r : responce){
            DataRequest request = new DataRequest();
            request.Id = r.id;
            request.FluidDate = parseDate(r.fluidDate);
            request.FluidType = r.fluidType;
            request.Number = r.number;
            request.ReadyDate = parseDate(r.readyDate);
            request.Tests = new ArrayList<>();
            if (r.tests != null)
                for (TestApi t :r.tests){
                    DataTest test = new DataTest();
                    test.Fluid = t.fluid;
                    test.FluidDate = parseDate(t.fluidDate);
                    if (t.referenceLimits != null){
                        if (t.referenceLimits.lowLimit != null)
                            test.LowerLimit = new Double(t.referenceLimits.lowLimit);
                        if (t.referenceLimits.highLimit != null)
                            test.UpperLimit = new Double(t.referenceLimits.highLimit);
                    }
                    test.Name = t.name;
                    test.ReadyDate = parseDate(t.readyDate);
                    test.Unit = t.unit;
                    test.Id = t.id;
                    //fluid type??
                    if (t.value != null && t.value != "")
                        try {
                            test.Value = Double.parseDouble(t.value);
                        } catch (Exception ex){

                    }
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
