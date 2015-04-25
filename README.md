# netatmo2wow

Automatic converter of Netatmo weather station data to Met Office Weather Observation Website WOW format. Only temperature and humidity are processed.

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

Site ID
AWS Pin (create on yourself).

Now  with this information the netatmo2wow tool can be run as follows:

#Running netatmo2wow

java -jar netatmo2wow-1.0.jar -clientid <netatmo_client_id> -secret <netatmo_secret> -email <netatmo_email_account> -password <netatmo_password> -timeperiod 7200 -siteid <wow_siteid>  -awspin <wow_aws_pin>

In this example the timeperiod has been set to 1800 seconds (halve an hour).
So running netatmo2wow will update all data from from halve an hour ago in netatmo to WOW.
