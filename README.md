# smartStorage

[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23) [![Platform](https://img.shields.io/badge/Platform-Android-brightgreen)](https://developer.android.com/index.html) [![Licence](https://img.shields.io/badge/Licence-Apache--2.0-brightgreen)](https://opensource.org/licenses/Apache-2.0) 


File storage management in Android can be a complex task, requiring developers to handle various permissions, file paths, and platform-specific constraints. The Smart Storage library aims to simplify this process by providing a unified and streamlined approach to file storage, reducing development time and increasing code maintainability.

## Demo

![Preview](https://github.com/smartSenseSolutions/smartStorage/blob/development/preview/preview.gif)

## Setup

Add the Maven Central repository to your project's root ```build.gradle``` file
``` gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```

Add the dependency in your app ```build.gradle``` file:
``` gradle
implementation 'io.github.smartsensesolutions:smartStorage:1.0.0'

```

## Usage

Sample implementation [here](https://github.com/smartSenseSolutions/smartStorage/tree/main/app)

Use the library in your activity by declaring:

``` jetpack Compose
val smartStorage = SmartStorage(this)
	smartStorage.store(
                                    location = SmartDirectory.DOWNLOADS,
                                    fileType = SmartFileType.TXT,
                                    fileName = "testFile",
                                    fileData = "This is a sample txt file.".toByteArray()
                                )
                                )
```
## Usage of parameters:
location = path of storing the file.

location type can be:
- SmartDirectory.INTERNAL
- SmartDirectory.SCOPED_STORAGE
- SmartDirectory.CUSTOM
- SmartDirectory.DOWNLOADS
- SmartDirectory.DOCUMENTS
- SmartDirectory.EXTERNAL_PUBLIC


fileType = type(extension) of the file.

file type can be:
- SmartFileType.TXT
- SmartFileType.JPEG
- SmartFileType.PNG
- SmartFileType.PDF
- SmartFileType.WEBP


file name = name of the file.

file Data = content of the file.




## License
```
Copyright 2021 SmartSense Solutions
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
```
