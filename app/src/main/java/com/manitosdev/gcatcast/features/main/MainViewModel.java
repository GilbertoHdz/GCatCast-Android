package com.manitosdev.gcatcast.features.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.manitosdev.gcatcast.api.models.ApiResult;
import com.manitosdev.gcatcast.api.models.search.RssFeed;
import com.manitosdev.gcatcast.api.models.search.SearchResult;
import com.manitosdev.gcatcast.api.repository.ItunesRepository;
import com.manitosdev.gcatcast.db.AppDatabase;
import com.manitosdev.gcatcast.db.entities.PlaylistEntity;
import com.manitosdev.gcatcast.db.entities.PodCastEntity;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    @NonNull
    private ItunesRepository repository = ItunesRepository.getInstance();

    public MainViewModel(@NonNull Application application) {
        super(application);
        searchResultMutableLiveData = repository.getSearchResultMutableLiveData();
        rssFeedMutableLiveData = repository.getRssFeedMutableLiveData();
    }

    @NonNull
    private LiveData<ApiResult<SearchResult>> searchResultMutableLiveData;
    public LiveData<ApiResult<SearchResult>> getSearchResultMutableLiveData() {
        return searchResultMutableLiveData;
    }

    public LiveData<List<PodCastEntity>> loadSavedPodCasts() {
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        return db.podCastDao().loadSavedPodCasts();
    }

    public LiveData<List<PlaylistEntity>> loadSavedPlaylist() {
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        return db.playlistDao().loadSavedPlaylists();
    }

    @NonNull
    private LiveData<ApiResult<RssFeed>> rssFeedMutableLiveData;
    public LiveData<ApiResult<RssFeed>> getRssFeedMutableLiveData() {
        return rssFeedMutableLiveData;
    }

    public void loadRssFeedsByPodcast(String feedUrl) {
        repository.loadFeedFromRss(feedUrl);
    }
}