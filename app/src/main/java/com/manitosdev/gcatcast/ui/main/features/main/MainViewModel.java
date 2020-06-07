package com.manitosdev.gcatcast.ui.main.features.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.manitosdev.gcatcast.ui.main.api.models.ApiResult;
import com.manitosdev.gcatcast.ui.main.api.models.search.SearchResult;
import com.manitosdev.gcatcast.ui.main.api.repository.ItunesRepository;

public class MainViewModel extends AndroidViewModel {

    @NonNull
    private ItunesRepository repository = ItunesRepository.getInstance();

    public MainViewModel(@NonNull Application application) {
        super(application);
        searchResultMutableLiveData = repository.getSearchResultMutableLiveData();
    }

    @NonNull
    private LiveData<ApiResult<SearchResult>> searchResultMutableLiveData;

    public LiveData<ApiResult<SearchResult>> getSearchResultMutableLiveData() {
        return searchResultMutableLiveData;
    }
}