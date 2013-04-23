# Clip Share Android App

### MyActivity, FilterParameters and ShortenLink
This app is primarily composed of these three classes doing the bulk of the work.

* ShortenLink runs in the background asynchronously when the Shorten button is clicked.
* FilterParameters is accessed by hitting Filter when query parameters are present. Pressing finish in this screen will return you to the main activity.
* MyActivity is the primary hub of this app, all activities lead from this one and, excepting Copy to Clipboard, return here as well.