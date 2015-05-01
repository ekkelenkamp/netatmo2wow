# netatmo2wow

Automatic converter of Netatmo weather station data to Met Office Weather Observation Website WOW format. The results can be displayed in:

- http://wow.metoffice.gov.uk/
- https://wow.knmi.nl/

The following parameters are processed:

    - temperature
    - humidity
    - pressure at station


Netatmo registeres new measurements each 5 minutes. Netatmo2wow will try to merge measurements of different modules into one WOW measurement if the timestamp of the different modules is less than 2 minutes apart. The most recent measurement will prevail. Only measurements with at least a temperature are uploaded.
Netatmo2wow will store that last uploaded timestamp to avoid uploading duplicate measurements. Only newer measurements wil be uploaded.

#Netatmo registration

To access the netatmo data, the public Netatmo Api is used. See:

https://dev.netatmo.com/

Register for a api key of netatmo by creating an application:

https://dev.netatmo.com/dev/createapp
After registration the following is provided:

- Client id
- Client secret


#WOW registration

Register a site at WOW of the metoffice.
http://wow.metoffice.gov.uk/
After registration the following is provided:

- Site ID
- AWS Pin (create yourself).

Now  with this information the netatmo2wow tool can be run as follows:

#Running netatmo2wow
<pre>
java -jar netatmo2wow-1.1.jar
    -clientid &lt;netatmo_client_id&gt;
    -secret &lt;netatmo_secret&gt;
    -email &lt;netatmo_email_account&gt;
    -password &lt;netatmo_password&gt;
    -timeperiod 600
    -siteid &lt;wow_siteid&gt;
    -awspin &lt;wow_aws_pin&gt;
</pre>

In this example the timeperiod has been set to 600 seconds (ten minutes).
So running netatmo2wow will update all data from the last ten minutes in netatmo to WOW.

#How to install?

Any system that can run a Java SE version 6 or higher can use this tool.
Typically a batch file or shell script should be created that can be run from a scheduled job using a task scheduler or cron job.
Scheduling the job every 5 minutes should do the job.
Tool was tested on:

- Synology NAS with DSM 4.3
- Raspberry PI with OpenElec

#Synololgy NAS server configuration example

A tested setup with a Synology NAS server is as follows:

- Synology DSM 4.3
- Java SE Development Kit 8 Update 33 for ARM
- Cron job on synology that runs every 2 minutes (edit as root):
<pre>
    */5   *   *   *   *   root   /bin/su -c "/volume1/public/netatmo/r.sh" admin
</pre>
- Restart cron scheduler on synology server as root.
<pre>
synoservice -restart crond
</pre>
- Executable script r.sh that runs netatmo2wow:
<pre>
    /volume1/public/java/jdk1.8.0_33/bin/java -jar netatmo2wow-1.1.jar -clientid ......
</pre>

