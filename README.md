### Veer Kakar Penn Labs Android Application

# Food Truck List
- The interal data was stores used a `ViewModel` with a `MutableStateFlow` of `FoodTruck` data class objects. This is done for easy sorting by name and rating.
- The content is stored using a `LazyList` in the frontend which stores basic information, and each item can be clicked to show a more detailed information page, with all relevant stored data.

# Google Maps
- The API key for this is empty in the github for security reasons. Please paste in a valid API key (one which I have sent over) into ANdroidManifest.xml
- This works in another package [com.example.pennlabsapplication.googlemaps], which has all the necessary data.
- The map is embedded in the page as a `FragmentActivity` and is contained with a `FragmentContainerView`. This is loaded once when first switching to this tab.
- The map has location markers for all food truck locations inputted through `res/raw/places.json`. Clicking on these shows name and star rating.