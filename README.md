## CheckMyDigitalFootprint

CheckMyDigitalFootprint is an application that allows a user to regulate the email services they have subscribed to, preventing unwanted emails and clutter from rendering their inbox unusable. 

CheckMyDigitalFootprint works by using the Gmail API to search through a users emails. Emails that are scanned as "subscription" are then parsed for sender, loaded into the application, and designated as "keep" or "discard" based on the users preference. This is done through parsing the raw email data the Gmail API returns which includes a "from" header and a "list-subscribed" header. Only emails that include the unsubscribe option at the bottom of the email will have the "list-subscribed" field. Parsing of emails was decoded using base64url encoding.

# Features

The application has additional features which improve ease of access for the user. These are the save and load file features. The save and load features allow the user to easily access credentials which the Gmail API requires authorization for every time a new user attempts to use their API. Additionally, data is stored as an xml.

Multithreading is used by the application in order to improve the scan time of emails and prevent lag. Alongside with batch calling this results in a very quick process by which the user is able to scan through their entire inbox and there is no evident lag to the emails coming in. Furthermore, multithreading allows us to add different features to the application such as pause and resume which would not have been possible without it due to input lag.

Batch requesting was an additional feature which was necessary for our application to run correctly. Initially we were looping through a users inbox, but by using batches we designated work to the API instead in sending us groups of emails. This resulted in a much faster process as the email ID did not have to be verified every time and is now processed in batches which are returned at once.

# User Manual

A user will load the application, allow the Gmail API to access their email account and press scan. There are pause and resume buttons which allow the user to interact with the scanning process. Emails that are designated as subscription will be parsed by the application for the sender and that sender will be loaded into the application where the user can designate them as keep or discard. Save and load buttons allow the user to keep authorization credentials.