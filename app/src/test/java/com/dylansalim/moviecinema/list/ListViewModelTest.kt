package com.dylansalim.moviecinema.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dylansalim.moviecinema.network.MovieApi
import com.dylansalim.moviecinema.network.MovieListSource
import com.dylansalim.moviecinema.testutils.MockResponseFileReader
import com.dylansalim.moviecinema.ui.list.ListViewModel
import com.dylansalim.moviecinema.utils.ViewState
import com.dylansalim.moviecinema.utils.ViewStateEnum
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection


@RunWith(MockitoJUnitRunner::class)
class ListViewModelTest {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var mockWebServer = MockWebServer()

    private lateinit var listViewModel: ListViewModel

    @Mock
    private lateinit var apiMovieObserver: Observer<ViewState>

    @Mock
    private lateinit var movieApi: MovieApi

    @InjectMocks
    private lateinit var movieListSource: MovieListSource


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockWebServer.start()
        movieApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient())
            .build()
            .create(MovieApi::class.java)

        listViewModel = ListViewModel(movieListSource)

        listViewModel.listViewLoading.observeForever(apiMovieObserver)

    }

    // Test If the ViewModel changes from loading state to success state if a network request is successful
    @Test
    fun testListViewModelWithSuccessData() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("movielist_success.json").content)
        mockWebServer.enqueue(response)

        listViewModel.getMovieList(1, "release_date.asc")
        verify(apiMovieObserver).onChanged(ViewState(ViewStateEnum.LOADING.value))
        verify(apiMovieObserver).onChanged(ViewState(ViewStateEnum.SUCCESS.value))
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}