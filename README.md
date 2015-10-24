ifttt-build-notifier
===================
A Simple Jenkins Build Status Notifier for IFTTT Maker Channel Trigger

Posts a Web request with build status to IFTTT Maker Channel to tigger actions with all other Channels on IFTTT. For example tweet or send a build notification or Light Up a IoT connected device.

Create an installable artifact:

1. `git clone https://github.com/upgundecha/ifttt-build-notifier.git`
2. `cd ifttt-build-notifier`
3. `mvn clean install`
4. (wait for mvn to download the internet)
5. Manage Jenkins > Plugins > Advanced > Upload ```./target/snsnotify.hpi```
6. Restart Jenkins ([$JENKINS_URL]/restart)

Now, login to IFTTT and do the following:

1. Create a new Recipe
2. Click on `this` part
2. Enter or select `Maker Channel` in `Choose Trigger Channel` 
3. Select `Receive a Web Request` in `Choose a Trigger`
4. Enter an Event name for example `build_notification`
5. Click on `Create Trigger` button
6. Next, click on `that` part
7. Enter or select `Twitter` in `Choose Action Channel`
8. Select `Send a direct message to yourself` in `Choose an Actin`
9. Enter following message in `Message field` in `Complete Action Fields`

10. Finally click on `Create Recipe` 


1. Create an SNS Topic, subscribe a target SQS queue (or create subscription via email etc.)
2. Right-click on targeted SQS queue to confirm subscription 

Finally, back to Jenkins...

1. Manage Jenkins > Configure Jenkins to use AWS creds and newly created Topic ARN.
   You can also specify the default bevaiour in case you want to send out also an 
   SNS notification when the build is started (off by default). 
2. As part of your job: add post-build step for SNS notification, optionally configure 
   subject and message (you can make use of build and environment variables, which do 
   get substituted), resp. override Topic ARN (if you do not want to stick with globally 
   configured one).
   
