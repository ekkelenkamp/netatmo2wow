# netatmo2wow
Automatic converter of a Netatmo weather station data to Met Office Weather Observation Website WOW format


#Netatmo public api.
To access the netatmo data, the public Netatmo Api is used.

https://github.com/Netatmo/Netatmo-API-Android


https://dev.netatmo.com/doc/methods/getmeasure


Register for a api key of netatmo:

https://dev.netatmo.com/dev/createapp

Client id
Client secret


Example of netatmo rest calls:

https://api.netatmo.net/api/getuser?access_token=5400b44a1e7759f26a7b24e4|d27b0d6b5b00ef0f7ca019870de30167


https://api.netatmo.net/api/devicelist?access_token=5400b44a1e7759f26a7b24e4|d27b0d6b5b00ef0f7ca019870de30167

https://api.netatmo.net/api/getmeasure?access_token=5400b44a1e7759f26a7b24e4|d27b0d6b5b00ef0f7ca019870de30167&device_id=70%3Aee%3A50%3A03%3Abe%3A92&type=Temperature,Humidity&scale=max&date_begin=1347556500
