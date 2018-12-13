# CheckMyDigitalFootprint

CheckMyDigitalFootprint is an application that allows a user to regulate the email services they have subscribed to, preventing unwanted emails and clutter from rendering their inbox unusable.

CheckMyDigitalFootprint works by using the Gmail API to search through a users emails. Emails that are scanned as "subscription" are then parsed for sender, loaded into the application, and designated as "keep" or "discard" based on the users preference. This is done through parsing the raw email data the Gmail API returns which includes a "From" header and a "List-Subscribed" header. Only emails that include the unsubscribe option at the bottom of the email will have the "List-Subscribed" field. Parsing of emails was decoded using base64url encoding.

## Features

The application has additional features which improve ease of access for the user. These are the save and load file features. The save and load features allow the user to easily access credentials which the Gmail API requires authorization for every time a new user attempts to use their API. Additionally, data is stored as an xml.

Multithreading is used by the application in order to improve the scan time of emails and prevent lag. Alongside with batch calling this results in a very quick process by which the user is able to scan through their entire inbox and there is no evident lag to the emails coming in. Furthermore, multithreading allows us to add different features to the application such as pause and resume which would not have been possible without it due to input lag.

Batch requesting was an additional feature which was necessary for our application to run correctly. Initially we were looping through a users inbox, but by using batches we designated work to the API instead in sending us groups of emails. This resulted in a much faster process as the email ID did not have to be verified every time and is now processed in batches which are returned at once.

## User Manual

A user will load the application, allow the Gmail API to access their email account and press scan. There are pause and resume buttons which allow the user to interact with the scanning process. Emails that are designated as subscription will be parsed by the application for the sender and that sender will be loaded into the application where the user can designate them as keep or discard. Save and load buttons allow the user to keep authorization credentials.

### Setup
Please ensure you have the following:
* [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
* [JavaFX 11](https://openjfx.io/)
* [Gradle 5.0](https://gradle.org/)

Then in `settings.gradle`, set the Java home field to your Java 11 JDK directory:
E.g.

For Mac:

```
org.gradle.java.home=/Library/Java/JavaVirtualMachines/jdk-11.0.1.jdk/Contents/Home
```

For Windows:

```
org.gradle.java.home=/Library/Java/JavaVirtualMachines/jdk-11.0.1.jdk/Contents/Home
```

Then in the project root folder, run:

```
gradle build
gradle run
```

#### Setup for Eclipse
Additional setup is required for Eclipse. Ensure that the following are installed:
* [Eclipse 4.9](https://www.eclipse.org/)

With the following plugins:
* [Buildship Gradle Integration](https://marketplace.eclipse.org/content/buildship-gradle-integration)
* [e(fx)clipse](https://marketplace.eclipse.org/content/efxclipse)
* [Java 11 Support for Eclipse 2018-09 (4.9)](https://marketplace.eclipse.org/content/java-11-support-eclipse-2018-09-49)

Open the project from `File > Open Projects from File System...` and make the changes to `settings.gradle`.

You will also need to add the JavaFX 11 library by right clicking the project `Build Path > Configure Build Path > Libraries > Add Library > User Library`.

Also ensure that Java 11 is set as the JRE.

Then right click project folder again and click on `Configure > Add Gradle Nature`. Eclipse should now recognise the project as a Gradle project. Then right click again and click on `Gradle > Refresh Gradle Project` to install the dependencies.

One last step before you run the application: goto `Run > Run Configurations > Arguments` and under `VM Arguments` paste in the following ensuring that you copy in your own JavaFX11 SDK jar path:

```
--module-path="<PATH TO JAVAFX SDK 111>/javafx-sdk-11.0.1/lib"
--add-modules=javafx.controls
--add-exports=javafx.graphics/com.sun.javafx.util=ALL-UNNAMED
--add-exports=javafx.base/com.sun.javafx.reflect=ALL-UNNAMED
--add-exports=javafx.base/com.sun.javafx.beans=ALL-UNNAMED
--add-exports=javafx.graphics/com.sun.glass.utils=ALL-UNNAMED
--add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED
```

Finally you will be able to get the application running! (If you haven't noticed it's much easier to just use Gradle ;))

#### Gmail API Key Setup

On first load of the application you will be prompted to upload an API key for Gmail. This is to give you a peace of mind that we're not out to steal your data in any way or form!

Gmail API keys can be downloaded as a `credentials.json`. It will require you to enable the Gmail API from your Google account. Please click on the blue "ENABLE THE GMAIL API" button [here](https://developers.google.com/gmail/api/quickstart/java?authuser=3).


#### Scanning your inbox

Once your API key is loaded your ready to start scanning. Simply press the big purple "Scan" button and start scanning away! All listserver email addresses automatically goto the "Delete List", but you may choose to decide to keep some. If so, simply click on the cell and move to the keep list.

Ensure that after each scan you save your data so that you don't have to restart again the next time you reload the app.

Enjoy!
