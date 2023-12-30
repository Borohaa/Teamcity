package com.example.teamcity;

import com.example.teamcity.api.generators.TestData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.request.CheckedRequests;
import com.example.teamcity.api.request.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseApiTest extends BaseTest{
    public TestDataStorage testDataStorage;
    public CheckedRequests checkedWithSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    public UncheckedRequests uncheckedWithSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());

    @BeforeMethod
    public void setupTest(){
         testDataStorage = TestDataStorage.getStorage();
    }

    @AfterMethod
    public void cleanTest(){
        testDataStorage.delete();
    }
}
