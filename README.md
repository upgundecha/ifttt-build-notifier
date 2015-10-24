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

Let's setup a build notification trigger which will send a direct twitter message when a build is completed with following steps:

First, login to IFTTT and do the following:

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
```
Jenkins Build Status for Project: {{Value1}}, Build Number:{{Value2}}, Status: {{Value3}} 
```
10. Finally click on `Create Action` button
11. Give a name to newly created recipe
12. Click on `Create Recipe` button to create the receipe
13. Now, navigate to https://ifttt.com/maker
14. Copy the Key from `Your key is:` section

Back in Jenkins, let's setup a project to send build notifications:

1. Configure the Job or Project for which you want to enable IFTTT trigger
2. Add `IFTTT Build Notifier` action from `Add Post Build Action` list
3. Enter `Event Name` specified in Step#4 above (for example `build_notification`)
4. Enter `Key copied` from Step#14 above
3. Save the configuration
4. That's it
   
Now whenever a build triggers, you will see a Twitter message with build status
