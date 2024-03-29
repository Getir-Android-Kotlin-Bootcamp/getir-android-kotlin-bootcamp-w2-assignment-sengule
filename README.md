# Getir Homemork Ertugrul Sengul week 2

## Features

### Managing Google Map

When user click to google map, select a place or first starting of  app, Google map change such as adding marker, animate to selected place etc.\

Params\
map : Google map that managing on it.\
latLng: LatLng object where action perform in google map.\
markerTitle: Marker name when user put marker on google map.\
markerIcon: Marker icon where user put marker on google map.\
shouldAnimated: Check if animation is performed when user select a place. False as default

```bash
manageGoogleMap(
        map:GoogleMap,
        latLng: LatLng,
        markerTitle: String = "My Location",
        markerIcon: Int = R.drawable.ic_marker,
        shouldAnimated: Boolean = false
)
    
```
### Initialize Auto Complete

Initializing of Auto Complete Fragment.

Params\
onPlaceSelected: Callback when user select a place. Also Place object given to that callback.
```bash
initAutocompleteFragment(
        onPlaceSelected: (Place) -> Unit
)

```

### Registering To Permission Launcher

Registers the permission launcher so launcher can be used with .launch() API.

Params\
 onPermissionDenied: Callback when user deny that permission.\
 onPermissionGranted: Callback when user grant that permission
```bash
registerLauncher(
        onPermissionDenied: () -> Unit = {},
        onPermissionGranted: () -> Unit = {}
)

```

### Getting User Current Location

Gets the User current location with using FusedLocationProviderClient object .\

Params\
 onSuccess: Callback when getting user possition. It has also give LatLng parameter.
```bash
getUserLocation(
        onSuccess: (LatLng) -> Unit = {}
)

```
## Common File
Common File added to project to gather all constants and static functions in one piece.

### DEFAULT_ZOOM_VALUE
It is a zoom float value when maps opened and in configuration\
```bash
const val DEFAULT_ZOOM_VALUE: Float = 13.0f
```

### Generating Bitmap Descriptor
For generating Bitmap Descriptor from resource file\

```bash
fun generateBitmapDescriptorFromRes(
    context: Context?, resId: Int,
)
```
