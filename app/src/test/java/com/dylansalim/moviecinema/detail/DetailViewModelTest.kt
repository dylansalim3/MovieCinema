package com.dylansalim.moviecinema.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dylansalim.moviecinema.network.MovieApi
import com.dylansalim.moviecinema.network.MovieDetailSource
import com.dylansalim.moviecinema.testutils.MockResponseFileReader
import com.dylansalim.moviecinema.ui.detail.DetailViewModel
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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var mockWebServer = MockWebServer()

    private lateinit var detailViewModel: DetailViewModel

    @Mock
    private lateinit var apiMovieObserver: Observer<ViewState>

    @Mock
    private lateinit var movieApi: MovieApi

    @InjectMocks
    private lateinit var movieDetailSource: MovieDetailSource


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

        detailViewModel = DetailViewModel(movieDetailSource)

        detailViewModel.detailViewLoading.observeForever(apiMovieObserver)

    }

    // Test If the ViewModel changes from loading state to success state if a network request is successful
    @Test
    fun testDetailViewModelWithSuccessData() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("moviedetail_success.json").content)
        mockWebServer.enqueue(response)

        detailViewModel.getSelectedMovieById(328111)
        Mockito.verify(apiMovieObserver).onChanged(ViewState(ViewStateEnum.LOADING.value))
        Mockito.verify(apiMovieObserver).onChanged(ViewState(ViewStateEnum.SUCCESS.value))
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}