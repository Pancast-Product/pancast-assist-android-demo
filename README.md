# Pancast Assist for Android

Pancast Assist is an Android SDK for reporting media player events to Snowplow Analytics.

## Getting Started

The best way to get started using Pancast Assist is to add it as a gradle dependency. You need to make sure you have the maven jitpack repository included in the `build.gradle` file in the root of your project:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Next add a dependency in the `build.gradle` file of your app module:

```gradle
implementation 'com.github.Pancast-Product:pancast-assist-android-sdk:v1.0.0'
```

## Starting a PancastAssist session

To start using Pancast Assist, simply create a `PancastAssist(Context context, String appId, String uri)` instance. You should only create one instance of PancastAssist to use for all video and advert events.

```kotlin
val pancastAssist = PancastAssist(
    this,
    "example-app-id",
    "exmaple.uri.com"
)
```

## Sending events

You can send Video and Ad events using `PancastAssist.sendVideoEvent` and `PancastAssist.sendAdEvent`. Where possible you should also include the media context.

### Video Events

`PancastAssist.sendVideoEvent(PAVideoEvent event)`

`PancastAssist.sendVideoEvent(PAVideoEvent event, PAVideo mediaContext)`

Example:

```kotlin
pancastAssist.sendVideoEvent(videoEvent, videoMeta)
```

### Ad Events

`PancastAssist.sendAdEvent(PAAdEvent event)`

`PancastAssist.sendAdEvent(PAAdEvent event, PAAd mediaContext)`

Example:

```kotlin
pancastAssist.sendAdEvent(adEvent, adMeta)
```

## Events

### PAVideoEvent

```kotlin
val videoEvent = PAVideoEvent().apply {
    vid = videoMeta.id
    pid = "player id"
    action = PAVideoAction.Play
    ctime = player.currentPosition.toDouble()
    pp = player.currentPosition.toDouble() / player.duration.toDouble()
    vd = player.duration.toDouble()
}
```

| Property | Type          | Required | Description                                                                                                                                                                         |
| -------- | ------------- | -------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| vid      | String        | Required | Identifier the content of the video the action is for                                                                                                                               |
| pid      | String        | Required | Identifier for the player the video is playing in                                                                                                                                   |
| action   | PAVideoAction | Required | The action that occurred                                                                                                                                                            |
| ctime    | double        | Optional | The number of seconds into the video when the action occurred (its current time)                                                                                                    |
| vtime    | double        | Optional | The cumulative number of seconds spent viewing the video by the time the action occurred. Includes time spent buffering and viewing ads, but not time spent paused or scrubbed past |
| pp       | double        | Optional | The percentage of progress milestone the video is at when the progress/timing event occurred                                                                                        |
| vd       | double        | Optional | The total number of seconds the video lasts for                                                                                                                                     |
| pc       | String        | Optional | The player error information for the error that occurred                                                                                                                            |

### PAAdEvent

```kotlin
val adEvent = PAAdEvent().apply {
    pid = "player id"
    vid = videoMeta.id
    action = PAAdAction.Play
    ctime = player.currentPosition.toDouble()
}
```

| Property | Type       | Required | Description                                                                      |
| -------- | ---------- | -------- | -------------------------------------------------------------------------------- |
| pid      | String     | Required | Identifier for the player the ad is playing in                                   |
| vid      | String     | Optional | Identifier for the video the ad is playing amidst                                |
| action   | PAAdAction | Required | The action that occurred                                                         |
| ctime    | double     | Optional | The number of seconds into the video when the action occurred (its current time) |

## Actions

### PAVideoAction

| Type           | Description                            |
| -------------- | -------------------------------------- |
| Start          | Video started                          |
| End            | Video ended                            |
| Pause          | Video paused                           |
| Play           | Video played                           |
| Progress       | Video progress updated                 |
| VolumeChange   | Video volume changed                   |
| Fullscreen     | Video entered fullscreen mode          |
| Change         | Player changed to another media source |
| Error          | Video error during playback            |
| QualityChange  | Video quality changed                  |
| LoadedMetadata | Video metadata loaded                  |
| Unknown        | Unknown video action                   |

### PAAdAction

| Type     | Description                         |
| -------- | ----------------------------------- |
| Change   | Advertisement changed               |
| Start    | Advertisement started               |
| End      | Advertisement ended                 |
| Play     | Advertisement played                |
| Pause    | Advertisement paused                |
| Progress | Advertisement progress updated      |
| Error    | Advertisement error during playback |

## Meta Context

### PAVideo

```kotlin
val videoMeta = PAVideo().apply {
    id = "1234"
    title = "Tears"
    duration = 99.0
    owner = "owner id"
    channel = "channel id"
    tags = listOf("tag1", "tag2")
    country = "AU"
    language = "en"
    mode = "vod"
}
```

| Property | Type           | Required | Description                                               |
| -------- | -------------- | -------- | --------------------------------------------------------- |
| id       | String         | Required | The identifier of the video                               |
| title    | String         | Optional | The title of the video                                    |
| duration | double         | Optional | The number in seconds that the video goes for             |
| owner    | String         | Optional | An identifier for the user who owns the video             |
| channel  | String         | Optional | An identifier for the channel the video belongs to        |
| tags     | List of String | Optional | Tags for labelling the video, Example: ["bunny", "teeth"] |
| country  | String         | Optional | Country the video originates from, Example: "AU"          |
| language | String         | Optional | Language of the video content, Example: "en"              |
| mode     | String         | Optional | The playback mode of the video, Example: "vod"            |

### PAAd

```kotlin
val adMeta = PAAd().apply {
    id = "ad id"
    title = "ad title"
    system = "ad system"
    advertiser = "advertiser name"
    creative_id = "creative id"
    description = "ad description"
    duration = 10.0
    type = "linear"
    universal_ad_id = "universal ad id"
    viewable = true
}
```

| Property        | Type    | Required | Description                                                                         |
| --------------- | ------- | -------- | ----------------------------------------------------------------------------------- |
| id              | String  | Optional | The identifier of the advertisement                                                 |
| serve_id        | String  | Optional | An identifier for the specific advertisement impression this event relates to       |
| title           | String  | Optional | The title of the advertisement                                                      |
| system          | String  | Optional | The system that served the advertisement, Example: "SpotXchange"                    |
| advertiser      | String  | Optional | The name of the advertiser buying the advertisement                                 |
| advertiser_id   | String  | Optional | The identifier of the advertiser buying the advertisement                           |
| creative_id     | String  | Optional | The identifier of the advertisement's creative                                      |
| description     | String  | Optional | Description of the advertisement                                                    |
| duration        | Double  | Optional | The number in seconds that the advertisement goes for                               |
| type            | String  | Optional | If the ad is linear or not, Example: "linear"                                       |
| universal_ad_id | String  | Optional | The universal ad ID for the advertisement                                           |
| viewable        | Boolean | Optional | Whether or not the advertisement is considered viewable by the player's measurement |

## Example Application

Clone this repo to see some examples of using Pancast Assist. The android application contains the following activities:

- `ManualHarnessActivity` Simple list of buttons for creating a Pancast Assist session and sending video and ad events.
- `YoutubePlayerActivity` Example of how to connect Pancast Assist to a YoutTube player and report video events.
- `ExoplayerAdsActivity` More comprehensive player integration with both video and ad events.
