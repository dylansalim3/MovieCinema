# MovieCinema - Dylan Salim
## Description
A Movie Database application that retrieve movies metadata from [TMDb](https://www.themoviedb.org/). This application allows user to filter and view selected movies details. 

## Usage Guide
1. Create TMDb API Key [here](https://www.themoviedb.org/)
2. Replace your API Key in the utils/Constants.kt
```
    const val API_KEY="TMDB_API_KEY"
```
## Assumptions
* The application allows anyone to access the list of movies, no login required
* The application is a movie listing application that allows users to view list of movies, detail of individual movie and book the movie
* Book movie button will open the Cathay Cinema website in a web view of the the application

## Technical Details
| Application | Android |
| ----------- | ----------- |
| Programming Language | Kotlin |
| Asynchronous Library | Coroutines |
| Software Architectural Pattern | MVVM |
| Dependency Injection | Dagger2 |
| REST Client | Retrofit2 |
| JSON Parsing | Gson |
| HTTP Client | OkHttp3 |
| Image Caching | Glide |
| Unit Testing | JUnit4 & Mockito |
| | Data Binding |
| | Android Navigation Component |

## Design Consideration
## UI Design
### Home Screen
* Drawing user attention is crucial, especially for E-Commerce application, therefore a **Grid Layout** is used on the home screen
* As compared to List Layout, a Grid Layout is more engaging to the users because it have more images and each image will get equal amount of attentions by the users
* Human are tends to more attracted by pictures rather than words

<p align="center">
    <img src="https://i.imgur.com/EKg3qsO.png" alt="home_screen_ui" height="400"/>
</p>

**Bottom Navigation Dialog**
* Bottom Navigation Dialog is used for order criteria selection rather than a traditional confirmation dialog.
* In a default multi-selection confirmation dialog, a positive and negative action buttons will be defined at the bottom of the dialog. The user must either confirm a choice or dismiss the dialog before proceeding. The chances of accidentally dismiss a selected dialog is high (user might accidentally click the dismiss button when they want to confirm the selection) and it can be troublesome for some who are coming from an IOS background. In contrast, the default behavior of a Bottom Navigation Dialog does not require user to confirm the selection, the selected option will be submitted automatically and the dialog will close after user have select their option.
* In short, it helps to simplify the order criteria selection process.

<p align="center">
    <img src="https://i.imgur.com/p1nlOE0.png" alt="bottom_navigation_dialog" height="400"/>
</p>

### Detail Screen
* Movie banner is shown on top, all the details of the movie is listed below and a "Book Movie" button is placed at the center bottom of the screen.

<p align="center">
    <img src="https://i.imgur.com/zV21QoS.png" alt="detail_screen_ui" height="400"/>
</p>

### Web View Screen
* A toolbar with a back button to allow users to navigate back to the previous screen.

<p align="center">
    <img src="https://i.imgur.com/zuXoEme.png" alt="webview_screen_ui" height="400"/>
</p>

### Splash Screen 
<p align="center">
    <img src="https://i.imgur.com/sGTwKL6.png" alt="splash_screen_ui" height="400"/>
</p>

**Font Used**
- Roboto Slab

## Project Structure Design
**adapters**
- Containing all the adapters used, including the binding adapters and recycler view adapters

**dagger**
- Containing all the dagger related classes which includes the application class
- Define dependency injection for Network and ViewModel Module
- Inject dependencies to the main application

**models**
- Containing Kotlin data classes

**network**
- Containing all the networking related interface/class
- MovieApi - A retrofit interface that specify the network request path, path and query variable
- MovieNetworkSource - Define the coroutines that is used to retrieve asynchronous data from the API 

**ui**
- Containing all the UI classes and ViewModel classes.

**utils**
- Containing all the utility classes that are used by the application
- Event class - Alternative to SingleLiveEvent, Single Live Event won't allow multiple observer but this Event class support multiple observer
- ViewState class - Define the view state of the presenter (LOADING/SUCCESS/FAILED)

**Screenshot of the project structure**
<img src="https://i.imgur.com/rUecy1K.png" alt="splash_screen_ui" height="400"/>


## Navigation Design
**Android Navigation Library** is used to manage the navigation between screens. As the application grows, the navigation between different views will be more complex, therefore having such mechanism to manage navigation, transition animation, deep-linking and compile-time checked argument passing between fragments will improve the code readability and maintainability.

The flow of the activities in this application:
SplashActivity -> MainActivity

The flow of the fragments in the MainActivity:
ListFragment -> DetailFragment -> WebViewFragment

<p align="center">
    <img src="https://i.imgur.com/jy1xC23.png" alt="android_navigation_graph" height="400"/>
</p>

## Unit Test
- Conducted Unit Testing using JUnit4 and Mockito with MockWebServer to mock API response
- Local JSON data file is used to mock API request
- Main test scope: Test whether the ViewState for ListViewModel and DetailViewModel class changes from LOADING to SUCCESS state after an API request
- Test classes: ListViewModelTest & DetailViewModelTest

<p align="center">
    <img src="https://i.imgur.com/FQ55s62.png" alt="unit-test-project-structure" height="400"/>
</p>