# netatmo2wow
Automatic converter of a Netatmo weather station data to Met Office Weather Observation Website WOW format


#Netatmo public api.
To access the netatmo data, the public Netatmo Api is used.

https://github.com/Netatmo/Netatmo-API-Android


https://dev.netatmo.com/doc/methods/getmeasure


Register for a api key of netatmo:

https://dev.netatmo.com/dev/createapp
After registration the following is provided:

Client id
Client secret

Register a site at WOW of the metoffice.
http://wow.metoffice.gov.uk/
After registration the following is provided:

Site ID
AWS Pin (create on yourself).

Now  with this information the netatmo2wow tool can be run as follows:

java -jar netatmo2wow-1.0.jar -clientid <netatmo_client_id> -secret <netatmo_secret> -email <netatmo_email_account> -password <netatmo_password> -timeperiod 7200 -siteid <wow_siteid>  -awspin <wow_aws_pin>

In this example the timeperiod has been set to 7200 seconds (one hour).
So running netatmo2wow every hour will keep update all data from netatmo to wow every hour.
