package com.baimaisu.atest.account;

import com.baimaisu.process.CrossLiveData;
import com.baimaisu.retrofit.Projection;

public interface AccountApi {
    //    @Projection(name = "accountInfo")
    @Projection(name = 10)
    CrossLiveData<AccountInfo> getAccountInfo();
}
