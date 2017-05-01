package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Created by saeid on 3/11/2017.
 */
public interface IOnOffloadService {
    List<OnOffLoadModel> getReadingList(int type);
    List<OnOffLoadModel> getNeedToBeOffloadedOverall(int configIndex);

    /**
     * get all OnOffloadModel list of a route(readingConfig) ,
     * including active or inActive
     * @param trackNumber
     * @return List<OnOffLoadModel>
     */
    List<OnOffLoadModel> get(BigDecimal trackNumber);

    /**
     * get OnOffloadModel list in specific 'offloadState' of a route(readingConfig) ,
     * including active or inActive
     * @param configIndex
     * @param offloadState
     * @return List<OnOffLoadModel>
     */
    List<OnOffLoadModel> get(int configIndex,Integer offloadState);

    /**
     * <h1>get list by 'offloadState'</h1>
     * <p>int offloadState is of type 'OffloadState' constant only class
     * in models package</p>
     * @param offloadState
     * @return List<OnOffLoadModel></>
     */
     List<OnOffLoadModel> get(int offloadState);

    void changeOffloadState(Collection<OnOffLoadModel> onOffloadList, int offloadState);
}
