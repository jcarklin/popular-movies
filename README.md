# Movie Browser

Movie Browser is an app which simply retrieves the current top 20 movies from TheMovieDb, sorted by either popularity or rating. Details for selected movie can then be viewed. The app was created and submitted as an assignment for the The Android Developer Nanodegree program at Udacity

### Prerequisites

An API key from TheMovieDb is required (see https://www.themoviedb.org/faq/api)

Once you have your API key, you need to add it to the gradle.properties file in your home .gradle directory.

Add the following line
```
TMDBApiToken = "1234567890"
```
to ~/.gradle/gradle.properties in Linux
or C:\Users\\[Username]\\.gradle in Windows (for example)
where _"1234567890"_ is replaced with your actual API Key

You will not be able to build the app until this is done. 

### Acknowledgments

The following libraries were used:
* Butter Knife (http://jakewharton.github.io/butterknife/)
* Picasso (http://square.github.io/picasso/)

Movie data is supplied by The Movie Db (https://www.themoviedb.org/)


