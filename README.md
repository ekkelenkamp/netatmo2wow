# netatmo2wow

Automatic converter of Netatmo weather station data to Met Office Weather Observation Website WOW format. The results can be displayed in:

- http://wow.metoffice.gov.uk/
- https://wow.knmi.nl/

Temperature and humidity are processed.

#Netatmo registration

To access the netatmo data, the public Netatmo Api is used. See:

https://dev.netatmo.com/

Register for a api key of netatmo by creating an application:

https://dev.netatmo.com/dev/createapp
After registration the following is provided:

Client id
Client secret


#WOW registration

Register a site at WOW of the metoffice.
http://wow.metoffice.gov.uk/
After registration the following is provided:

- Site ID
- AWS Pin (create yourself).

Now  with this information the netatmo2wow tool can be run as follows:

#Running netatmo2wow
<pre>
java -jar netatmo2wow-1.0.jar
    -clientid &lt;netatmo_client_id&gt;
    -secret &lt;netatmo_secret&gt;
    -email &lt;netatmo_email_account&gt;
    -password &lt;netatmo_password&gt;
    -timeperiod 7200
    -siteid &lt;wow_siteid&gt;
    -awspin &lt;wow_aws_pin&gt;
</pre>

In this example the timeperiod has been set to 1800 seconds (halve an hour).
So running netatmo2wow will update all data from from halve an hour ago in netatmo to WOW.
